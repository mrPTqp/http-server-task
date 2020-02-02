package com.study.server;

import com.study.server.controller.Controller;
import com.study.server.controller.FileController;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        ConfigurationReaderImpl cr = new ConfigurationReaderImpl();
        ServerConfiguration config = cr.readConfig();
        Map<String, String> mappings = cr.readMappings();
        Set<Controller> controllers = getControllers(mappings);

        RequestDispatcher requestDispatcher = new RequestDispatcherImpl(controllers);
        SocketHandlerFactoryImpl socketHandlerFactory = new SocketHandlerFactoryImpl(requestDispatcher);

        HttpServerImpl server = new HttpServerImpl(config, socketHandlerFactory);
        server.start();
    }

    private static Set<Controller> getControllers(Map<String, String> mappings) {
        return mappings.entrySet()
                .stream()
                .map(e -> new FileController(e.getKey(), e.getValue()))
                .collect(Collectors.toSet());
    }
}
