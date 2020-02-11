package com.study.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServerImpl implements HttpServer {
    private final int port;
    private final int poolSize;
    private boolean stop;
    private final ExecutorService executor;
    private final SocketHandlerFactoryImpl shFactory;
    private static final Logger LOGGER = Logger.getLogger(HttpServerImpl.class.getName());

    public HttpServerImpl(ServerConfiguration config, SocketHandlerFactoryImpl shf) {
        port = config.getPort();
        poolSize = config.getPoolSize();
        executor = Executors.newFixedThreadPool(poolSize);
        shFactory = shf;
    }

    @Override
    @SuppressWarnings({"PMD.CloseResource", "PMD.AvoidInstantiatingObjectsInLoops"})
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            LOGGER.log(
                    Level.INFO,
                    "The server is started with the following parameters:\r\n" + "port = " + port + "\r\n"
                            + "poolSize = " + poolSize
            );
            while (!stop) {
                Socket clientSocket;
                try {
                    clientSocket = serverSocket.accept();
                    LOGGER.log(Level.INFO, "New connection established");
                    executor.execute(shFactory.createSocketHandler(clientSocket));
                } catch (IOException e) {
                    if (stop) {
                        LOGGER.log(Level.SEVERE, "Server is already stopped");
                        break;
                    } else {
                        stop = true;
                    }
                    LOGGER.log(Level.WARNING, "Error accepting client connection");
                    throw new IllegalArgumentException("Error accepting client connection");
                }
            }

            executor.shutdown();
            LOGGER.log(Level.SEVERE, "Server is stopped");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Cannot open port " + port);
            throw new IllegalArgumentException("Cannot open port " + port);
        }
    }
}