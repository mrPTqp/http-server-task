package com.study.server.http;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class HttpResponse {
    private final String protocol;
    private final String statusCode;
    private final Map<String, String> headers;
    private final String body;

    public HttpResponse(String protocol, String statusCode, Map<String, String> headers, String body) {
        this.protocol = protocol;
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return Map.copyOf(headers);
    }

    public String getBody() {
        return body;
    }

    public byte[] toBytes() {
        StringBuilder sb = new StringBuilder();
        sb.append(protocol).append(" ").append(statusCode).append("\r\n");
        headers.forEach((k, v) -> sb.append(k).append(": ").append(v).append("\r\n"));
        sb.append("\r\n");
        sb.append(body);
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HttpResponse response = (HttpResponse) o;

        return Objects.equals(protocol, response.protocol)
                && Objects.equals(statusCode, response.statusCode)
                && Objects.equals(headers, response.headers)
                && Objects.equals(body, response.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(protocol, statusCode, headers, body);
    }

    public static class Builder {
        private String protocol = "HTTP/1.1";
        private String statusCode = StatusCode._400.toString();
        private Map<String, String> headers = Collections.emptyMap();
        private String body = "";

        public Builder() {
        }

        public Builder setProtocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder setStatusCode(String statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(protocol, statusCode, headers, body);
        }
    }
}