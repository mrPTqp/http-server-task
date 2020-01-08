package com.study.server.controller;

import com.study.server.http.HttpRequest;
import com.study.server.http.Response;
import com.study.server.http.StatusCode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ControllerImpl implements Controller {
    List<String> indexHtmlFilesPaths;
    String truePath;

    public ControllerImpl(List<String> indexHtmlFilesPaths) {
        this.indexHtmlFilesPaths = indexHtmlFilesPaths;
    }

    @Override
    public boolean match(HttpRequest request) {
        String host = request.getHost();

        for (String path : indexHtmlFilesPaths) {
            if (path.contains(host)) {
                truePath = path;
                return true;
            }
        }

        return false;
    }

    @Override
    public Response handle(HttpRequest request) {
        Response response = new Response();
        StringBuffer buf = new StringBuffer();

        try {
            FileInputStream file = new FileInputStream(truePath);

            int c;
            while ((c = file.read()) != -1) {
                buf.append((char) c);
            }

            response.setResponseCode(StatusCode._200);
            response.addHeader("Content-Type", "text/html");
            response.addBody(buf.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}