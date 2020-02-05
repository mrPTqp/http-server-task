package com.study.server.utils;

import com.study.server.RequestDispatcher;
import com.study.server.http.HttpRequest;
import com.study.server.http.HttpResponse;
import com.study.server.http.StatusCode;

import java.util.HashMap;
import java.util.Map;

public class RequestDispatcherMock implements RequestDispatcher {
    private final Map<HttpRequest, Integer> counts = new HashMap<>();

    public RequestDispatcherMock() {
    }

    @Override
    public HttpResponse dispatch(HttpRequest request) {
        if (counts.get(request) == null) {
            counts.put(request, 1);
        } else {
            var i = counts.get(request);
            ++i;
            counts.put(request, i);
        }

        final Map<String, String> headers = Map.of(
                "Server", "Apache"
        );

        final String body = "HTML\nit's cool";
        HttpResponse.Builder builder = new HttpResponse.Builder();

        return builder.setProtocol("HTTP/1.1")
                .setStatusCode(StatusCode._200.toString())
                .setHeaders(headers)
                .setBody(body)
                .build();
    }

    public int verifyRequest(HttpRequest request) {
        return counts.get(request);
    }

    public void clearMock() {
        counts.clear();
    }
}
