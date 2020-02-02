package com.study.server;

import com.study.server.http.HttpRequest;
import com.study.server.http.HttpResponse;

public interface RequestDispatcher {
    public HttpResponse dispatch(HttpRequest request);
}
