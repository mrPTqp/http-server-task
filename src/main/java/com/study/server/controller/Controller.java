package com.study.server.controller;

import com.study.server.http.HttpRequest;
import com.study.server.http.HttpResponse;

interface Controller {
    boolean match(HttpRequest request);

    HttpResponse handle(HttpRequest request);
}