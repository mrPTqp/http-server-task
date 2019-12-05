package com.study.server.http;

import java.util.HashMap;
import java.util.Map;

public class RequestParserImpl implements RequestParser {
    private String inputLine;
    private static String method = null;
    private static String URL = null;
    private static String path = null;
    private static String HttpVersion = null;
    private static Map<String, String> headers = new HashMap<>();
    private static Map<String, String> queryParameters = new HashMap<>();

    public RequestParserImpl(String inputLine) {
        this.inputLine = inputLine;
    }

    @Override
    public Request parse(String inputLine) {
        String[] parts = inputLine.split("\n");
        String[] partsFirstLine = inputLine.split(" ");

        method = partsFirstLine[0];
        URL = partsFirstLine[1];
        HttpVersion = partsFirstLine[2];

        for (int i = 1; i < parts.length; i++) {
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
        System.out.println(HttpVersion);
        System.out.println(headers);
        System.out.println(queryParameters);

        Request request = new Request(method, URL, path, HttpVersion, headers, queryParameters);

        return request;
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
}