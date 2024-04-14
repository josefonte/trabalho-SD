package org.example;

/*
* Node class
*   - each server has storage of files ()
*   - routing table of other servers (every node knows other nodes location)
*   - upload file (given a key)
*   - download file (given a key)
* Virtual Node
* ConsistentHash
*   - hash server ip+porta
*   -
 * */


import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
      Service service = new Service();
      service.addNode("8000");
      service.printState();
    }

}
