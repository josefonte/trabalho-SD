package psd.trabalho;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;


import io.grpc.stub.StreamObserver;


public class GrpcServer  {
/*
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(8080)
                .addService((BindableService) new GrpcServer())
                .build().start();

        server.awaitTermination();
    }

    @Override
    public void greet(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        String name = request.getName();
        HelloResponse response = HelloResponse.newBuilder().setMessage("Hello " + name).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    @Override
    public void multiGreet (HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        String name = request.getName();
        responseObserver.onNext(HelloResponse.newBuilder().setMessage("Welcome " + name).build());
        responseObserver.onNext(HelloResponse.newBuilder().setMessage("como estas " + name).build());
        responseObserver.onNext(HelloResponse.newBuilder().setMessage("Adeus " + name).build());
        responseObserver.onCompleted();
    }

    @Override
    public  StreamObserver<HelloRequest> streamGreet(StreamObserver<HelloResponse> responseObserver){

        return new StreamObserver<HelloRequest>() {
            @Override
            public void onNext(HelloRequest request) {
                responseObserver.onNext(HelloResponse.newBuilder().setMessage("Hello " + request.getName()).build());
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };

    }*/


}
