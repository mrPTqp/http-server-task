package com.study.server.controller;

import com.study.server.RequestDispatcherImpl;
import com.study.server.http.HttpRequest;
import com.study.server.http.HttpResponse;
import com.study.server.http.StatusCode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class FileController implements Controller {
    private final String host;
    private final String path;
    @SuppressWarnings("PMD.FieldNamingConventions")
    private static final Logger log = Logger.getLogger(RequestDispatcherImpl.class.getName());

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
                return new HttpResponse.Builder()
                        .setProtocol("HTTP/1.1")
                        .setStatusCode(StatusCode._200.toString())
                        .setBody(getBodyString(path))
                        .build();
            } catch (IOException e) {
                log.severe(e.toString() + " Response not created");
            }
        }

        HttpResponse response = new HttpResponse.Builder()
                .setProtocol("HTTP/1.1")
                .setStatusCode(StatusCode._404.toString())
                .build();
        log.info("Response with code 404 created");

        return response;
    }

    private String getBodyString(Path path) throws IOException {
        var fis = Files.readAllLines(path);
        var sb = new StringBuilder();

        for (String b : fis) {
            sb.append(b);
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