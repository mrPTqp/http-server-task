package com.study.server;

import com.study.server.controller.Controller;
import com.study.server.http.HttpRequest;
import com.study.server.http.HttpResponse;
import com.study.server.http.StatusCode;

import java.util.Set;

public class RequestDispatcherImpl implements RequestDispatcher {
    private Set<Controller> controllers;

    public RequestDispatcherImpl(Set<Controller> controllers) {
        this.controllers = controllers;
    }

    public HttpResponse dispatch(HttpRequest request) {
        for (Controller controller : controllers) {
            if (controller.match(request)) {
                return controller.handle(request);
            }
        }

        return new HttpResponse.Builder().setProtocol("HTTP/1.1")
                .setStatusCode(StatusCode._404.toString())
                .build();
    }
}