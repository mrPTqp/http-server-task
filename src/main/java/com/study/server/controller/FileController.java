package com.study.server.controller;

import com.study.server.http.HttpRequest;
import com.study.server.http.HttpResponse;
import com.study.server.http.StatusCode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class FileController implements Controller {
    private final String host;
    private final String path;
    private final ConcurrentHashMap<String, HttpResponse> cache = new ConcurrentHashMap<>();
    private int allReqCounter;
    private int cachedReqCounter;
    private float cacheRate;
    @SuppressWarnings("PMD.FieldNamingConventions")
    private static final Logger log = Logger.getLogger(FileController.class.getName());

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
        ++allReqCounter;
        var requestPath = request.getPath();
        var normalizePath = normalizePath(requestPath);

        if (cache.get(normalizePath) == null) {
            var path = Paths.get(normalizePath);

            if (Files.exists(path)) {
                try {
                    HttpResponse response = new HttpResponse.Builder()
                            .setProtocol("HTTP/1.1")
                            .setStatusCode(StatusCode._200.toString())
                            .setBody(getBodyString(path))
                            .build();
                    cache.put(normalizePath, response);

                    return response;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return new HttpResponse.Builder()
                    .setProtocol("HTTP/1.1")
                    .setStatusCode(StatusCode._404.toString())
                    .build();
        } else {
            ++cachedReqCounter;
            cacheRate = (float) cachedReqCounter / (float) allReqCounter;
            log.info("CacheRate = " + cacheRate);

            return cache.get(normalizePath);
        }
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