package com.study.server;

import com.study.server.controller.ControllerImpl;
import com.study.server.http.HttpRequest;
import com.study.server.http.Response;

import java.util.HashMap;
import java.util.List;

public class RequestDispatcherImpl implements RequestDispatcher {
    private HttpRequest request;
    private String sitesAndConfigDirectory = System.getenv().get("SitesAndConfigDirectory");
    private HashMap<HttpRequest, ControllerImpl> controllers = new HashMap<>();
    private List<String> indexHtmlFilesPaths;

    public RequestDispatcherImpl(HttpRequest request) {
        this.request = request;
    }

    Response dispatch() {
        Response response;

        if (controllers.isEmpty()) {
            indexHtmlFilesPaths = findHtmlInDir();
            ControllerImpl controller = new ControllerImpl(indexHtmlFilesPaths);
            if (controller.match(request)) {
                response = controller.handle(request);
                controllers.put(request, controller);
                System.out.println(controllers);
                return response;
            } else {
                return null;
            }
        }

        if (controllers.containsValue(request)) {
            ControllerImpl controller = controllers.get(request);
            response = controller.handle(request);
            return response;
        } else {
            indexHtmlFilesPaths = findHtmlInDir();
            ControllerImpl controller = new ControllerImpl(indexHtmlFilesPaths);
            if (controller.match(request)) {
                response = controller.handle(request);
                controllers.put(request, controller);
                System.out.println(controllers);
                return response;
            } else {
                return null;
            }
        }
    }

    private List<String> findHtmlInDir() {
        HtmlFinderImpl finder = new HtmlFinderImpl(sitesAndConfigDirectory);
        return finder.findHtmlInDir();
    }
}