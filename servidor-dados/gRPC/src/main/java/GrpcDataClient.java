import com.google.protobuf.ByteString;
import grpc.DataStorageGrpc;
import grpc.UploadRequest;
import grpc.DownloadRequest;
import grpc.FileContent;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.io.*;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;

public class GrpcDataClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8000;
    private static final Integer MAX_MESSAGE_SIZE = 1024 * 1024 * 1024;

    public static void main(String[] args) throws InterruptedException, IOException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .maxInboundMessageSize(MAX_MESSAGE_SIZE)
                .build();

        DataStorageGrpc.DataStorageStub stub = DataStorageGrpc.newStub(channel);

        System.out.println("Choose an option: \n1 - Upload, 2 - Download");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        switch (option) {
            case 1:
                uploadFile(stub, scanner);
                break;
            case 2:
                downloadFile(stub, scanner);
                break;
            default:
                System.out.println("Invalid option");
        }
        channel.shutdown();
    }

    private static void uploadFile(DataStorageGrpc.DataStorageStub stub, Scanner scanner) throws IOException, InterruptedException {
        System.out.println("Enter the path of the file to upload:");
        String filePath = scanner.next();

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not found.");
            return;
        }

        System.out.println("Enter the key:");
        String key = scanner.next();

        int chunkSize = 1024 * 1024; // 1MB chunk size
        byte[] buffer = new byte[chunkSize];
        int totalChunks = (int) Math.ceil((double) file.length() / chunkSize);

        CountDownLatch uploadCompleteLatch = new CountDownLatch(1);

        StreamObserver<FileContent> responseObserver = new StreamObserver<FileContent>() {
            @Override
            public void onNext(FileContent value) {
                System.out.println("Response received from server: " + value.getStatus());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("Error during file upload: " + t.getMessage());
                uploadCompleteLatch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Upload completed successfully.");
                uploadCompleteLatch.countDown();
            }
        };

        StreamObserver<UploadRequest> requestObserver = stub.uploadFile(responseObserver);

        try (FileInputStream fis = new FileInputStream(file)) {
            int bytesRead;
            int seg = 0;
            while ((bytesRead = fis.read(buffer)) != -1) {
                UploadRequest request = UploadRequest.newBuilder()
                        .setFileName(file.getName())
                        .setFileKey(key)
                        .setFileContent(ByteString.copyFrom(buffer, 0, bytesRead))
                        .setSeg(seg++)
                        .setTotalChunks(totalChunks)
                        .build();
                requestObserver.onNext(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
            requestObserver.onError(e);
        }

        requestObserver.onCompleted();
        uploadCompleteLatch.await();
    }

    private static void downloadFile(DataStorageGrpc.DataStorageStub stub, Scanner scanner) throws InterruptedException {
        System.out.println("Enter the directory to save the file:");
        String saveDirectory = scanner.next();

        System.out.println("Enter the key:");
        String key = scanner.next();

        DownloadRequest request = DownloadRequest.newBuilder()
                .setFileKey(key)
                .build();

        CountDownLatch downloadCompleteLatch = new CountDownLatch(1);
        ConcurrentSkipListMap<Long, ByteString> chunksMap = new ConcurrentSkipListMap<>();
        final String[] receivedFileName = {null}; // Use an array to allow modification within inner class

        StreamObserver<FileContent> responseObserver = new StreamObserver<FileContent>() {
            @Override
            public void onNext(FileContent value) {
                chunksMap.put(value.getSeg(), value.getFileContent());
                if (receivedFileName[0] == null) {
                    receivedFileName[0] = value.getFileName(); // Store the file name including extension from the server
                }
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("Error during file download: " + t.getMessage());
                downloadCompleteLatch.countDown();
            }

            @Override
            public void onCompleted() {
                try {
                    if (receivedFileName[0] != null) {
                        File file = new File(saveDirectory, receivedFileName[0]); // Use the received file name
                        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
                            for (Map.Entry<Long, ByteString> entry : chunksMap.entrySet()) {
                                raf.seek(entry.getKey() * 1024 * 1024); // Assuming 1MB chunks
                                raf.write(entry.getValue().toByteArray());
                            }
                        }
                        System.out.println("Download completed successfully.");
                    } else {
                        System.out.println("File name not received, download failed.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    downloadCompleteLatch.countDown();
                }
            }
        };

        stub.downloadFile(request, responseObserver);

        downloadCompleteLatch.await();
    }
}
