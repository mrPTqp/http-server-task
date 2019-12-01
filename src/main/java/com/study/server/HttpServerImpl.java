package com.study.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HttpServerImpl implements HttpServer {
    private int port;
    private int coreNumberOfThreads;
    private int maxNumberOfThreads;
    private int keepAliveTime;
    private ExecutorService executor;

    public HttpServerImpl(int port, int coreNumberOfThreads, int maxNumberOfThreads, int keepAliveTime) {
        this.port = port;
        this.coreNumberOfThreads = coreNumberOfThreads;
        this.maxNumberOfThreads = maxNumberOfThreads;
        this.keepAliveTime = keepAliveTime;
        this.executor = new ThreadPoolExecutor(coreNumberOfThreads, maxNumberOfThreads,
                keepAliveTime, TimeUnit.SECONDS, new SynchronousQueue<>());
    }

    @Override
    public void start() throws IOException {
        int port = this.port;
        ServerSocket socket = new ServerSocket(port);
        Socket client;
        while ((client = socket.accept()) != null) {
            System.out.println("Received connection from " + client.getRemoteSocketAddress().toString());
            executor.execute(new HttpRequestParserImpl(client));
        }
        executor.shutdown();
    }
}