package com.study.server;

import com.study.server.utils.HttpPatternsUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;

public class ConfigurationReaderImpl implements ConfigurationReader {
    private final String sourceDir;

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

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(sourceDir, "server-config.properties"))) {
            properties.load(reader);
            port = Integer.parseInt(properties.getProperty("server.port"));
            poolSize = Integer.parseInt(properties.getProperty("server.pool-size"));

            return new ServerConfiguration(port, poolSize);
        } catch (IOException e) {
            return new ServerConfiguration();
        }
    }

    @Override
    public Map<String, String> readMappings() {
        Map<String, String> mappings = new HashMap<>();
        File[] dirs = new File(sourceDir).listFiles(File::isDirectory);

        if (dirs == null) {
            throw new NoSuchElementException("The configuration directory does not contain directories");
        } else {
            for (File dir : dirs) {
                String host;
                var hostMatcher = HttpPatternsUtils.PATH_HOST_PATTERN.matcher(dir.getPath());
                if (hostMatcher.find()) {
                    host = hostMatcher.group("pathHost");
                    mappings.put(host, dir.getPath());
                }
            }
        }

        if (mappings.isEmpty()) {
            throw new NoSuchElementException("The configuration directory does not contain valid site directories");
        }

        return Map.copyOf(mappings);
    }
}
