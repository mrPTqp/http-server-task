package com.study.server.controller;

import com.study.server.http.HttpRequest;
import com.study.server.http.Response;

interface Controller {
    boolean match(HttpRequest request);

    Response handle(HttpRequest request);
}