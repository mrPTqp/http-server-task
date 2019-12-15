package com.study.server.http;

import java.util.HashMap;
import java.util.Map;

public class RequestParserImpl implements RequestParser {
    private String inputLine;
    private static String method = null;
    private static String URL = null;
    private static String path = null;
    private static String httpVersion = null;
    private static String host = null;
    private static Map<String, String> headers = new HashMap<>();
    private static Map<String, String> queryParameters = new HashMap<>();
    private Request request;

    public RequestParserImpl(String inputLine) {
        this.inputLine = inputLine;
    }

    @Override
    public boolean parse(String inputLine) {
        String[] parts = inputLine.split("\n");
        String[] partsFirstLine = parts[0].split(" ");
        String[] partsSecondLine = parts[1].split(": ");

        method = partsFirstLine[0];
        URL = partsFirstLine[1];
        httpVersion = partsFirstLine[2];
        host = partsSecondLine[1];

        if (method != "GET" || URL == null || httpVersion != "HTTP/1.1" || host == null) {
            return false;
        }

        for (int i = 2; i < parts.length; i++) {
            String headerLine = parts[i];
            if (headerLine.length() == 0) {
                break;
            }

            int separator = headerLine.indexOf(":");
            if (!(separator == -1)) {
                headers.put(headerLine.substring(0, separator),
                        headerLine.substring(separator + 1));
            }
        }

        if (!URL.contains("?")) {
            path = URL;
        } else {
            path = URL.substring(0, URL.indexOf("?"));
            parseQueryParameters(URL.substring(URL.indexOf("?") + 1));
        }

        if ("/".equals(path)) {
            path = "/index.html";
        }

        System.out.println(method);
        System.out.println(URL);
        System.out.println(path);
        System.out.println(httpVersion);
        System.out.println(host);
        System.out.println(headers);
        System.out.println(queryParameters);

        request = new Request(method, URL, path, httpVersion, host, headers, queryParameters);

        return true;
    }

    private void parseQueryParameters(String queryString) {
        for (String parameter : queryString.split("&")) {
            int separator = parameter.indexOf('=');
            if (separator > -1) {
                queryParameters.put(parameter.substring(0, separator),
                        parameter.substring(separator + 1));
            } else {
                queryParameters.put(parameter, null);
            }
        }
    }

    public Request getRequest() {
        return request;
    }
}