package com.study.server;

public class Main {
    public static void main(String[] args) {
        ConfigurationReader cr = new ConfigurationReader(args);
        cr.readConfig();
        cr.createMapping();

        HttpServerImpl server = new HttpServerImpl(cr.getPort(), cr.getPoolSize());
        server.start();
    }
}
