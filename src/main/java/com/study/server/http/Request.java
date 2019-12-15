package com.study.server.http;

import java.util.Map;

public class Request {
    private String method = null;
    private String URL = null;
    private String path = null;
    private String HttpVersion = null;
    private String host = null;
    private Map<String, String> headers;
    private Map<String, String> queryParameters;

    public String getMethod() {
        return method;
    }

    public String getURL() {
        return URL;
    }

    public String getPath() {
        return path;
    }

    public String getHttpVersion() {
        return HttpVersion;
    }

    public String getHost() {
        return host;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getQueryParameters() {
        return queryParameters;
    }

    public Request(String method, String URL, String path, String HttpVersion, String host,
                   Map<String, String> headers, Map<String, String> queryParameters) {
        this.method = method;
        this.URL = URL;
        this.path = path;
        this.HttpVersion = HttpVersion;
        this.host = host;
        this.headers = headers;
        this.queryParameters = queryParameters;
    }
}