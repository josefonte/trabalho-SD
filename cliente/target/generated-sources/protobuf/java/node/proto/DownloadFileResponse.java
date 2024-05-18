// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Node.proto

// Protobuf Java Version: 3.25.3
package node.proto;

/**
 * Protobuf type {@code node.proto.DownloadFileResponse}
 */
public final class DownloadFileResponse extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:node.proto.DownloadFileResponse)
    DownloadFileResponseOrBuilder {
private static final long serialVersionUID = 0L;
  // Use DownloadFileResponse.newBuilder() to construct.
  private DownloadFileResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private DownloadFileResponse() {
    fileName_ = "";
    nodeIp_ = "";
    nodePort_ = "";
    errorMessage_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new DownloadFileResponse();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return node.proto.NodeProto.internal_static_node_proto_DownloadFileResponse_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return node.proto.NodeProto.internal_static_node_proto_DownloadFileResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            node.proto.DownloadFileResponse.class, node.proto.DownloadFileResponse.Builder.class);
  }

  public static final int SUCCESS_FIELD_NUMBER = 1;
  private boolean success_ = false;
  /**
   * <code>bool success = 1;</code>
   * @return The success.
   */
  @java.lang.Override
  public boolean getSuccess() {
    return success_;
  }

  public static final int FILE_NAME_FIELD_NUMBER = 2;
  @SuppressWarnings("serial")
  private volatile java.lang.Object fileName_ = "";
  /**
   * <code>string file_name = 2;</code>
   * @return The fileName.
   */
  @java.lang.Override
  public java.lang.String getFileName() {
    java.lang.Object ref = fileName_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      fileName_ = s;
      return s;
    }
  }
  /**
   * <code>string file_name = 2;</code>
   * @return The bytes for fileName.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getFileNameBytes() {
    java.lang.Object ref = fileName_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      fileName_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int NODE_IP_FIELD_NUMBER = 3;
  @SuppressWarnings("serial")
  private volatile java.lang.Object nodeIp_ = "";
  /**
   * <code>string node_ip = 3;</code>
   * @return The nodeIp.
   */
  @java.lang.Override
  public java.lang.String getNodeIp() {
    java.lang.Object ref = nodeIp_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      nodeIp_ = s;
      return s;
    }
  }
  /**
   * <code>string node_ip = 3;</code>
   * @return The bytes for nodeIp.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getNodeIpBytes() {
    java.lang.Object ref = nodeIp_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      nodeIp_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int NODE_PORT_FIELD_NUMBER = 4;
  @SuppressWarnings("serial")
  private volatile java.lang.Object nodePort_ = "";
  /**
   * <code>string node_port = 4;</code>
   * @return The nodePort.
   */
  @java.lang.Override
  public java.lang.String getNodePort() {
    java.lang.Object ref = nodePort_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      nodePort_ = s;
      return s;
    }
  }
  /**
   * <code>string node_port = 4;</code>
   * @return The bytes for nodePort.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getNodePortBytes() {
    java.lang.Object ref = nodePort_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      nodePort_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int ERROR_MESSAGE_FIELD_NUMBER = 5;
  @SuppressWarnings("serial")
  private volatile java.lang.Object errorMessage_ = "";
  /**
   * <code>string error_message = 5;</code>
   * @return The errorMessage.
   */
  @java.lang.Override
  public java.lang.String getErrorMessage() {
    java.lang.Object ref = errorMessage_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      errorMessage_ = s;
      return s;
    }
  }
  /**
   * <code>string error_message = 5;</code>
   * @return The bytes for errorMessage.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getErrorMessageBytes() {
    java.lang.Object ref = errorMessage_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      errorMessage_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (success_ != false) {
      output.writeBool(1, success_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(fileName_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, fileName_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(nodeIp_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, nodeIp_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(nodePort_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 4, nodePort_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(errorMessage_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 5, errorMessage_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (success_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(1, success_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(fileName_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, fileName_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(nodeIp_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, nodeIp_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(nodePort_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, nodePort_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(errorMessage_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(5, errorMessage_);
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof node.proto.DownloadFileResponse)) {
      return super.equals(obj);
    }
    node.proto.DownloadFileResponse other = (node.proto.DownloadFileResponse) obj;

    if (getSuccess()
        != other.getSuccess()) return false;
    if (!getFileName()
        .equals(other.getFileName())) return false;
    if (!getNodeIp()
        .equals(other.getNodeIp())) return false;
    if (!getNodePort()
        .equals(other.getNodePort())) return false;
    if (!getErrorMessage()
        .equals(other.getErrorMessage())) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + SUCCESS_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getSuccess());
    hash = (37 * hash) + FILE_NAME_FIELD_NUMBER;
    hash = (53 * hash) + getFileName().hashCode();
    hash = (37 * hash) + NODE_IP_FIELD_NUMBER;
    hash = (53 * hash) + getNodeIp().hashCode();
    hash = (37 * hash) + NODE_PORT_FIELD_NUMBER;
    hash = (53 * hash) + getNodePort().hashCode();
    hash = (37 * hash) + ERROR_MESSAGE_FIELD_NUMBER;
    hash = (53 * hash) + getErrorMessage().hashCode();
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static node.proto.DownloadFileResponse parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static node.proto.DownloadFileResponse parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static node.proto.DownloadFileResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static node.proto.DownloadFileResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static node.proto.DownloadFileResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static node.proto.DownloadFileResponse parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static node.proto.DownloadFileResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static node.proto.DownloadFileResponse parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static node.proto.DownloadFileResponse parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static node.proto.DownloadFileResponse parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static node.proto.DownloadFileResponse parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static node.proto.DownloadFileResponse parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(node.proto.DownloadFileResponse prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code node.proto.DownloadFileResponse}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:node.proto.DownloadFileResponse)
      node.proto.DownloadFileResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return node.proto.NodeProto.internal_static_node_proto_DownloadFileResponse_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return node.proto.NodeProto.internal_static_node_proto_DownloadFileResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              node.proto.DownloadFileResponse.class, node.proto.DownloadFileResponse.Builder.class);
    }

    // Construct using node.proto.DownloadFileResponse.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      success_ = false;
      fileName_ = "";
      nodeIp_ = "";
      nodePort_ = "";
      errorMessage_ = "";
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return node.proto.NodeProto.internal_static_node_proto_DownloadFileResponse_descriptor;
    }

    @java.lang.Override
    public node.proto.DownloadFileResponse getDefaultInstanceForType() {
      return node.proto.DownloadFileResponse.getDefaultInstance();
    }

    @java.lang.Override
    public node.proto.DownloadFileResponse build() {
      node.proto.DownloadFileResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public node.proto.DownloadFileResponse buildPartial() {
      node.proto.DownloadFileResponse result = new node.proto.DownloadFileResponse(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(node.proto.DownloadFileResponse result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.success_ = success_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.fileName_ = fileName_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.nodeIp_ = nodeIp_;
      }
      if (((from_bitField0_ & 0x00000008) != 0)) {
        result.nodePort_ = nodePort_;
      }
      if (((from_bitField0_ & 0x00000010) != 0)) {
        result.errorMessage_ = errorMessage_;
      }
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof node.proto.DownloadFileResponse) {
        return mergeFrom((node.proto.DownloadFileResponse)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(node.proto.DownloadFileResponse other) {
      if (other == node.proto.DownloadFileResponse.getDefaultInstance()) return this;
      if (other.getSuccess() != false) {
        setSuccess(other.getSuccess());
      }
      if (!other.getFileName().isEmpty()) {
        fileName_ = other.fileName_;
        bitField0_ |= 0x00000002;
        onChanged();
      }
      if (!other.getNodeIp().isEmpty()) {
        nodeIp_ = other.nodeIp_;
        bitField0_ |= 0x00000004;
        onChanged();
      }
      if (!other.getNodePort().isEmpty()) {
        nodePort_ = other.nodePort_;
        bitField0_ |= 0x00000008;
        onChanged();
      }
      if (!other.getErrorMessage().isEmpty()) {
        errorMessage_ = other.errorMessage_;
        bitField0_ |= 0x00000010;
        onChanged();
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {
              success_ = input.readBool();
              bitField0_ |= 0x00000001;
              break;
            } // case 8
            case 18: {
              fileName_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000002;
              break;
            } // case 18
            case 26: {
              nodeIp_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000004;
              break;
            } // case 26
            case 34: {
              nodePort_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000008;
              break;
            } // case 34
            case 42: {
              errorMessage_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000010;
              break;
            } // case 42
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private boolean success_ ;
    /**
     * <code>bool success = 1;</code>
     * @return The success.
     */
    @java.lang.Override
    public boolean getSuccess() {
      return success_;
    }
    /**
     * <code>bool success = 1;</code>
     * @param value The success to set.
     * @return This builder for chaining.
     */
    public Builder setSuccess(boolean value) {

      success_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>bool success = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearSuccess() {
      bitField0_ = (bitField0_ & ~0x00000001);
      success_ = false;
      onChanged();
      return this;
    }

    private java.lang.Object fileName_ = "";
    /**
     * <code>string file_name = 2;</code>
     * @return The fileName.
     */
    public java.lang.String getFileName() {
      java.lang.Object ref = fileName_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        fileName_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string file_name = 2;</code>
     * @return The bytes for fileName.
     */
    public com.google.protobuf.ByteString
        getFileNameBytes() {
      java.lang.Object ref = fileName_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        fileName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string file_name = 2;</code>
     * @param value The fileName to set.
     * @return This builder for chaining.
     */
    public Builder setFileName(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      fileName_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>string file_name = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearFileName() {
      fileName_ = getDefaultInstance().getFileName();
      bitField0_ = (bitField0_ & ~0x00000002);
      onChanged();
      return this;
    }
    /**
     * <code>string file_name = 2;</code>
     * @param value The bytes for fileName to set.
     * @return This builder for chaining.
     */
    public Builder setFileNameBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      fileName_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }

    private java.lang.Object nodeIp_ = "";
    /**
     * <code>string node_ip = 3;</code>
     * @return The nodeIp.
     */
    public java.lang.String getNodeIp() {
      java.lang.Object ref = nodeIp_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        nodeIp_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string node_ip = 3;</code>
     * @return The bytes for nodeIp.
     */
    public com.google.protobuf.ByteString
        getNodeIpBytes() {
      java.lang.Object ref = nodeIp_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        nodeIp_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string node_ip = 3;</code>
     * @param value The nodeIp to set.
     * @return This builder for chaining.
     */
    public Builder setNodeIp(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      nodeIp_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>string node_ip = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearNodeIp() {
      nodeIp_ = getDefaultInstance().getNodeIp();
      bitField0_ = (bitField0_ & ~0x00000004);
      onChanged();
      return this;
    }
    /**
     * <code>string node_ip = 3;</code>
     * @param value The bytes for nodeIp to set.
     * @return This builder for chaining.
     */
    public Builder setNodeIpBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      nodeIp_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }

    private java.lang.Object nodePort_ = "";
    /**
     * <code>string node_port = 4;</code>
     * @return The nodePort.
     */
    public java.lang.String getNodePort() {
      java.lang.Object ref = nodePort_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        nodePort_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string node_port = 4;</code>
     * @return The bytes for nodePort.
     */
    public com.google.protobuf.ByteString
        getNodePortBytes() {
      java.lang.Object ref = nodePort_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        nodePort_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string node_port = 4;</code>
     * @param value The nodePort to set.
     * @return This builder for chaining.
     */
    public Builder setNodePort(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      nodePort_ = value;
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>string node_port = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearNodePort() {
      nodePort_ = getDefaultInstance().getNodePort();
      bitField0_ = (bitField0_ & ~0x00000008);
      onChanged();
      return this;
    }
    /**
     * <code>string node_port = 4;</code>
     * @param value The bytes for nodePort to set.
     * @return This builder for chaining.
     */
    public Builder setNodePortBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      nodePort_ = value;
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }

    private java.lang.Object errorMessage_ = "";
    /**
     * <code>string error_message = 5;</code>
     * @return The errorMessage.
     */
    public java.lang.String getErrorMessage() {
      java.lang.Object ref = errorMessage_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        errorMessage_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string error_message = 5;</code>
     * @return The bytes for errorMessage.
     */
    public com.google.protobuf.ByteString
        getErrorMessageBytes() {
      java.lang.Object ref = errorMessage_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        errorMessage_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string error_message = 5;</code>
     * @param value The errorMessage to set.
     * @return This builder for chaining.
     */
    public Builder setErrorMessage(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      errorMessage_ = value;
      bitField0_ |= 0x00000010;
      onChanged();
      return this;
    }
    /**
     * <code>string error_message = 5;</code>
     * @return This builder for chaining.
     */
    public Builder clearErrorMessage() {
      errorMessage_ = getDefaultInstance().getErrorMessage();
      bitField0_ = (bitField0_ & ~0x00000010);
      onChanged();
      return this;
    }
    /**
     * <code>string error_message = 5;</code>
     * @param value The bytes for errorMessage to set.
     * @return This builder for chaining.
     */
    public Builder setErrorMessageBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      errorMessage_ = value;
      bitField0_ |= 0x00000010;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:node.proto.DownloadFileResponse)
  }

  // @@protoc_insertion_point(class_scope:node.proto.DownloadFileResponse)
  private static final node.proto.DownloadFileResponse DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new node.proto.DownloadFileResponse();
  }

  public static node.proto.DownloadFileResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<DownloadFileResponse>
      PARSER = new com.google.protobuf.AbstractParser<DownloadFileResponse>() {
    @java.lang.Override
    public DownloadFileResponse parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<DownloadFileResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<DownloadFileResponse> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public node.proto.DownloadFileResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

