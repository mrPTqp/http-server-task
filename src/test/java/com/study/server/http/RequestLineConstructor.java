package com.study.server.http;

import java.util.HashMap;
import java.util.Map;

class RequestLineConstructor {
    private String method;
    private String path;
    private Map<String, String> queryParameters = new HashMap<>();
    private String protocol;
    private String host;
    private Map<String, String> headers = new HashMap<>();
    private String body;
    private String requestLine;

    RequestLineConstructor(String method, String path, Map<String, String> queryParameters, String protocol,
                           String host, Map<String, String> headers, String body) {
        this.method = method;
        this.path = path;
        this.queryParameters = queryParameters;
        this.protocol = protocol;
        this.host = host;
        this.headers = headers;
        this.body = body;
    }

    public String getRequestLine() {
        StringBuilder sb = new StringBuilder();
        sb.append(method)
                .append(" ")
                .append(path);

        if (!queryParameters.isEmpty()) {
            sb.append("?");
            for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
                sb.append(entry.getKey()).
                        append("=").
                        append(entry.getValue());
            }
        }

        sb.append(" ").
                append(protocol).
                append("\r\n");

        if (!headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                sb.append(entry.getKey()).
                        append(": ").
                        append(entry.getValue()).
                        append("\r\n");
            }
            sb.append("\r\n");
        } else {
            sb.append("\r\n\r\n");
        }

        sb.append(body);

        return sb.toString();
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setQueryParameters(Map<String, String> queryParameters) {
        this.queryParameters = queryParameters;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setRequestLine(String requestLine) {
        this.requestLine = requestLine;
    }
}