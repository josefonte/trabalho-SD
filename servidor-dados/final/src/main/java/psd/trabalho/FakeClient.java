package psd.trabalho;


import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import node.proto.Rx3DataServerNodeGrpc;

import node.proto.UploadFileRequest;
import node.proto.UploadFileResponse;
import node.proto.UploadFileRequestTransfer;
import node.proto.UploadFileResponseTransfer;
import node.proto.DownloadFileRequest;
import node.proto.DownloadFileResponse;
import node.proto.PingRequest;
import node.proto.PingResponse;
public class FakeClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8000)
                .usePlaintext()
                .build();

        node.proto.Rx3DataServerNodeGrpc.RxDataServerNodeStub stub = node.proto.Rx3DataServerNodeGrpc.newRxStub(channel);

        fileUpload(stub,"eliseu.jpg");
        fileUpload(stub,"video.mp4");


        downloadFile(stub,"paulinho.jpeg");

        removeFile(stub,"eliseu.jpg");

        channel.shutdown();
    }


    private static void downloadFile(node.proto.Rx3DataServerNodeGrpc.RxDataServerNodeStub stub, String fileName) {
        Single<DownloadFileRequest> req = Single.just(DownloadFileRequest.newBuilder().setFileName(fileName).build());

        AtomicLong totalFileSize = new AtomicLong(0);
        AtomicBoolean isFirstChunk = new AtomicBoolean(true);
        AtomicReference<File> file = new AtomicReference<>();
        AtomicReference<FileOutputStream> fileOutputStream = new AtomicReference<>();
        List<node.proto.DownloadFileResponseTransfer> chunkBuffer = new ArrayList<>();

        System.out.println("Sending download request");

        DownloadFileResponse response = stub.downloadFile(req).blockingGet();

        if (response.getSuccess()) {
            System.out.println("Download permission granted");
            stub.downloadFileTransfer(req)
                    .onBackpressureBuffer()
                    .doOnNext(chunk -> {
                        if (isFirstChunk.get()) {
                            System.out.println("#### File Download started");
                            System.out.println("First Chunk -> " + chunk.getFileName());
                            try {
                                File newFile = new File("./final/src/main/java/psd/trabalho/files_client/" + chunk.getFileName());
                                file.set(newFile);
                                fileOutputStream.set(new FileOutputStream(String.valueOf(file)));

                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            isFirstChunk.set(false);
                            System.out.println("File created successfully! ");
                        }
                        totalFileSize.addAndGet(chunk.getFileData().size());
                        chunkBuffer.add(chunk);
                        if (chunkBuffer.size() == 40) {
                            writeBufferToFile(chunkBuffer, fileOutputStream.get());
                            chunkBuffer.clear();
                        }
                    })
                    .ignoreElements()
                    .doOnComplete(() -> {
                        if (!chunkBuffer.isEmpty()) {
                            writeBufferToFile(chunkBuffer, fileOutputStream.get());
                            chunkBuffer.clear();
                            System.out.println("#### File downloaded successfully! | Total size: " + totalFileSize.get() + " bytes");
                        }
                        if (file.get() != null && fileOutputStream.get() != null) {
                            fileOutputStream.get().close();
                        }
                    })
                    .onErrorComplete(error -> {
                        System.err.println("Error occurred during file download: " + error.getMessage());
                        if (file.get() != null && fileOutputStream.get() != null) {
                            file.get().delete();
                            fileOutputStream.get().close();
                        }
                        return false;
                    })
                    .blockingSubscribe();
        }
        else {
            System.out.println("Error downloading file: " + response.getFileName());
            System.out.println("Error: " + response.getErrorMessage() + " from " + response.getNodeIp() + ":" + response.getNodePort());
        }


    }
    private static void writeBufferToFile(List<node.proto.DownloadFileResponseTransfer> chunkBuffer, FileOutputStream fileOutputStream) {
        try {
            System.out.println("Writing buffer to file");
            for (node.proto.DownloadFileResponseTransfer chunk : chunkBuffer) {fileOutputStream.write(chunk.getFileData().toByteArray());}
            fileOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void fileUpload(node.proto.Rx3DataServerNodeGrpc.RxDataServerNodeStub stub, String fileName) throws IOException {
        UploadFileRequest req = UploadFileRequest.newBuilder().setFileName(fileName).build();

        UploadFileResponse auth = stub.uploadFile(req).blockingGet();

        if(auth.getSuccess()){
            File file = new File("./final/src/main/java/psd/trabalho/files_client/" + fileName);

            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                Flowable<UploadFileRequestTransfer> requestFlowable = Flowable.generate(emitter -> {
                    byte[] buffer = new byte[1024]; // Chunk size
                    int bytesRead = fileInputStream.read(buffer);
                    if (bytesRead == -1) {
                        emitter.onComplete(); // No more data to read
                    } else {
                        byte[] chunk = Arrays.copyOf(buffer, bytesRead);
                        emitter.onNext(UploadFileRequestTransfer.newBuilder()
                                .setFileName(file.getName())
                                .setFileData(ByteString.copyFrom(chunk))
                                .build()); // Emit next chunk
                    }
                });
                System.out.println("Starting file transfer");
                stub.uploadFileTransfer(requestFlowable).onErrorComplete(
                                throwable -> {
                                    System.err.println("Error uploading file: " + throwable.getMessage());
                                    return false;
                                }
                        )
                        .blockingSubscribe(response -> {
                            System.out.println("File Transfered: " + response.getSuccess() );
                        });}}
        else {
            System.out.println("Error uploading file: " + auth.getFileName());
            System.out.println("Error: " + auth.getErrorMessage() + " from " + auth.getNodeIp() + ":" + auth.getNodePort());
        }


    }

    private static void removeFile(node.proto.Rx3DataServerNodeGrpc.RxDataServerNodeStub stub, String fileName) {
        Single<node.proto.RemoveRequest> req = Single.just(node.proto.RemoveRequest.newBuilder().setFileName(fileName).build());

        System.out.println("Sending remove request");

        node.proto.RemoveResponse response = stub.removeFile(req).blockingGet();

        if (response.getSuccess()) {
            System.out.println("File removed successfully");
        }
        else {
            System.out.println("Error removing file");
          }
    }

    private static void ping(node.proto.Rx3DataServerNodeGrpc.RxDataServerNodeStub  stub){
        Single<PingRequest> req = Single.just(PingRequest.newBuilder().setNodeIp("localhost").setNodePort("0000").setMessage("PING").build());

        System.out.println("Sending ping");

        stub.pingPong(req).blockingSubscribe(r->{
            System.out.println("Response: " + r.getMessage() + " from " + r.getNodeIp() + ":" + r.getNodePort());
        });
    }
}


