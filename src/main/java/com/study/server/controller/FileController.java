package com.study.server.controller;

import com.study.server.http.HttpRequest;
import com.study.server.http.HttpResponse;
import com.study.server.http.StatusCode;

import java.util.Map;

public class FileController implements Controller {
    String host;
    String path;

    public FileController(String host, String path) {
        this.host = host;
        this.path = path;
    }

    @Override
    public boolean match(HttpRequest request) {
        String requestHost = request.getHost();

        return host.equals(requestHost);
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        return new HttpResponse.Builder().setProtocol("HTTP/1.1")
                .setStatusCode(StatusCode._404.toString())
                .setHeaders(Map.of())
                .setBody("body")
                .build();
    }
}