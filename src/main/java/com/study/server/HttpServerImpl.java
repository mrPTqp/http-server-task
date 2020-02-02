package com.study.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServerImpl implements HttpServer {
    private int port;
    private int poolSize;
    private boolean isStopped = false;
    private ServerSocket serverSocket = null;
    private ExecutorService executor;
    private SocketHandlerFactoryImpl socketHandlerFactory;

    public HttpServerImpl(ServerConfiguration config, SocketHandlerFactoryImpl shf) {
        port = config.getPort();
        poolSize = config.getPoolSize();
        executor = Executors.newFixedThreadPool(poolSize);
        socketHandlerFactory = shf;
    }

    @Override
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + port, e);
        }

        while (!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server stopped");
                    break;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }
            executor.execute(socketHandlerFactory.createSocketHandler(clientSocket));
        }
        executor.shutdown();
        System.out.println("Server stopped");
    }

    private synchronized boolean isStopped() {
        return isStopped;
    }

    public synchronized void stop() {
        isStopped = true;
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }
}