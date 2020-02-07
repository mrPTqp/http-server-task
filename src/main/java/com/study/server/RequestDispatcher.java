package com.study.server;

import com.study.server.http.HttpRequest;
import com.study.server.http.HttpResponse;

public interface RequestDispatcher {
    HttpResponse dispatch(HttpRequest request);
}