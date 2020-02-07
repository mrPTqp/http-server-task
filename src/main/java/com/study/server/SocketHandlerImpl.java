package com.study.server;

import com.study.server.exceptions.BadRequestException;
import com.study.server.http.HttpRequestParser;
import com.study.server.http.StatusCode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketHandlerImpl implements SocketHandler, Runnable {
    private final InputStream in;
    private final OutputStream out;
    private final RequestDispatcher requestDispatcher;

    public SocketHandlerImpl(Socket clientSocket, RequestDispatcher requestDispatcher) {
        this.requestDispatcher = requestDispatcher;
        try {
            in = clientSocket.getInputStream();
            out = clientSocket.getOutputStream();
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't read clientSocket");
        }
    }

    @Override
    public void run() {
        try {
            out.write(requestDispatcher.dispatch(HttpRequestParser.parse(in)).toBytes());

            out.close();
            in.close();
        } catch (BadRequestException e) {
            try {
                respond(StatusCode._400.toString(), out);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void respond(String statusCode, OutputStream out) throws IOException {
        String responseLine = "HTTP/1.1 " + statusCode + "\r\n\r\n";
        out.write(responseLine.getBytes(StandardCharsets.UTF_8));
    }
}
