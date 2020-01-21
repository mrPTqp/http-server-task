package com.study.server;

import com.study.server.utils.ParsingPatterns;

import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.regex.Pattern;

public class ConfigurationReaderImpl implements ConfigurationReader {
    private static final String MESSAGE = "The configuration directory " +
            "does not contain valid site directories";
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
        Map<String, String> mutableMappings = new HashMap<>();
        File[] dirs = new File(sourceDir).listFiles(File::isDirectory);

        for (File curPath : dirs) {
            String host = getHostFromPath(ParsingPatterns.pathHostPattern, curPath);
            mutableMappings.put(host, curPath.toString());
        }

        if (mutableMappings.isEmpty()) {
            throw new NoSuchElementException(MESSAGE);
        }

        return Collections.unmodifiableMap(mutableMappings);
    }

    private String getHostFromPath(Pattern hostPattern, File curDir) {
        try {
            var hostMatcher = hostPattern.matcher(curDir.toString());
            hostMatcher.find();

            return hostMatcher.group("pathHost");
        } catch (Exception e) {
            throw new NoSuchElementException(MESSAGE);
        }
    }
}
