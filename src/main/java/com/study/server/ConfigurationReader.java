package com.study.server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigurationReader {
    private String[] launchParameters;
    private String sourceDir;
    private int port;
    private int poolSize;
    private static int defaultPort = 3333;
    private static int defaultPoolSize = 20;

    public ConfigurationReader(String[] launchParameters) {
        this.launchParameters = launchParameters;
    }

    public int getPort() {
        return this.port;
    }

    public int getPoolSize() {
        return this.poolSize;
    }

    public void readConfig() {
        if (launchParameters != null) {
            sourceDir = launchParameters[0];
        } else {
            sourceDir = System.getenv().get("CONF_DIR");
        }

        Properties properties = new Properties();
        File file = new File(sourceDir, "server-config.properties");

        if (file.exists()) {
            try {
                properties.load(new FileReader(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            port = Integer.parseInt(properties.getProperty("server.port"));
            poolSize = Integer.parseInt(properties.getProperty("server.pool-size"));
        } else {
            port = defaultPort;
            poolSize = defaultPoolSize;
        }
    }

    public void createMapping() {
        List<String> sitesPaths;
        Path dir = Paths.get(sourceDir);

        try (Stream<Path> walk = Files.walk(dir)) {
            sitesPaths = walk.map(Path::toString)
                    .filter(f -> f.contains("index.html"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
