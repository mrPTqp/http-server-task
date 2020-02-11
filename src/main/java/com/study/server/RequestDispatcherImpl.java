package com.study.server;

import com.study.server.controller.Controller;
import com.study.server.http.HttpRequest;
import com.study.server.http.HttpResponse;
import com.study.server.http.StatusCode;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestDispatcherImpl implements RequestDispatcher {
    private final Set<Controller> controllers;
    private static final Logger LOGGER = Logger.getLogger(RequestDispatcherImpl.class.getName());

    public RequestDispatcherImpl(Set<Controller> controllers) {
        if (controllers == null || controllers.isEmpty()) {
            LOGGER.log(Level.SEVERE, "The set of controllers is empty");
            throw new NoSuchElementException("The set of controllers is empty");
        }

        this.controllers = controllers;
    }

    @Override
    public HttpResponse dispatch(HttpRequest request) {
        for (Controller controller : controllers) {
            if (controller.match(request)) {
                return controller.handle(request);
            }
        }

        LOGGER.log(
                Level.WARNING,
                "There is no controller for this request. A response with code 404 will be sent"
        );

        return new HttpResponse.Builder().setProtocol("HTTP/1.1")
                .setStatusCode(StatusCode._404.toString())
                .build();
    }
}