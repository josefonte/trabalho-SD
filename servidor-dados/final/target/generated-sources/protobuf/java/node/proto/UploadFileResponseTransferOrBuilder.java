// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Node.proto

// Protobuf Java Version: 3.25.3
package node.proto;

public interface UploadFileResponseTransferOrBuilder extends
    // @@protoc_insertion_point(interface_extends:node.proto.UploadFileResponseTransfer)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>bool success = 1;</code>
   * @return The success.
   */
  boolean getSuccess();

  /**
   * <code>string file_name = 2;</code>
   * @return The fileName.
   */
  java.lang.String getFileName();
  /**
   * <code>string file_name = 2;</code>
   * @return The bytes for fileName.
   */
  com.google.protobuf.ByteString
      getFileNameBytes();

  /**
   * <code>string error_message = 3;</code>
   * @return The errorMessage.
   */
  java.lang.String getErrorMessage();
  /**
   * <code>string error_message = 3;</code>
   * @return The bytes for errorMessage.
   */
  com.google.protobuf.ByteString
      getErrorMessageBytes();
}
