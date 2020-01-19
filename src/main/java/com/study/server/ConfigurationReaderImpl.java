package com.study.server;

import com.study.server.exceptions.BadConfigException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

public class ConfigurationReaderImpl implements ConfigurationReader {
    private String sourceDir;

    public ConfigurationReaderImpl() {
        try {
            sourceDir = System.getenv().get("CONF_DIR");
        } catch (Exception e) {
            throw new BadConfigException("Missing environment variable CONF_DIR");
        }
    }

    @Override
    public ServerConfiguration readConfig() {
        ServerConfiguration config = new ServerConfiguration();
        try {
            int port;
            int poolSize;

            Properties properties = new Properties();
            File file = new File(sourceDir, "server-config.properties");

            properties.load(new FileReader(file));
            port = Integer.parseInt(properties.getProperty("server.port"));
            config.setPort(port);
            poolSize = Integer.parseInt(properties.getProperty("server.pool-size"));
            config.setPoolSize(poolSize);

            return config;
        } catch (IOException e) {
            return config;
        }
    }

    @Override
    public Map<String, String> createMapping() {
        Map<String, String> mapping = new HashMap<>();
        Pattern jsonHostPattern = Pattern.compile(
                "(^\\{\"siteName\":\")(?<jsonHost>[\\x41-\\x5A[\\x61-\\x7A[\\x30-\\x39[\\x2D-\\x2E]]]]+\\." +
                        "[\\x41-\\x5A[\\x61-\\x7A[\\x30-\\x39[\\x2D-\\x2E]]]]+)(\"\\})"
        );
        Pattern pathHostPattern = Pattern.compile(
                "(.+\\\\)(?<pathHost>[\\x41-\\x5A[\\x61-\\x7A[\\x30-\\x39[\\x2D-\\x2E]]]]+\\." +
                        "[\\x41-\\x5A[\\x61-\\x7A[\\x30-\\x39[\\x2D-\\x2E]]]]+)"
        );
        File[] dirs = new File(sourceDir).listFiles(File::isDirectory);

        if (dirs.length > 0) {
            for (File curPath : dirs) {
                File jsonFile = new File(curPath.toString() + "\\site-name.json");
                String host;

                if (jsonFile.exists()) {
                    host = getHostFromJson(jsonHostPattern, jsonFile);
                } else {
                    host = getHostFromPath(pathHostPattern, curPath);
                }

                mapping.put(host, curPath.toString());
            }
        } else {
            throw new BadConfigException("The configuration directory does not contain valid site directories");
        }

        if (mapping.isEmpty()) {
            throw new BadConfigException("The configuration directory does not contain valid site directories");
        }

        return mapping;
    }

    private String getHostFromJson(Pattern jsonHostPattern, File jsonFile) {
        try {
            var fis = new FileInputStream(jsonFile);
            var bis = new BufferedInputStream(fis);
            var br = new BufferedReader(new InputStreamReader(bis));
            var curLine = br.readLine();

            var jsonHostMatcher = jsonHostPattern.matcher(curLine);
            jsonHostMatcher.find();

            return jsonHostMatcher.group("jsonHost");
        } catch (Exception e) {
            throw new BadConfigException("Invalid site-name.json content");
        }
    }

    private String getHostFromPath(Pattern pathHostPattern, File curDir) {
        try {
            var pathHostMatcher = pathHostPattern.matcher(curDir.toString());
            pathHostMatcher.find();

            return pathHostMatcher.group("pathHost");
        } catch (Exception e) {
            throw new BadConfigException("The configuration directory " +
                    "does not contain valid site directories");
        }
    }
}
