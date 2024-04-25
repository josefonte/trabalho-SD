package org.example;

import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ConsistentHashingService {
    // key space is 2^256
    private final Comparator<byte[]> byteArrayComparator = Arrays::compare;
    private final TreeMap<byte[], String> nodes = new TreeMap<>(byteArrayComparator);
    private final Map<byte[], TreeMap<byte[], String>> nodes_Storage = new TreeMap<>(byteArrayComparator);
    private final HashFunction hashFunction = new HashFunction();

    // Compare the byte arrays lexicographically

    public void printState(){
        System.out.println("NODES");
        for ( byte[] node : nodes.keySet()){
            System.out.println(nodes.get(node));
        }

        System.out.println("NODES_STORAGE");
        for ( byte[] node : nodes_Storage.keySet()) {
            System.out.println(nodes.get(node));
            System.out.print("{");
            System.out.print(nodes_Storage.get(node).values());
            System.out.println("}");
        }
    }

    public void add_Node (String serverName) throws NoSuchAlgorithmException {
        byte[] node_hash = hashFunction.generateHash(serverName);
        nodes.put(node_hash, serverName);
        nodes_Storage.put(node_hash, new TreeMap<>(byteArrayComparator));
    }

    public void remove_Node(String serverName) throws NoSuchAlgorithmException {
        byte[] hash = hashFunction.generateHash(serverName);
        migrate_Keys(hash);
        nodes.remove(hash);
        nodes_Storage.remove(hash);
    }

    private void migrate_Keys(byte[] hash){
        SortedMap<byte[], String> tailMap = nodes.tailMap(hash, false);
        byte[] newNode;
        if (tailMap.isEmpty()) {
            newNode=nodes.firstEntry().getKey();
        }else{
            newNode=tailMap.firstKey();
        }
        nodes_Storage.get(newNode).putAll(nodes_Storage.get(hash));
    }

    public byte[] lookup(String key) throws NoSuchAlgorithmException {
        if (nodes.isEmpty()) {
            return null;
        }
        byte[] hash = hashFunction.generateHash(key);
        SortedMap<byte[], String> tailMap = nodes.tailMap(hash);
        if (tailMap.isEmpty()) {
            return nodes.firstEntry().getKey();
        }
        return tailMap.firstKey();
    }
    public String download_file(String key ) throws NoSuchAlgorithmException {

        if (nodes.isEmpty()) {
            return null;
        }
        // lookup Node
        byte[] key_Hash = hashFunction.generateHash(key); ;
        byte[] node_Hash = lookup(key) ;
        return nodes_Storage.get(node_Hash).get(key_Hash);
    }

    public void upload_file(String value ) throws NoSuchAlgorithmException {

        // hash the key
        // locate node
        // upload_file

        byte[] key_hashed = hashFunction.generateHash(value);
        byte[] node_Hash = lookup(value);
        nodes_Storage.get(node_Hash).put(key_hashed,value);
    }


}
