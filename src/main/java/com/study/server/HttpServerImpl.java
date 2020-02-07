package com.study.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServerImpl implements HttpServer {
    private final int port;
    private boolean stopped;
    private ServerSocket serverSocket;
    private final ExecutorService executor;
    private final SocketHandlerFactoryImpl shFactory;

    public HttpServerImpl(ServerConfiguration config, SocketHandlerFactoryImpl shf) {
        port = config.getPort();
        int poolSize = config.getPoolSize();
        executor = Executors.newFixedThreadPool(poolSize);
        shFactory = shf;
    }

    @Override
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot open port " + port, e);
        }

        while (!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server stopped");
                    break;
                } else {
                    stop();
                }
                throw new IllegalArgumentException(
                        "Error accepting client connection", e);
            } finally {
                try {
                    if (clientSocket != null) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            executor.execute(shFactory.createSocketHandler(clientSocket));
        }
        executor.shutdown();
        System.out.println("Server stopped");
    }

    private boolean isStopped() {
        return stopped;
    }

    public void stop() {
        stopped = true;
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("Error closing server", e);
        }
    }
}