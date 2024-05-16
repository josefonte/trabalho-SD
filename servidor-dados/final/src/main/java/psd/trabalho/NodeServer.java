package psd.trabalho;

import io.grpc.*;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import io.reactivex.rxjava3.core.Single;
import node.proto.DataServerNodeGrpc ;
import node.proto.NewNodeRequest;
import node.proto.NewNodeResponse;
import node.proto.Rx3DataServerNodeGrpc;

public class NodeServer extends node.proto.Rx3DataServerNodeGrpc.DataServerNodeImplBase {
    private NodeState nodeState = new NodeState();
    private final int number_vNodes = 5;
    private final HashFunction hash = new HashFunction();


    //CONSTRUCTORS
    public NodeServer() {
        super();

        nodeState.setIpPort("");
        nodeState.setIpAddress("");
        nodeState.setKey(new byte[0]);
    }

    public NodeServer(String ip_add, String ip_port) throws NoSuchAlgorithmException {
        super();
        //initializing node info
        nodeState.setIpAddress( ip_add);
        nodeState.setIpPort(ip_port);
        nodeState.setKey(hash.generateHash(ip_add + "-" + ip_port));

        System.out.println("#### Node created: " + ip_add + " | " + ip_port + " | " + nodeState.getKey());
    }



    /*START -> method to start the node
    * @param neighborNodes -> list of neighbor nodes
    * - add virtual nodes to the ring
    * - open server socket
    * - open connections to neighbor nodes
    * - add neighbor nodes to the ring
    * */
    public void start( ArrayList<Pair<String, String>> neighborNodes) throws NoSuchAlgorithmException, IOException, InterruptedException {
       System.out.println("#### Starting node: " + nodeState.getIpAddress() + " | " + nodeState.getIpPort() + " | " + nodeState.getKey());

       System.out.println("Adding Vnodes to ring");
        //add virtual nodes to ring
        for (int i = 0; i < number_vNodes; i++) {
            VirtualNode<NodeState> vNode = new VirtualNode<>(this.nodeState,i);
            nodeState.addRing(vNode.getKey(),vNode);
        }
        nodeState.printState();

        System.out.println("Opening connections to neighbor nodes " + neighborNodes);
        //opening connections to neighbor nodes
        for (Pair<String,String> neighbor : neighborNodes) {
            if (!neighbor.getPair2().equals(nodeState.getIpPort())) {
                try {
                    System.out.println("Connecting with neighbor: " + neighbor.getPair1() + " | " + neighbor.getPair2());
                    String neighbor_ip = neighbor.getPair1();
                    String neighbor_port = neighbor.getPair2();

                    ManagedChannel channel = ManagedChannelBuilder.forAddress(neighbor_ip, Integer.parseInt(neighbor_port))
                            .usePlaintext()
                            .build();

                    //send message to neighbor node that NEW_NODE is joining
                    Rx3DataServerNodeGrpc.RxDataServerNodeStub stub = Rx3DataServerNodeGrpc.newRxStub(channel);

                    Single<NewNodeRequest> request = Single.just(NewNodeRequest.newBuilder().setNodeIpAdd(nodeState.getIpAddress()).setNodeIpPort(nodeState.getIpPort()).build());
                    Single<NewNodeResponse> response = stub.newNode(request);

                    response.subscribe(
                            r -> {
                                try {
                                    System.out.println("#### New node response: " + r.getNodeIpAdd() + " | " + r.getNodeIpPort());
                                    addNode(r.getNodeIpAdd(),r.getNodeIpPort());

                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                }
                            },
                            // Error handling for connection refusal
                            throwable -> {
                                System.out.println("Error connecting to neighbor: " + neighbor.getPair1() + " | " + neighbor.getPair2());
                            });

                    channel.shutdown();

                } catch (Exception e) {
                    System.out.println("Error connecting to neighbor: " + neighbor.getPair1() + " | " + neighbor.getPair2());
                }
            }
        }

        //open server socket
        System.out.println("Opening server socket");
        Server server = ServerBuilder.forPort(Integer.parseInt(nodeState.getIpPort()))
                .addService(this)
                .build().start();


        System.out.println("Server started, listening on " +  nodeState.getKeyString() );

        server.awaitTermination();
    }

    public void addNode(String ip_add, String ip_port) throws NoSuchAlgorithmException {
        System.out.println("#### Adding node: " + ip_add + " | " + ip_port);
        NodeState newNode = new NodeState();
        newNode.setIpAddress(ip_add);
        newNode.setIpPort(ip_port);
        for (int i = 0; i < number_vNodes; i++) {
            VirtualNode<NodeState> vNode = new VirtualNode<>(newNode,i);
            if (!nodeState.getRing().containsKey(vNode.getKey())){
                nodeState.addRing(vNode.getKey(),vNode);
            }
        }
        nodeState.printState();
    }


    //NEW_NODE -> used to add a new node to the ring
    @Override
    public Single<NewNodeResponse> newNode (Single<NewNodeRequest> request) {
        return request.map(req -> {
            System.out.println("#### New Node request: " + req.getNodeIpAdd() + " | " + req.getNodeIpPort());
            String ip_add = req.getNodeIpAdd();
            String ip_port = req.getNodeIpPort();
            try {
                this.addNode(ip_add,ip_port);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            nodeState.printState();
            return NewNodeResponse.newBuilder().setNodeIpAdd(nodeState.getIpAddress()).setNodeIpPort(nodeState.getIpPort()).build();
        }); }

    /*public void newNode(NewNodeRequest request, StreamObserver<NewNodeResponse> responseObserver) {
        System.out.println("#### New Node request: " + request.getNodeIpAdd() + " | " + request.getNodeIpPort());
        System.out.println("ring: " + nodeState.getRing());
        String ip_add = request.getNodeIpAdd();
        String ip_port = request.getNodeIpPort();
        try {
            this.addNode(ip_add,ip_port);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        nodeState.printState();
        responseObserver.onNext(NewNodeResponse.newBuilder().setNodeIpAdd(nodeState.getIpAddress()).setNodeIpPort(nodeState.getIpPort()).build());
        responseObserver.onCompleted();
    }*/
}
