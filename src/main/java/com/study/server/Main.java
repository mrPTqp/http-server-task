package com.study.server;

import com.study.server.controller.Controller;
import com.study.server.controller.FileController;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        final Logger logger = Logger.getLogger(Main.class.getName());

        var cr = new ConfigurationReaderImpl();
        var config = cr.readConfig();
        var mappings = cr.readMappings();

        Set<Controller> controllers = mappings.entrySet()
                .stream()
                .map(e -> new FileController(e.getKey(), e.getValue()))
                .collect(Collectors.toSet());
        logger.log(Level.INFO, controllers.size() + " controllers created");

        var requestDispatcher = new RequestDispatcherImpl(controllers);
        var shFactory = new SocketHandlerFactoryImpl(requestDispatcher);

        var server = new HttpServerImpl(config, shFactory);
        server.start();
    }
}
