import grpc.DataStorageGrpc;
import grpc.DownloadRequest;
import grpc.FileContent;
import grpc.UploadRequest;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrpcDataServer {
    private static final int PORT = 8000;
    private Map<String, List<FileContent>> fileMap = new HashMap<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        GrpcDataServer server = new GrpcDataServer();
        server.start();
        server.blockUntilShutdown();
    }

    private void start() throws IOException {
        Server server = ServerBuilder.forPort(PORT)
                .addService(new DataStorageImpl())
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
        public void uploadFile(UploadRequest request, StreamObserver<FileContent> responseObserver) {
            String fileKey = request.getFileKey();
            List<FileContent> fileList = fileMap.computeIfAbsent(fileKey, k -> new ArrayList<>());
            FileContent fileContent = FileContent.newBuilder()
                    .setFileName(request.getFileName())
                    .setFileContent(request.getFileContent())
                    .setStatus("pending")
                    .setKey(fileKey)
                    .build();
            fileList.add(fileContent);
            responseObserver.onNext(fileContent);
            responseObserver.onCompleted();
        }

        @Override
        public void downloadFile(DownloadRequest request, StreamObserver<FileContent> responseObserver) {
            String fileKey = request.getFileKey();
            List<FileContent> fileList = fileMap.get(fileKey);
            if (fileList != null) {
                for (FileContent fileContent : fileList) {
                    responseObserver.onNext(fileContent);
                }
            }
            responseObserver.onCompleted();
        }
    }
}
