package org.example;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

public class Server {
    public static void main(String[] args) throws Exception {
        var ss = ServerSocketChannel.open();
        ss.bind(new InetSocketAddress(12345));
        var loop = new MainLoop();
        var ss_obs = loop.accept(ss);
        ss_obs.subscribe(s -> {
            var s_obs = loop.read(s);
            s_obs.subscribe(bb -> System.out.println("received: "+bb.remaining()));
        });
        loop.run();
    }
}
