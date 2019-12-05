package com.study.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class HttpServerImpl implements HttpServer {
    private int port;
    private int numberOfThreads;
    private ExecutorService executor;

    public HttpServerImpl(int port, int numberOfThreads) {
        this.port = port;
        this.numberOfThreads = numberOfThreads;
        this.executor = Executors.newFixedThreadPool(8);
    }

    @Override
    public void start() throws IOException {
        int port = this.port;
        ServerSocket socket = new ServerSocket(port);
        Socket client;
        while ((client = socket.accept()) != null) {
            System.out.println("Received connection from " + client.getRemoteSocketAddress().toString());
            executor.execute(new SocketHandlerImpl(client));
        }
        executor.shutdown();
    }
}