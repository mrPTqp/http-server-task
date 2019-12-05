package com.study.server.http;

import java.util.Map;

public class Request {
    private String method = null;
    private String URL = null;
    private String path = null;
    private String HttpVersion = null;
    private Map<String, String> headers;
    private Map<String, String> queryParameters;

    public Request(String method, String URL, String path, String HttpVersion,
                   Map<String, String> headers, Map<String, String> queryParameters) {
        this.method = method;
        this.URL = URL;
        this.path = path;
        this.HttpVersion = HttpVersion;
        this.headers = headers;
        this.queryParameters = queryParameters;
    }
}
