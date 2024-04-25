package org.example;


import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class VirtualNode<T extends Node>  {
    final T physicalNode;
    final int replicaIndex;
    final HashFunction hash = new HashFunction() ;

    public VirtualNode(T physicalNode, int replicaIndex) {
        this.replicaIndex = replicaIndex;
        this.physicalNode = physicalNode;
    }


    public byte[] getKey() throws NoSuchAlgorithmException {
        return hash.generateHash( physicalNode.getKeyString() + "-" + replicaIndex);
    }
    public String getKeyString() throws NoSuchAlgorithmException {
        return physicalNode.getKeyString() + "-" + replicaIndex;
    }

    public boolean isVirtualNodeOf(T pNode) {
        return physicalNode.getKey().equals(pNode.getKey());
    }

    public T getPhysicalNode() {
        return physicalNode;
    }
}