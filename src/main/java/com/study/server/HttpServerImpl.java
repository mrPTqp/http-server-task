package com.study.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServerImpl implements HttpServer {
    private int port;
    private int numberOfThreads;
    private ExecutorService executor;

    public HttpServerImpl(int port, int numberOfThreads) {
        this.port = port;
        this.numberOfThreads = numberOfThreads;
        this.executor = Executors.newFixedThreadPool(numberOfThreads);
    }

    @Override
    public void start() throws IOException {
        int port = this.port;
        ServerSocket serverSocket = new ServerSocket(port);
        Socket clientSocket;
        System.out.println("Server is started");
        while ((clientSocket = serverSocket.accept()) != null) {
            System.out.println("Received connection from " + clientSocket.getRemoteSocketAddress().toString());
            executor.execute(new SocketHandlerImpl(clientSocket));
        }
        executor.shutdown();
    }
}