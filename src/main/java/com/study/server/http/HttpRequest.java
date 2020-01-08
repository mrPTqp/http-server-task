package com.study.server.http;

import java.util.Map;
import java.util.Objects;

public class HttpRequest {
    private final String method;
    private final String path;
    private final Map<String, String> queryParameters;
    private final String protocol;
    private final String host;
    private final String port;
    private final Map<String, String> headers;
    private final String body;

    private HttpRequest(Builder builder) {
        method = builder.method;
        path = builder.path;
        queryParameters = builder.queryParameters;
        protocol = builder.protocol;
        host = builder.host;
        port = builder.port;
        headers = builder.headers;
        body = builder.body;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQueryParameters() {
        return Map.copyOf(queryParameters);
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public Map<String, String> getHeaders() {
        return Map.copyOf(headers);
    }

    public String getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequest that = (HttpRequest) o;
        return Objects.equals(method, that.method) &&
                Objects.equals(path, that.path) &&
                Objects.equals(queryParameters, that.queryParameters) &&
                Objects.equals(protocol, that.protocol) &&
                Objects.equals(host, that.host) &&
                Objects.equals(port, that.port) &&
                Objects.equals(headers, that.headers) &&
                Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, path, queryParameters, protocol, host, port, headers, body);
    }

    public static class Builder {
        private String method;
        private String path;
        private String host;
        private String protocol;
        private Map<String, String> queryParameters;
        private Map<String, String> headers;
        private String port;
        private String body;

        public Builder() {
        }

        public Builder setMethod(String method) {
            this.method = method;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public Builder setProtocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder setQueryParameters(Map<String, String> queryParameters) {
            this.queryParameters = queryParameters;
            return this;
        }

        public Builder setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder setPort(String port) {
            this.port = port;
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }
}