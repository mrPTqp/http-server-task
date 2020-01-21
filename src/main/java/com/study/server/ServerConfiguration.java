package com.study.server;

public class ServerConfiguration {
    private final int defaultPort = 3333;
    private final int defaultPoolSize = 20;
    private int port;
    private int poolSize;

    public ServerConfiguration() {
        this.port = defaultPort;
        this.poolSize = defaultPoolSize;
    }

    public ServerConfiguration(int port, int poolSize) {
        this.port = port;
        this.poolSize = poolSize;
    }

    public int getPort() {
        return port;
    }

    public int getPoolSize() {
        return poolSize;
    }
}
