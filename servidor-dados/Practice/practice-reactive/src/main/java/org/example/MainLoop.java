package org.example;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.Observer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MainLoop {
    Selector sel = SelectorProvider.provider().openSelector();
    Set<SelectionKey> clients;
    private final ConcurrentHashMap<SocketChannel, ObservableEmitter<ByteBuffer>> readMap;

    public MainLoop() throws IOException {
        this.clients = new HashSet<>();
        this.readMap = new ConcurrentHashMap<>();
    }

    public Observable<SocketChannel> accept(ServerSocketChannel s) {
        return Observable.create(sub -> {
            s.configureBlocking(false);
            s.register(sel, SelectionKey.OP_ACCEPT, sub);
        });
    }


    public Observable<ByteBuffer> read(SocketChannel socketChannel) {
        return Observable.create(subscriber -> {
            readMap.put(socketChannel, subscriber);
            socketChannel.register(sel, SelectionKey.OP_READ);
        });
    }
    /* public void run() {
        for (Iterator<SelectionKey> i = sel.selectedKeys().iterator(); i.hasNext(); ) {
            SelectionKey key = i.next();
            if (key.isAcceptable()) {
                var sub = (ObservableEmitter<SocketChannel>) key.attachment();
                clients.add(s.register(sel, SelectionKey.OP_READ));

                sub.onNext(s);
                if (key.isReadable()) {
                    ByteBuffer buf = ByteBuffer.allocate(100);
                    SocketChannel s = (SocketChannel) key.channel();
                    int r = s.read(buf);
                    if (r < 0) {
                        key.cancel();
                        s.close();
                    } else {
                        buf.flip();
                        for (SelectionKey k : clients) {
                            k.attach(buf.duplicate());
                            k.interestOpsOr(SelectionKey.OP_WRITE);
                        }
                    }
                }
                if (key.isWritable()) {
                    SocketChannel s = (SocketChannel) key.channel();
                    ByteBuffer buf = (ByteBuffer) key.attachment();
                    s.write(buf);
                    key.interestOps(SelectionKey.OP_READ);
                }
                i.remove();
            }


        /*if (key.isAcceptable()) {
            var sub = (ObservableEmitter<SocketChannel>) k.attachment();
            sub.onNext(s);
        }
        }

    }**/
    public void run() throws IOException {
        while (true) {
            sel.selectNow();
            Set<SelectionKey> selectedKeys = sel.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (key.isAcceptable()) {
                    var sub = (ObservableEmitter<SocketChannel>) key.attachment();
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    clients.add(socketChannel.register(sel, SelectionKey.OP_READ));
                    sub.onNext(socketChannel);
                } else if (key.isReadable()) {
                    ByteBuffer buf=ByteBuffer.allocate(100);
                    SocketChannel s=(SocketChannel)key.channel();
                    int r=s.read(buf);
                    if (r<0) {
                        key.cancel();
                        s.close();
                    } else {
                        buf.flip();
                        for(SelectionKey k : clients) {
                            k.attach(buf.duplicate());
                            k.interestOpsOr(SelectionKey.OP_WRITE);
                        }
                    }
                }
                else if (key.isWritable()){
                    SocketChannel s=(SocketChannel)key.channel();
                    ByteBuffer buf=(ByteBuffer)key.attachment();
                    s.write(buf);
                    key.interestOps(SelectionKey.OP_READ);
                }
                keyIterator.remove();
            }
        }
    }

}


