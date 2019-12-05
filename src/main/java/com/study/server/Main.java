package com.study.server;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException {
        Map<String, Integer> parametersOfHttpServer;
        String sourceConfig = System.getenv().get("SitesAndConfigDirectory");
        parametersOfHttpServer = readProperties(sourceConfig);

        if(parametersOfHttpServer != null) {
            Integer port = parametersOfHttpServer.get("port");
            Integer numberOfThreads = parametersOfHttpServer.get("numberOfThreads");

            HttpServerImpl server = new HttpServerImpl(port, numberOfThreads);
            System.out.println("Server is started");
            server.start();
        }
    }

    private static HashMap<String, Integer> readProperties(String sourceConfig) throws IOException {
        HashMap<String, Integer> parametersOfHttpServer = new HashMap<>();
        Properties properties = new Properties();
        File file = new File(sourceConfig, "server-config.properties");

        if (file.exists()) {
            properties.load(new FileReader(file));
            Integer port = Integer.parseInt(properties.getProperty("port"));
            parametersOfHttpServer.put("port", port);
            Integer numberOfThreads = Integer.parseInt(properties.getProperty("numberOfThreads"));
            parametersOfHttpServer.put("numberOfThreads", numberOfThreads);
            return parametersOfHttpServer;
        } else {
            return null;
        }
    }
}
