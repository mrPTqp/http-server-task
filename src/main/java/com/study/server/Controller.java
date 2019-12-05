package com.study.server;

import com.study.server.http.Request;
import com.study.server.http.Response;

interface Controller {
    boolean match(Request req);

    Response handle(Request req);
}
