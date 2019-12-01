package com.study.server;

import java.util.HashMap;
import java.util.Map;

public class ControllerImpl implements Controller {
    private String method;
    private String path;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> queryParameters = new HashMap<>();

    public ControllerImpl(String method, String path, Map<String, String> headers, Map<String, String> queryParameters) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.queryParameters = queryParameters;
    }

    @Override
    public void makeOperation(String method, String path, Map<String, String> headers,
                              Map<String, String> queryParameters) {

    }
}
