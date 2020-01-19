package com.study.server;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ConfigurationReaderImpl cr = new ConfigurationReaderImpl();
        ServerConfiguration config = cr.readConfig();
        Map<String, String> mapping = cr.createMapping();

        HttpServerImpl server = new HttpServerImpl(config.getPort(), config.getPoolSize());
        server.start();
    }
}
