import grpc.DataStorageGrpc;
import grpc.DownloadRequest;
import grpc.FileContent;
import grpc.UploadRequest;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GrpcDataServer {
    private static final int PORT = 9000;
    private static final String OUTPUT = "src/main/java/output";
    private Map<String, List<FileContent>> fileMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        GrpcDataServer server = new GrpcDataServer();
        server.start();
        server.blockUntilShutdown();
        try {
            // Abre o fluxo de escrita para o arquivo
            BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT));
            // Escreve uma string vazia para limpar o conteúdo do arquivo
            writer.write("");
            // Fecha o fluxo de escrita
            writer.close();

            System.out.println("Conteúdo do arquivo apagado com sucesso.");
        } catch (IOException e) {
            System.out.println("Erro ao manipular o arquivo: " + e.getMessage());
        }
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

    private void printFileMap() {
        System.out.println("File Map Contents:");
        Map<String, List<FileContent>> fileMapCopy = new HashMap<>(fileMap); // Cria uma cópia do fileMap
        for (Map.Entry<String, List<FileContent>> entry : fileMapCopy.entrySet()) {
            System.out.println("File Key: " + entry.getKey());
            List<FileContent> fileList = entry.getValue();
            for (FileContent fileContent : fileList) {
                System.out.println("  File Name: " + fileContent.getFileName());
                System.out.println("  File Size: " + fileContent.getFileContent().size());
                System.out.println("  Status: " + fileContent.getStatus());
            }
        }
    }

    private class DataStorageImpl extends DataStorageGrpc.DataStorageImplBase {
        @Override
        public void uploadFile(UploadRequest request, StreamObserver<FileContent> responseObserver) {
            int seg = 0;
            String fileKey = request.getFileKey();
            List<FileContent> fileList = fileMap.computeIfAbsent(fileKey, k -> new ArrayList<>());
            FileContent fileContent = FileContent.newBuilder()
                    .setFileName(request.getFileName())
                    .setFileContent(request.getFileContent())
                    .setStatus("pending")
                    .setKey(fileKey)
                    .build();


            synchronized (fileList) {
                fileList.add(fileContent); // Adiciona o conteúdo do arquivo à lista de arquivos
                // Aqui você pode escrever o conteúdo do arquivo em ordem para o sistema de arquivos
                // Certifique-se de manipular exceções de E/S adequadamente
                // Exemplo de escrita em um arquivo:
                File outputFile = new File(OUTPUT);
                try (FileOutputStream fos = new FileOutputStream(outputFile, true)) {
                    if (!outputFile.exists()) {
                        // Se o arquivo não existe, cria um novo
                        outputFile.createNewFile();
                    }
                    fos.write(request.getFileContent().toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                    // Lida com a exceção de E/S aqui
                }
            }


            //printFileMap();
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
