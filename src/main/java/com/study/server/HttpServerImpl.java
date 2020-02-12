package com.study.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
            LOGGER.info("The server is started with the following parameters: port = " + port
                    + "poolSize = " + poolSize);

            while (!stop) {
                Socket clientSocket;
                try {
                    clientSocket = serverSocket.accept();
                    LOGGER.info("New connection established");
                    executor.execute(shFactory.createSocketHandler(clientSocket));
                } catch (IOException e) {
                    if (stop) {
                        LOGGER.severe("Server is already stopped");
                        break;
                    } else {
                        stop = true;
                    }
                    LOGGER.warning("Error accepting client connection");
                    throw new IllegalArgumentException("Error accepting client connection");
                }
            }

            executor.shutdown();
            LOGGER.severe("Server is stopped");
        } catch (IOException e) {
            LOGGER.severe("Cannot open port " + port);
            throw new IllegalArgumentException("Cannot open port " + port);
        }
    }
}