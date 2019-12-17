package com.study.server.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpRequestParser implements RequestParser {
    private static HttpRequestParser parser = new HttpRequestParser();

    private HttpRequestParser() {

    }

    public static HttpRequestParser getParser() {
        return parser;
    }

    @Override
    public Optional<Request> parse(InputStream in) {
        Request request = new Request();
        String requestLine = convertRequestToString(in);
        String[] requestElements = requestLine.trim().split("\\s+", 2);

        request.setMethod(requestElements[0]);

        requestElements = requestElements[1].trim().split("\\s+", 2);

        if (requestElements[0].endsWith("/")) {
            request.setPath(requestElements[0] + "/index.html");
        } else {
            request.setPath(requestElements[0]);
        }

        requestElements = requestElements[1].trim().split("\\s+", 2);
        request.setProtocol(requestElements[0]);

        Map<String, String> headers = new HashMap<>();
        String[] headersLines = requestElements[1].trim().split("\\r?\\n");
        for (String s : headersLines) {
            String[] headerParts = s.split(":\\s+", 2);
            headers.put(headerParts[0], headerParts[1]);
        }
        request.setHeaders(headers);

        return Optional.empty();
    }

    private String convertRequestToString(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String inputLine;

        try {
            while ((inputLine = br.readLine()) != null && !inputLine.trim().isEmpty()) {
                sb.append(inputLine);
                sb.append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}