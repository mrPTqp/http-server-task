package com.study.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServerImpl implements HttpServer {
    private final int port;
    private boolean stop;
    private final ExecutorService executor;
    private final SocketHandlerFactoryImpl shFactory;

    public HttpServerImpl(ServerConfiguration config, SocketHandlerFactoryImpl shf) {
        port = config.getPort();
        int poolSize = config.getPoolSize();
        executor = Executors.newFixedThreadPool(poolSize);
        shFactory = shf;
    }

    @Override
    @SuppressWarnings("PMD.CloseResource")
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (!stop) {
                Socket clientSocket;
                try {
                    clientSocket = serverSocket.accept();
                    executor.execute(shFactory.createSocketHandler(clientSocket));
                } catch (IOException e) {
                    if (stop) {
                        System.out.println("Server stopped");
                        break;
                    } else {
                        stop = true;
                    }
                    throw new IllegalArgumentException("Error accepting client connection", e);
                }
            }

            executor.shutdown();
            System.out.println("Server stopped");
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot open port " + port, e);
        }
    }
}