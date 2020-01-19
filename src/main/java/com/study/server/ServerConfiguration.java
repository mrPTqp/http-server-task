package com.study.server;

public class ServerConfiguration {
    private static int defaultPort = 3333;
    private static int defaultPoolSize = 20;
    private int port = defaultPort;
    private int poolSize = defaultPoolSize;

    public ServerConfiguration() {
        this.port = port;
        this.poolSize = poolSize;
    }

    public int getPort() {
        return port;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public ServerConfiguration setPort(int port) {
        this.port = port;
        return this;
    }

    public ServerConfiguration setPoolSize(int poolSize) {
        this.poolSize = poolSize;
        return this;
    }
}
