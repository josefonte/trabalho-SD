package org.example;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.*;


/*
*   - node key, ip_add, ip_port
*   - num_vNodes : num_vNodes per Node
*   - ring : all vNodes position on ring
*   - storage : Node stored values
*   (hashFunc, bytearraycomparator: so TreeMap can organize itself)
*   constructor()
*   lookup(key) : returns closest vNode to key
*
*
*
*
* */

public class Node{

    private final String ip_address =  InetAddress.getLocalHost().getHostAddress();
    private final String ip_port;
    private final byte[] key  ;

    private final int number_vNodes = 5;
    private final HashFunction hash = new HashFunction();
    private final Comparator<byte[]> byteArrayComparator = Arrays::compare;
    private TreeMap<byte[],String> storage = new TreeMap<>(byteArrayComparator);
    private TreeMap<byte[],VirtualNode<Node>> nodes = new TreeMap<>(byteArrayComparator);
    public byte[] getKey(){
        return this.key;
    }

    public String getKeyString(){
        return this.ip_address + " - " + this.ip_port;
    }
    public String getIp_port(){
        return this.ip_port;
    }
    public String getIp_address(){
        return this.ip_address;
    }


    public Node(String ipPort, byte[] key) throws UnknownHostException {
        ip_port = ipPort;
        this.key = key;
    }


    public Node(String port) throws IOException, NoSuchAlgorithmException {
        this.ip_port = port;
        this.key = hash.generateHash( this.ip_address + "|"+ this.ip_port +"|");

        VirtualNode<Node> ogNode = new VirtualNode<>(this,0);
        nodes.put(this.key,ogNode);

        for (int rep=1; rep<number_vNodes; rep ++){
            VirtualNode<Node> vNode = new VirtualNode<>(this, rep);
            byte[] vNode_Key = hash.generateHash(vNode.getKey());
            nodes.put(vNode_Key,vNode);
        }

        //abrir portas
        //start(port);

        //enviar msg para tds os nodes a dizer "add_node,ip,port"

    }
    private void start(String port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(port));
        System.out.println("Listening for clients...");

        Socket clientSocket = serverSocket.accept();
        System.out.println("[IP: " + clientSocket.getInetAddress().toString() + " ,Port: " + clientSocket.getPort() +"]  " + "Client Connection Successful!");
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String greeting = in.readLine();
        if ("hello server".equals(greeting)) {
            out.println("hello client");
        }
        else {
            out.println("unrecognised greeting");
        }

        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    public boolean nodeState(){
        System.out.println("#### KEY");
        System.out.println(this.key);
        System.out.println("#### NODES");
        for ( byte[] node : nodes.keySet()){
            System.out.println(node + " |> " + nodes.get(node).getKey());
        }
        System.out.println("#### NODE STORAGE");
        System.out.println(this.storage);
        return false;
    }

    private void add_Node (String key) throws NoSuchAlgorithmException {
        byte[] node_Key = hash.generateHash(key);
        VirtualNode<Node> ogNode = new VirtualNode<>(this,0);
        nodes.put(node_Key,ogNode);

        for (int rep=1; rep<number_vNodes; rep ++){
            VirtualNode<Node> vNode = new VirtualNode<>(this, rep);
            byte[] vNode_Key = hash.generateHash(vNode.getKey());
            nodes.put(vNode_Key,vNode);
        }

    }
    private void remove_Node (Node node) throws NoSuchAlgorithmException {
        for(VirtualNode<Node> vNode : nodes.values()){
            if(vNode.isVirtualNodeOf(node)){
                remove_vNode(vNode);
            }
        }
    }

    private  void remove_vNode(VirtualNode<Node> vNode) throws NoSuchAlgorithmException {
        //
        byte[] vNode_key_hash = hash.generateHash(vNode.getKey());
        migrate_keys(vNode_key_hash, "remove_Node");
        nodes.remove(vNode_key_hash);
    }
    private void migrate_keys(byte[] key, String mode){
        if (mode == "add_Node"){
            //add_Node
        } else if (mode == "remove_Node") {
            //remove_Node
        }

    }

    private void transfer_keys(ArrayList<byte[]> list , VirtualNode<Node> vNode){
        Node dest_node =  vNode.getPhysicalNode();
       //send all key values to


    }

    private void checkMigrations() throws NoSuchAlgorithmException {
        ArrayList<byte[]> transferKeys = new ArrayList<>();

        for(byte[] key : storage.keySet()){
            VirtualNode<Node> storedNode =  lookupClosestNode(key);
            assert storedNode != null;
            if(!storedNode.isVirtualNodeOf(this)){
               transferKeys.add(storedNode.getKey());
            }
        }


    }

    private VirtualNode<Node> lookupClosestNode(byte[] key) throws NoSuchAlgorithmException {
        if (nodes.isEmpty()) {
            return null;
        }
        SortedMap<byte[], VirtualNode<Node>> tailMap = nodes.tailMap(key, false);
        if (tailMap.isEmpty()) {
            return nodes.firstEntry().getValue();
        }
        return tailMap.firstEntry().getValue();
    }



    private void download_file(String key ){
        // lookup Node
    }

    private void upload_file(String key,String value ){
        // hash the key
        // locate node
        // upload_file
    }



}
