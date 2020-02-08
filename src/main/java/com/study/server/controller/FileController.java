package com.study.server.controller;

import com.study.server.http.HttpRequest;
import com.study.server.http.HttpResponse;
import com.study.server.http.StatusCode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileController implements Controller {
    private final String host;
    private final String path;

    public FileController(String host, String path) {
        this.host = host;
        this.path = path;
    }

    @Override
    public boolean match(HttpRequest request) {
        var requestHost = request.getHost();
        return host.equals(requestHost);
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        var requestPath = request.getPath();
        var normalizePath = normalizePath(requestPath);
        var path = Paths.get(normalizePath);

        if (Files.exists(path)) {
            try {
                String bodyLine = getBodyString(path);

                return new HttpResponse.Builder()
                        .setProtocol("HTTP/1.1")
                        .setStatusCode(StatusCode._200.toString())
                        .setBody(bodyLine)
                        .build();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new HttpResponse.Builder()
                .setProtocol("HTTP/1.1")
                .setStatusCode(StatusCode._404.toString())
                .build();
    }

    private String getBodyString(Path path) throws IOException {
        var fis = Files.readAllBytes(path);
        var sb = new StringBuilder();

        for (byte b : fis) {
            sb.append(b).append("\r\n");
        }
        return sb.toString();
    }

    private String normalizePath(String rp) {
        var requestPath = rp;
        if (requestPath == null) {
            requestPath = path + "index.html";
        } else if (requestPath.endsWith("/")) {
            requestPath = path + requestPath + "index.html";
        } else {
            requestPath = path + requestPath;
        }

        if ("/".equals(File.separator)) {
            return requestPath.replace("\\", "/");
        } else {
            return requestPath.replace("/", "\\");
        }
    }
}