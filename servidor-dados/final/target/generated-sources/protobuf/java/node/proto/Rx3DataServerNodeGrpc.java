package node.proto;

import static node.proto.DataServerNodeGrpc.getServiceDescriptor;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;


@javax.annotation.Generated(
value = "by RxGrpc generator",
comments = "Source: Node.proto")
public final class Rx3DataServerNodeGrpc {
    private Rx3DataServerNodeGrpc() {}

    public static RxDataServerNodeStub newRxStub(io.grpc.Channel channel) {
        return new RxDataServerNodeStub(channel);
    }

    public static final class RxDataServerNodeStub extends io.grpc.stub.AbstractStub<RxDataServerNodeStub> {
        private DataServerNodeGrpc.DataServerNodeStub delegateStub;

        private RxDataServerNodeStub(io.grpc.Channel channel) {
            super(channel);
            delegateStub = DataServerNodeGrpc.newStub(channel);
        }

        private RxDataServerNodeStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
            delegateStub = DataServerNodeGrpc.newStub(channel).build(channel, callOptions);
        }

        @java.lang.Override
        protected RxDataServerNodeStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new RxDataServerNodeStub(channel, callOptions);
        }

        public io.reactivex.rxjava3.core.Single<node.proto.NewNodeResponse> newNode(io.reactivex.rxjava3.core.Single<node.proto.NewNodeRequest> rxRequest) {
            return com.salesforce.rx3grpc.stub.ClientCalls.oneToOne(rxRequest,
                new com.salesforce.reactivegrpc.common.BiConsumer<node.proto.NewNodeRequest, io.grpc.stub.StreamObserver<node.proto.NewNodeResponse>>() {
                    @java.lang.Override
                    public void accept(node.proto.NewNodeRequest request, io.grpc.stub.StreamObserver<node.proto.NewNodeResponse> observer) {
                        delegateStub.newNode(request, observer);
                    }
                }, getCallOptions());
        }

        public io.reactivex.rxjava3.core.Single<node.proto.NewNodeResponse> newNode(node.proto.NewNodeRequest rxRequest) {
            return com.salesforce.rx3grpc.stub.ClientCalls.oneToOne(io.reactivex.rxjava3.core.Single.just(rxRequest),
                new com.salesforce.reactivegrpc.common.BiConsumer<node.proto.NewNodeRequest, io.grpc.stub.StreamObserver<node.proto.NewNodeResponse>>() {
                    @java.lang.Override
                    public void accept(node.proto.NewNodeRequest request, io.grpc.stub.StreamObserver<node.proto.NewNodeResponse> observer) {
                        delegateStub.newNode(request, observer);
                    }
                }, getCallOptions());
        }

    }

    public static abstract class DataServerNodeImplBase implements io.grpc.BindableService {

        public io.reactivex.rxjava3.core.Single<node.proto.NewNodeResponse> newNode(node.proto.NewNodeRequest request) {
            return newNode(io.reactivex.rxjava3.core.Single.just(request));
        }

        public io.reactivex.rxjava3.core.Single<node.proto.NewNodeResponse> newNode(io.reactivex.rxjava3.core.Single<node.proto.NewNodeRequest> request) {
            throw new io.grpc.StatusRuntimeException(io.grpc.Status.UNIMPLEMENTED);
        }

        @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                    .addMethod(
                            node.proto.DataServerNodeGrpc.getNewNodeMethod(),
                            asyncUnaryCall(
                                    new MethodHandlers<
                                            node.proto.NewNodeRequest,
                                            node.proto.NewNodeResponse>(
                                            this, METHODID_NEW_NODE)))
                    .build();
        }

        protected io.grpc.CallOptions getCallOptions(int methodId) {
            return null;
        }

        protected Throwable onErrorMap(Throwable throwable) {
            return com.salesforce.rx3grpc.stub.ServerCalls.prepareError(throwable);
        }

    }

    public static final int METHODID_NEW_NODE = 0;

    private static final class MethodHandlers<Req, Resp> implements
            io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
        private final DataServerNodeImplBase serviceImpl;
        private final int methodId;

        MethodHandlers(DataServerNodeImplBase serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_NEW_NODE:
                    com.salesforce.rx3grpc.stub.ServerCalls.oneToOne((node.proto.NewNodeRequest) request,
                            (io.grpc.stub.StreamObserver<node.proto.NewNodeResponse>) responseObserver,
                            new com.salesforce.reactivegrpc.common.Function<node.proto.NewNodeRequest, io.reactivex.rxjava3.core.Single<node.proto.NewNodeResponse>>() {
                                @java.lang.Override
                                public io.reactivex.rxjava3.core.Single<node.proto.NewNodeResponse> apply(node.proto.NewNodeRequest single) {
                                    return serviceImpl.newNode(single);
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
                default:
                    throw new java.lang.AssertionError();
            }
        }
    }

}
