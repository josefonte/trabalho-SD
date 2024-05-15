package grpc;

import static grpc.DataStorageGrpc.getServiceDescriptor;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;


@javax.annotation.Generated(
value = "by RxGrpc generator",
comments = "Source: grpc.proto")
public final class Rx3DataStorageGrpc {
    private Rx3DataStorageGrpc() {}

    public static RxDataStorageStub newRxStub(io.grpc.Channel channel) {
        return new RxDataStorageStub(channel);
    }

    public static final class RxDataStorageStub extends io.grpc.stub.AbstractStub<RxDataStorageStub> {
        private DataStorageGrpc.DataStorageStub delegateStub;

        private RxDataStorageStub(io.grpc.Channel channel) {
            super(channel);
            delegateStub = DataStorageGrpc.newStub(channel);
        }

        private RxDataStorageStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
            delegateStub = DataStorageGrpc.newStub(channel).build(channel, callOptions);
        }

        @java.lang.Override
        protected RxDataStorageStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new RxDataStorageStub(channel, callOptions);
        }

        public io.reactivex.rxjava3.core.Single<grpc.FileContent> uploadFile(io.reactivex.rxjava3.core.Flowable<grpc.UploadRequest> rxRequest) {
            return com.salesforce.rx3grpc.stub.ClientCalls.manyToOne(rxRequest,
                new com.salesforce.reactivegrpc.common.Function<io.grpc.stub.StreamObserver<grpc.FileContent>, io.grpc.stub.StreamObserver<grpc.UploadRequest>>() {
                    @java.lang.Override
                    public io.grpc.stub.StreamObserver<grpc.UploadRequest> apply(io.grpc.stub.StreamObserver<grpc.FileContent> observer) {
                        return delegateStub.uploadFile(observer);
                    }
                }, getCallOptions());
        }

        public io.reactivex.rxjava3.core.Flowable<grpc.FileContent> downloadFile(io.reactivex.rxjava3.core.Single<grpc.DownloadRequest> rxRequest) {
            return com.salesforce.rx3grpc.stub.ClientCalls.oneToMany(rxRequest,
                new com.salesforce.reactivegrpc.common.BiConsumer<grpc.DownloadRequest, io.grpc.stub.StreamObserver<grpc.FileContent>>() {
                    @java.lang.Override
                    public void accept(grpc.DownloadRequest request, io.grpc.stub.StreamObserver<grpc.FileContent> observer) {
                        delegateStub.downloadFile(request, observer);
                    }
                }, getCallOptions());
        }

        public io.reactivex.rxjava3.core.Flowable<grpc.FileContent> downloadFile(grpc.DownloadRequest rxRequest) {
            return com.salesforce.rx3grpc.stub.ClientCalls.oneToMany(io.reactivex.rxjava3.core.Single.just(rxRequest),
                new com.salesforce.reactivegrpc.common.BiConsumer<grpc.DownloadRequest, io.grpc.stub.StreamObserver<grpc.FileContent>>() {
                    @java.lang.Override
                    public void accept(grpc.DownloadRequest request, io.grpc.stub.StreamObserver<grpc.FileContent> observer) {
                        delegateStub.downloadFile(request, observer);
                    }
                }, getCallOptions());
        }

    }

    public static abstract class DataStorageImplBase implements io.grpc.BindableService {


        public io.reactivex.rxjava3.core.Single<grpc.FileContent> uploadFile(io.reactivex.rxjava3.core.Flowable<grpc.UploadRequest> request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        public io.reactivex.rxjava3.core.Flowable<grpc.FileContent> downloadFile(grpc.DownloadRequest request) {
            return downloadFile(io.reactivex.rxjava3.core.Single.just(request));
        }

        public io.reactivex.rxjava3.core.Flowable<grpc.FileContent> downloadFile(io.reactivex.rxjava3.core.Single<grpc.DownloadRequest> request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                    .addMethod(
                            grpc.DataStorageGrpc.getUploadFileMethod(),
                            asyncClientStreamingCall(
                                    new MethodHandlers<
                                            grpc.UploadRequest,
                                            grpc.FileContent>(
                                            this, METHODID_UPLOAD_FILE)))
                    .addMethod(
                            grpc.DataStorageGrpc.getDownloadFileMethod(),
                            asyncServerStreamingCall(
                                    new MethodHandlers<
                                            grpc.DownloadRequest,
                                            grpc.FileContent>(
                                            this, METHODID_DOWNLOAD_FILE)))
                    .build();
        }

        protected io.grpc.CallOptions getCallOptions(int methodId) {
            return null;
        }

        protected Throwable onErrorMap(Throwable throwable) {
            return com.salesforce.rx3grpc.stub.ServerCalls.prepareError(throwable);
        }

    }

    public static final int METHODID_UPLOAD_FILE = 0;
    public static final int METHODID_DOWNLOAD_FILE = 1;

    private static final class MethodHandlers<Req, Resp> implements
            io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
        private final DataStorageImplBase serviceImpl;
        private final int methodId;

        MethodHandlers(DataStorageImplBase serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_DOWNLOAD_FILE:
                    com.salesforce.rx3grpc.stub.ServerCalls.oneToMany((grpc.DownloadRequest) request,
                            (io.grpc.stub.StreamObserver<grpc.FileContent>) responseObserver,
                            new com.salesforce.reactivegrpc.common.Function<grpc.DownloadRequest, io.reactivex.rxjava3.core.Flowable<grpc.FileContent>>() {
                                @java.lang.Override
                                public io.reactivex.rxjava3.core.Flowable<grpc.FileContent> apply(grpc.DownloadRequest single) {
                                    return serviceImpl.downloadFile(single);
                                }
                            }, serviceImpl::onErrorMap);
                    break;
                default:
                    throw new java.lang.AssertionError();
            }
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_UPLOAD_FILE:
                    return (io.grpc.stub.StreamObserver<Req>) com.salesforce.rx3grpc.stub.ServerCalls.manyToOne(
                            (io.grpc.stub.StreamObserver<grpc.FileContent>) responseObserver,
                            serviceImpl::uploadFile, serviceImpl::onErrorMap, serviceImpl.getCallOptions(methodId));
                default:
                    throw new java.lang.AssertionError();
            }
        }
    }

}
