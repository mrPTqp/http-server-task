package com.study.server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServerImpl server = new HttpServerImpl(8080, 2, 8, 60);
        server.start();
        System.out.println("Server is started");
    }
}
