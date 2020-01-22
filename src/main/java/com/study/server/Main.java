package com.study.server;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ConfigurationReaderImpl cr = new ConfigurationReaderImpl();
        ServerConfiguration config = cr.readConfig();
        Map<String, String> mappings = cr.readMappings();

        HttpServerImpl server = new HttpServerImpl(config);
        server.start();
    }
}
