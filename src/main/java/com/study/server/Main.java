package com.study.server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {
    private static int defaultPort = 3333;
    private static int defaultPoolSize = 20;

    public static void main(String[] args) throws IOException {
        Map<String, Integer> parameters;
        String sourceDir = System.getenv().get("CONF_DIR");
        parameters = readProperties(sourceDir);

        if (parameters != null) {
            Integer port = parameters.get("server.port");
            Integer poolSize = parameters.get("server.pool-size");

            HttpServerImpl server = new HttpServerImpl(port, poolSize);
            server.start();
        } else {
            int port = defaultPort;
            int poolSize = defaultPoolSize;

            HttpServerImpl server = new HttpServerImpl(port, poolSize);
            server.start();
        }
    }

    private static HashMap<String, Integer> readProperties(String sourceConfig) throws IOException {
        HashMap<String, Integer> parameters = new HashMap<>();
        Properties properties = new Properties();
        File file = new File(sourceConfig, "server-config.properties");

        if (file.exists()) {
            properties.load(new FileReader(file));
            Integer port = Integer.parseInt(properties.getProperty("server.port"));
            parameters.put("port", port);
            Integer poolSize = Integer.parseInt(properties.getProperty("server.pool-size"));
            parameters.put("poolSize", poolSize);
            return parameters;
        } else {
            return null;
        }
    }
}
