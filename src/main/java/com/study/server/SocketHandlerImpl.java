package com.study.server;

import com.study.server.exceptions.BadRequestException;
import com.study.server.http.HttpRequestParser;
import com.study.server.http.StatusCode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class SocketHandlerImpl implements SocketHandler, Runnable {
    private final InputStream in;
    private final OutputStream out;
    private final RequestDispatcher requestDispatcher;
    private final Socket clientSocket;
    private static final Logger LOGGER = Logger.getLogger(SocketHandlerImpl.class.getName());

    public SocketHandlerImpl(Socket clientSocket, RequestDispatcher requestDispatcher) {
        this.clientSocket = clientSocket;
        this.requestDispatcher = requestDispatcher;
        try {
            in = clientSocket.getInputStream();
            out = clientSocket.getOutputStream();
        } catch (IOException e) {
            LOGGER.warning("Can't read clientSocket");
            throw new IllegalArgumentException("Can't read clientSocket");
        }
    }

    @Override
    public void run() {
        try {
            out.write(requestDispatcher.dispatch(HttpRequestParser.parse(in)).toBytes());
        } catch (BadRequestException e) {
            try {
                respond(StatusCode._400.toString(), out);
            } catch (IOException ex) {
                LOGGER.warning(ex.toString() + " Can't write response with code 404 in clientSocket");
            }
        } catch (IOException e) {
            LOGGER.warning(e.toString() + " Can't write response in clientSocket");
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                LOGGER.warning(e.toString() + " Can't close clientSocket");
            }
        }
    }

    private void respond(String statusCode, OutputStream out) throws IOException {
        String responseLine = "HTTP/1.1 " + statusCode + "\n\n";
        out.write(responseLine.getBytes(StandardCharsets.UTF_8));
    }
}
