import com.google.protobuf.ByteString;
import grpc.DataStorageGrpc;
import grpc.DownloadRequest;
import grpc.FileContent;
import grpc.UploadRequest;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static com.google.common.io.Files.getFileExtension;

public class GrpcDataServer {
    private static final int PORT = 8000;
    private static final String OUTPUT = "src/test/lixo/server/";
    private static final Integer MAX_MESSAGE_SIZE = 1024 * 1024 * 1024;
    private Map<String, String> fileMapPath = new ConcurrentHashMap<>();
    private Map<String, ConcurrentSkipListMap<Integer, ByteString>> fileChunksMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        GrpcDataServer server = new GrpcDataServer();
        server.start();
        server.blockUntilShutdown();
    }

    private void start() throws IOException {
        Server server = ServerBuilder.forPort(PORT)
                .addService(new DataStorageImpl())
                .maxInboundMessageSize(MAX_MESSAGE_SIZE)
                .build()
                .start();
        System.out.println("Server started, listening on " + PORT);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server");
            server.shutdown();
        }));
    }

    private void blockUntilShutdown() throws InterruptedException {
        synchronized (this) {
            this.wait();
        }
    }

    private class DataStorageImpl extends DataStorageGrpc.DataStorageImplBase {
        @Override
        public StreamObserver<UploadRequest> uploadFile(StreamObserver<FileContent> responseObserver) {
            return new StreamObserver<UploadRequest>() {
                private String fileKey;
                private String fileName;
                private int totalChunks;

                @Override
                public void onNext(UploadRequest request) {
                    fileKey = request.getFileKey();
                    fileName = request.getFileName();
                    totalChunks = request.getTotalChunks();

                    fileChunksMap.computeIfAbsent(fileKey, k -> new ConcurrentSkipListMap<>())
                            .put((int) request.getSeg(), request.getFileContent());

                    if (fileChunksMap.get(fileKey).size() == totalChunks) {
                        try {
                            writeFile(fileKey, fileName);
                            FileContent response = FileContent.newBuilder()
                                    .setFileName(fileName)
                                    .setStatus("completed")
                                    .setKey(fileKey)
                                    .build();
                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                        } catch (IOException e) {
                            responseObserver.onError(e);
                        }
                    }
                }

                @Override
                public void onError(Throwable t) {
                    responseObserver.onError(t);
                }

                @Override
                public void onCompleted() {
                    System.out.println("Não passa aqui!");
                }

                private void writeFile(String fileKey, String fileName) throws IOException {
                    File outputFile = new File(OUTPUT + fileName);
                    try (RandomAccessFile raf = new RandomAccessFile(outputFile, "rw")) {
                        ConcurrentSkipListMap<Integer, ByteString> chunks = fileChunksMap.get(fileKey);
                        for (Map.Entry<Integer, ByteString> entry : chunks.entrySet()) {
                            raf.seek((long) entry.getKey() * 1024 * 1024); // Assuming 1MB chunks
                            raf.write(entry.getValue().toByteArray());
                        }
                    }
                    fileMapPath.put(fileKey, OUTPUT + fileName);
                    fileChunksMap.remove(fileKey);
                }
            };
        }

        @Override
        public void downloadFile(DownloadRequest request, StreamObserver<FileContent> responseObserver) {
            String fileKey = request.getFileKey();
            String filePath = fileMapPath.get(fileKey);

            if (filePath != null) {
                File fileToDownload = new File(filePath);

                try (RandomAccessFile raf = new RandomAccessFile(fileToDownload, "r")) {
                    long fileSize = fileToDownload.length();
                    int chunkSize = 1024 * 1024; // 1MB per chunk
                    long totalChunks = (fileSize + chunkSize - 1) / chunkSize; // Calculate total chunks

                    String fileName = fileToDownload.getName(); // Extract file name with extension

                    byte[] buffer = new byte[chunkSize];
                    for (int seg = 0; seg < totalChunks; seg++) {
                        int bytesRead = raf.read(buffer);
                        ByteString fileContent = ByteString.copyFrom(buffer, 0, bytesRead);

                        FileContent chunk = FileContent.newBuilder()
                                .setFileName(fileName) // Send the file name including extension
                                .setFileContent(fileContent)
                                .setKey(fileKey)
                                .setSeg(seg)
                                .build();

                        responseObserver.onNext(chunk);
                    }

                    responseObserver.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    responseObserver.onError(Status.NOT_FOUND
                            .withDescription("Problema no download.\nDownload não realizado!")
                            .asRuntimeException());
                }
            } else {
                responseObserver.onError(Status.NOT_FOUND
                        .withDescription("Hash " + fileKey + " not accepted")
                        .asRuntimeException());
            }
        }

    }
}
