import com.google.protobuf.ByteString;
import grpc.DataStorageGrpc;
import grpc.UploadRequest;
import grpc.DownloadRequest;
import grpc.FileContent;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.io.*;
import java.util.Scanner;

public class GrpcDataClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8000;

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        DataStorageGrpc.DataStorageStub stub = DataStorageGrpc.newStub(channel);

        System.out.println("Choose an option: \n1 - Upload, 2 - Download");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        switch (option) {
            case 1:
                try {
                    uploadFile(stub, scanner);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                downloadFile(stub, scanner);
                break;
            default:
                System.out.println("Invalid option");
        }
        channel.shutdown();
    }

    private static void uploadFile(DataStorageGrpc.DataStorageStub stub, Scanner scanner) throws IOException {
        System.out.println("Enter the path of the file to upload:");
        String filePath = scanner.next();

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not found.");
            return;
        }

        System.out.println("Enter the key:");
        String key = scanner.next();

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            String fileName = file.getName();

            StreamObserver<FileContent> responseObserver = new StreamObserver<FileContent>() {
                @Override
                public void onNext(FileContent value) {
                    System.out.println("Response received from server: " + value.getStatus());
                }

                @Override
                public void onError(Throwable t) {
                    System.out.println("Error during file upload: " + t.getMessage());
                }

                @Override
                public void onCompleted() {
                    System.out.println("Upload completed successfully.");
                }
            };

            while ((bytesRead = fis.read(buffer)) != -1) {
                UploadRequest request = UploadRequest.newBuilder()
                        .setFileName(fileName)
                        .setFileKey(key)
                        .setFileContent(ByteString.copyFrom(buffer, 0, bytesRead))
                        .build();
                stub.uploadFile(request, responseObserver);
            }
        }
    }

    private static void downloadFile(DataStorageGrpc.DataStorageStub stub, Scanner scanner) {
        System.out.println("Enter the file name to download:");
        String fileName = scanner.next();

        System.out.println("Enter the directory to save the file:");
        String saveDirectory = scanner.next();

        System.out.println("Enter the key:");
        String key = scanner.next();

        DownloadRequest request = DownloadRequest.newBuilder()
                .setFileName(fileName)
                .setFileKey(key)
                .build();

        StreamObserver<FileContent> responseObserver = new StreamObserver<FileContent>() {
            FileOutputStream outputStream = null;
            long fileSize = 0;
            long currentPosition = 0;

            @Override
            public void onNext(FileContent value) {
                try {
                    if (outputStream == null) {
                        File file = new File(saveDirectory, fileName);
                        outputStream = new FileOutputStream(file, true); // Append mode
                        fileSize = file.length();
                    }
                    outputStream.write(value.getFileContent().toByteArray());
                    currentPosition += value.getFileContent().size();
                    if (fileSize > 0) {
                        System.out.println("Downloaded " + (currentPosition * 100 / fileSize) + "%");
                    } else {
                        System.out.println("Downloading...");
                    }
                } catch (IOException e) {
                    onError(e);
                }
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("Error during file download: " + t.getMessage());
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCompleted() {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    System.out.println("Download completed successfully.");
                } catch (IOException e) {
                    onError(e);
                }
            }
        };

        stub.downloadFile(request, responseObserver);
    }
}