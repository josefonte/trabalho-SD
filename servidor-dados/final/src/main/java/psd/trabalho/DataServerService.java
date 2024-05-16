package psd.trabalho;


import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class DataServerService {
    /*private static DataServerService instance = null;
    private static ArrayList<Node> state = new ArrayList<Node>();
    private DataServerService() {
        state = new ArrayList<>();
    }

    public static DataServerService getInstance() {
        if (instance == null) {
            instance = new DataServerService();
        }
        return instance;
    }

    public static void launchService(int numNodes) throws IOException, NoSuchAlgorithmException, InterruptedException {
        for(int i = 0; i<numNodes ; i++){
            System.out.println("Adding node with port: " + 8000+i);
            addNode("localhost", Integer.toString(8000+i));
        }
    }

    public static void addNode(String ip_add, String ip_port) throws IOException, NoSuchAlgorithmException, InterruptedException {
        // create an arraylist with pairs of ip and port in state
        ArrayList<Pair<String, String>> neighbour_Nodes = new ArrayList<>();

        for (Node node : state) {
            neighbour_Nodes.add(new Pair<>(node.getIp_address(), node.getIp_port()));
        }


        System.out.println("Adding node with ip: " + ip_add + " and port: " + ip_port + " to the state" + neighbour_Nodes);
        Node newNode = new Node(ip_add,ip_port);



        state.add(newNode);
    }

    public void removeNode(){
        //TODO
    }

    public void printState() throws NoSuchAlgorithmException {
        System.out.println("Printing state");
        for(NodeServer node : state){
            node.printNodeState();
        }
    }
*/
}
