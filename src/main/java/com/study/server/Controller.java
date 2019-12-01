package com.study.server;

import java.util.Map;

public interface Controller {
    void makeOperation(String method, String path, Map<String, String> headers, Map<String, String> queryParameters);
}
