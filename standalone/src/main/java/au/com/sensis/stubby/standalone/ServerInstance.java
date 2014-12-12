package au.com.sensis.stubby.standalone;

import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public abstract class ServerInstance {

    public static final int SOCKET_BACKLOG = 10;

    protected static InetSocketAddress allInterfaces(final int port) {
        return new InetSocketAddress(port);
    }

    public void shutdown() {
        getServer().stop(1); // wait 1 second for graceful shutdown
    }

    public InetSocketAddress getAddress() {
        return getServer().getAddress();
    }

    public abstract HttpServer getServer();
}
