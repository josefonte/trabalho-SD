package grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.61.1)",
    comments = "Source: grpc.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class DataStorageGrpc {

  private DataStorageGrpc() {}

  public static final java.lang.String SERVICE_NAME = "grpc.DataStorage";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<grpc.UploadRequest,
      grpc.FileContent> getUploadFileMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "uploadFile",
      requestType = grpc.UploadRequest.class,
      responseType = grpc.FileContent.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<grpc.UploadRequest,
      grpc.FileContent> getUploadFileMethod() {
    io.grpc.MethodDescriptor<grpc.UploadRequest, grpc.FileContent> getUploadFileMethod;
    if ((getUploadFileMethod = DataStorageGrpc.getUploadFileMethod) == null) {
      synchronized (DataStorageGrpc.class) {
        if ((getUploadFileMethod = DataStorageGrpc.getUploadFileMethod) == null) {
          DataStorageGrpc.getUploadFileMethod = getUploadFileMethod =
              io.grpc.MethodDescriptor.<grpc.UploadRequest, grpc.FileContent>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "uploadFile"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.UploadRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.FileContent.getDefaultInstance()))
              .setSchemaDescriptor(new DataStorageMethodDescriptorSupplier("uploadFile"))
              .build();
        }
      }
    }
    return getUploadFileMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpc.DownloadRequest,
      grpc.FileContent> getDownloadFileMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "downloadFile",
      requestType = grpc.DownloadRequest.class,
      responseType = grpc.FileContent.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<grpc.DownloadRequest,
      grpc.FileContent> getDownloadFileMethod() {
    io.grpc.MethodDescriptor<grpc.DownloadRequest, grpc.FileContent> getDownloadFileMethod;
    if ((getDownloadFileMethod = DataStorageGrpc.getDownloadFileMethod) == null) {
      synchronized (DataStorageGrpc.class) {
        if ((getDownloadFileMethod = DataStorageGrpc.getDownloadFileMethod) == null) {
          DataStorageGrpc.getDownloadFileMethod = getDownloadFileMethod =
              io.grpc.MethodDescriptor.<grpc.DownloadRequest, grpc.FileContent>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "downloadFile"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.DownloadRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.FileContent.getDefaultInstance()))
              .setSchemaDescriptor(new DataStorageMethodDescriptorSupplier("downloadFile"))
              .build();
        }
      }
    }
    return getDownloadFileMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DataStorageStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DataStorageStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DataStorageStub>() {
        @java.lang.Override
        public DataStorageStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DataStorageStub(channel, callOptions);
        }
      };
    return DataStorageStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DataStorageBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DataStorageBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DataStorageBlockingStub>() {
        @java.lang.Override
        public DataStorageBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DataStorageBlockingStub(channel, callOptions);
        }
      };
    return DataStorageBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DataStorageFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DataStorageFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DataStorageFutureStub>() {
        @java.lang.Override
        public DataStorageFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DataStorageFutureStub(channel, callOptions);
        }
      };
    return DataStorageFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void uploadFile(grpc.UploadRequest request,
        io.grpc.stub.StreamObserver<grpc.FileContent> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUploadFileMethod(), responseObserver);
    }

    /**
     */
    default void downloadFile(grpc.DownloadRequest request,
        io.grpc.stub.StreamObserver<grpc.FileContent> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDownloadFileMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service DataStorage.
   */
  public static abstract class DataStorageImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return DataStorageGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service DataStorage.
   */
  public static final class DataStorageStub
      extends io.grpc.stub.AbstractAsyncStub<DataStorageStub> {
    private DataStorageStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataStorageStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DataStorageStub(channel, callOptions);
    }

    /**
     */
    public void uploadFile(grpc.UploadRequest request,
        io.grpc.stub.StreamObserver<grpc.FileContent> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getUploadFileMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void downloadFile(grpc.DownloadRequest request,
        io.grpc.stub.StreamObserver<grpc.FileContent> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getDownloadFileMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service DataStorage.
   */
  public static final class DataStorageBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<DataStorageBlockingStub> {
    private DataStorageBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataStorageBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DataStorageBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<grpc.FileContent> uploadFile(
        grpc.UploadRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getUploadFileMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<grpc.FileContent> downloadFile(
        grpc.DownloadRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getDownloadFileMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service DataStorage.
   */
  public static final class DataStorageFutureStub
      extends io.grpc.stub.AbstractFutureStub<DataStorageFutureStub> {
    private DataStorageFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataStorageFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DataStorageFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_UPLOAD_FILE = 0;
  private static final int METHODID_DOWNLOAD_FILE = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_UPLOAD_FILE:
          serviceImpl.uploadFile((grpc.UploadRequest) request,
              (io.grpc.stub.StreamObserver<grpc.FileContent>) responseObserver);
          break;
        case METHODID_DOWNLOAD_FILE:
          serviceImpl.downloadFile((grpc.DownloadRequest) request,
              (io.grpc.stub.StreamObserver<grpc.FileContent>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getUploadFileMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              grpc.UploadRequest,
              grpc.FileContent>(
                service, METHODID_UPLOAD_FILE)))
        .addMethod(
          getDownloadFileMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              grpc.DownloadRequest,
              grpc.FileContent>(
                service, METHODID_DOWNLOAD_FILE)))
        .build();
  }

  private static abstract class DataStorageBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DataStorageBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return grpc.Grpc.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DataStorage");
    }
  }

  private static final class DataStorageFileDescriptorSupplier
      extends DataStorageBaseDescriptorSupplier {
    DataStorageFileDescriptorSupplier() {}
  }

  private static final class DataStorageMethodDescriptorSupplier
      extends DataStorageBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    DataStorageMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (DataStorageGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DataStorageFileDescriptorSupplier())
              .addMethod(getUploadFileMethod())
              .addMethod(getDownloadFileMethod())
              .build();
        }
      }
    }
    return result;
  }
}
