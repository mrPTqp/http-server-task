package com.study.server;

import java.net.Socket;

public class SocketHandlerFactoryImpl implements SocketHandlerFactory {
    RequestDispatcher requestDispatcher;

    public SocketHandlerFactoryImpl(RequestDispatcher requestDispatcher) {
        this.requestDispatcher = requestDispatcher;
    }

    @Override
    public SocketHandlerImpl createSocketHandler(Socket clientSocket) {
        return new SocketHandlerImpl(clientSocket, requestDispatcher);
    }
}