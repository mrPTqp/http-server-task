package com.study.server.http;

import java.util.Map;

public class Request {
    private String method = null;
    private String path = null;
    private Map<String, String> queryParameters = null;
    private String protocol = null;
    private String host = null;
    private Map<String, String> headers = null;
    private String body = null;

    public Request() {
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public String getHost() {
        return host;
    }

    public Map<String, String> getQueryParameters() {
        return queryParameters;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setQueryParameters(Map<String, String> queryParameters) {
        this.queryParameters = queryParameters;
    }
}