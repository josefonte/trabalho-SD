// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Node.proto

// Protobuf Java Version: 3.25.3
package node.proto;

public interface RemoveResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:node.proto.RemoveResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>bool success = 1;</code>
   * @return The success.
   */
  boolean getSuccess();

  /**
   * <code>string node_ip = 2;</code>
   * @return The nodeIp.
   */
  java.lang.String getNodeIp();
  /**
   * <code>string node_ip = 2;</code>
   * @return The bytes for nodeIp.
   */
  com.google.protobuf.ByteString
      getNodeIpBytes();

  /**
   * <code>string node_port = 3;</code>
   * @return The nodePort.
   */
  java.lang.String getNodePort();
  /**
   * <code>string node_port = 3;</code>
   * @return The bytes for nodePort.
   */
  com.google.protobuf.ByteString
      getNodePortBytes();
}
