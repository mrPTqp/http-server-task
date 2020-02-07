package com.study.server.utils;

import com.study.server.controller.Controller;
import com.study.server.http.HttpRequest;
import com.study.server.http.HttpResponse;
import com.study.server.http.StatusCode;

public class ControllerMock implements Controller {

    @Override
    public boolean match(HttpRequest request) {
        return !request.getMethod().equals(" ");
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        return new HttpResponse.Builder().setProtocol("HTTP/1.1")
                .setStatusCode(StatusCode._200.toString())
                .build();
    }
}