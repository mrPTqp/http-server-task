package com.study.server.http;

import java.util.HashMap;
import java.util.Map;

public class Response {
    private StatusCode statusCode;
    private Map<String, String> headers = new HashMap<>();
    private String body;

    public Response() {
    }

    public void setResponseCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public void addHeader(String headerName, String headerValue) {
        this.headers.put(headerName, headerValue);
    }

    public void addBody(String body) {
        headers.put("Content-Length", Integer.toString(body.length()));
        this.body = body;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}