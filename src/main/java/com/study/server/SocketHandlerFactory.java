package com.study.server;

import java.net.Socket;

public interface SocketHandlerFactory {
    SocketHandlerImpl createSocketHandler(Socket clientSocket);
}