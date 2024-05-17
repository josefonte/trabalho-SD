package psd.trabalho;


import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.reactivex.rxjava3.core.Flowable;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

import io.reactivex.rxjava3.core.Single;
import node.proto.Rx3DataServerNodeGrpc;
import node.proto.NewNodeRequest;
import node.proto.NewNodeResponse;
import node.proto.RemoveResponse;
import node.proto.RemoveRequest;
import node.proto.UploadFileRequest;
import node.proto.UploadFileResponse;
import node.proto.PingRequest;
import node.proto.PingResponse;
public class FakeClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8000)
                .usePlaintext()
                .build();

        node.proto.Rx3DataServerNodeGrpc.RxDataServerNodeStub stub = node.proto.Rx3DataServerNodeGrpc.newRxStub(channel);

        fileUpload(stub);

        channel.shutdown();
    }


    private static void fileUpload(node.proto.Rx3DataServerNodeGrpc.RxDataServerNodeStub stub) throws IOException {

        File file = new File("./final/src/main/java/psd/trabalho/FakeClient.java");

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            Flowable<UploadFileRequest> requestFlowable = Flowable.generate(emitter -> {
                byte[] buffer = new byte[1024]; // Chunk size
                int bytesRead = fileInputStream.read(buffer);
                if (bytesRead == -1) {
                    emitter.onComplete(); // No more data to read
                } else {
                    emitter.onNext(UploadFileRequest.newBuilder()
                            .setFileName(file.getName()) // Set the file name
                            .setFileData(ByteString.copyFrom(buffer, 0, bytesRead))
                            .build()); // Emit next chunk
                }
            });

            stub.uploadFile(requestFlowable).onErrorComplete(
                    throwable -> {
                        System.err.println("Error uploading file: " + throwable.getMessage());
                        return false;
                    }
                    )
                    .blockingSubscribe(response -> {
                        System.out.println("Response: " + response.getSuccess() + response.getFileName());}
                    );

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            // Handle error
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


