package com.study.server.controller;

import com.study.server.http.Request;
import com.study.server.http.Response;

interface Controller {
    boolean match(Request request);

    Response handle(Request request);
}