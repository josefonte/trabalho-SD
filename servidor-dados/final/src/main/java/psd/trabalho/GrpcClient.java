package psd.trabalho;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import node.proto.DataServerNodeGrpc ;
import node.proto.NewNodeRequest;
import node.proto.NewNodeResponse;


public class GrpcClient {

    public static void main(String[] args) {
        /*ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8000)
                .usePlaintext()
                .build();

        DataServerNodeGrpc.DataServerNodeBlockingStub stub = DataServerNodeGrpc.newBlockingStub(channel);


        NewNodeRequest request = NewNodeRequest.newBuilder().setNodeIpAdd("localhost").setNodeIpPort("8001").build();

        NewNodeResponse response = stub.newNode(request);

        System.out.println(response.getNodeIpAdd() + " " + response.getNodeIpPort());


       // stub.multiGreet(request).forEachRemaining(r -> System.out.println(r.getMessage()));*/

    }

}
