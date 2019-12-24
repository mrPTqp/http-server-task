package com.study.server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {
    private static int defaultPort = 3333;
    private static int defaultNumberOfThreads = 20;

    public static void main(String[] args) throws IOException {
        Map<String, Integer> parameters;
        String sourceDir = System.getenv().get("SitesAndConfigDirectory");
        parameters = readProperties(sourceDir);

        if (parameters != null) {
            Integer port = parameters.get("port");
            Integer numberOfThreads = parameters.get("numberOfThreads");

            HttpServerImpl server = new HttpServerImpl(port, numberOfThreads);
            server.start();
        } else {
            int port = defaultPort;
            int numberOfThreads = defaultNumberOfThreads;

            HttpServerImpl server = new HttpServerImpl(port, numberOfThreads);
            server.start();
        }
    }

    private static HashMap<String, Integer> readProperties(String sourceConfig) throws IOException {
        HashMap<String, Integer> parameters = new HashMap<>();
        Properties properties = new Properties();
        File file = new File(sourceConfig, "server-config.properties");

        if (file.exists()) {
            properties.load(new FileReader(file));
            Integer port = Integer.parseInt(properties.getProperty("port"));
            parameters.put("port", port);
            Integer numberOfThreads = Integer.parseInt(properties.getProperty("numberOfThreads"));
            parameters.put("numberOfThreads", numberOfThreads);
            return parameters;
        } else {
            return null;
        }
    }
}
