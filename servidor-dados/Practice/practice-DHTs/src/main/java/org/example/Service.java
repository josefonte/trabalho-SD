package org.example;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


/*
*   state
*  launchService () - launchs 4 nodes sequentiallu
*  addNode() - adds one node by comunicating to random node . add to state.
*  removeNode()  - removes one node by comunicating to another node its removal . remove from state
*  printState() - print DHT Nodes State
* */

public class Service {
    private ArrayList<Node> state = new ArrayList();

    public void launchService(int numNodes) throws IOException, NoSuchAlgorithmException {
        for(int i = 8000; i<numNodes ; i++){
            addNode(Integer.toString(i));
        }
    }
    public void addNode(String porta) throws IOException, NoSuchAlgorithmException {
        // launch node
        Node newNode = new Node(porta);
        state.add(newNode);
    }


    public void removeNode(){

    }

    public void printState(){
        for(Node node : state){
            System.out.println(node.nodeState());
        }

    }


}
