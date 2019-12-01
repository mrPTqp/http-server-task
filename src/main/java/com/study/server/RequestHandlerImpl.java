package com.study.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RequestHandlerImpl implements RequestHandler, Runnable {
    private Socket client;

    public RequestHandlerImpl(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            RequestParserImpl parser = new RequestParserImpl(in);
            parser.parse(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
