package com.study.server;

import com.study.server.http.Request;
import com.study.server.http.RequestParserImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketHandlerImpl implements SocketHandler, Runnable {
    private Socket client;

    public SocketHandlerImpl(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        String inputLine = readLine();
        RequestParserImpl parser = new RequestParserImpl(inputLine);
        System.out.println(inputLine);
        Request request = parser.parse(inputLine);
        System.out.println(request);
    }

    private String readLine() {
        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            final StringBuilder content = new StringBuilder();
            while (!(inputLine = in.readLine()).equals("")) {
                content.append(inputLine).append("\n");
            }
            return content.toString();
        } catch (final Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
