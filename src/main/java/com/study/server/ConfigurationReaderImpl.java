package com.study.server;

import com.study.server.utils.HttpPatterns;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;

public class ConfigurationReaderImpl implements ConfigurationReader {
    private String sourceDir;

    public ConfigurationReaderImpl() {
        sourceDir = System.getenv().get("CONF_DIR");
        if (sourceDir == null) {
            throw new IllegalArgumentException("Missing environment variable CONF_DIR");
        }
    }

    @Override
    public ServerConfiguration readConfig() {
        int port;
        int poolSize;

        Properties properties = new Properties();
        File file = new File(sourceDir, "server-config.properties");

        try {
            properties.load(new FileReader(file));
            port = Integer.parseInt(properties.getProperty("server.port"));
            poolSize = Integer.parseInt(properties.getProperty("server.pool-size"));

            return new ServerConfiguration(port, poolSize);
        } catch (Exception e) {
            return new ServerConfiguration();
        }
    }

    @Override
    public Map<String, String> readMappings() {
        Map<String, String> mappings = new HashMap<>();
        File[] dirs = new File(sourceDir).listFiles(File::isDirectory);

        for (File dir : dirs) {
            String host;
            var hostMatcher = HttpPatterns.pathHostPattern.matcher(dir.getPath());
            if (hostMatcher.find()) {
                host = hostMatcher.group("pathHost");
                mappings.put(host, dir.toString());
            }
        }

        if (mappings.isEmpty()) {
            throw new NoSuchElementException("The configuration directory does not contain valid site directories");
        }

        return Map.copyOf(mappings);
    }
}
