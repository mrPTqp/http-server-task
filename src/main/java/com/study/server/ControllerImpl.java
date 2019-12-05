package com.study.server;

import com.study.server.http.Request;
import com.study.server.http.Response;

public class ControllerImpl implements Controller {

    @Override
    public boolean match(Request req) {
        return false;
    }

    @Override
    public Response handle(Request req) {
        return null;
    }
}
