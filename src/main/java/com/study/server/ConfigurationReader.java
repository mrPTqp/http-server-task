package com.study.server;

import java.util.Map;

public interface ConfigurationReader {
    ServerConfiguration readConfig();

    Map<String, String> createMapping();
}
