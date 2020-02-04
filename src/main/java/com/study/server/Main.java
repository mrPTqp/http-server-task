package com.study.server;

import com.study.server.controller.Controller;
import com.study.server.controller.FileController;

import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        var cr = new ConfigurationReaderImpl();
        var config = cr.readConfig();
        var mappings = cr.readMappings();
        Set<Controller> controllers = mappings.entrySet()
                .stream()
                .map(e -> new FileController(e.getKey(), e.getValue()))
                .collect(Collectors.toSet());

        var requestDispatcher = new RequestDispatcherImpl(controllers);
        var socketHandlerFactory = new SocketHandlerFactoryImpl(requestDispatcher);

        var server = new HttpServerImpl(config, socketHandlerFactory);
        server.start();
    }
}
