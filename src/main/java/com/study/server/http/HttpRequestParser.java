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
        requestElements = requestElements[1].trim().split("\r\n\r\n", 2);
        String[] headersLines = requestElements[0].trim().split("\\r?\\n");
        for (String s : headersLines) {
            String[] headerParts = s.split(":\\s+", 2);
            headers.put(headerParts[0], headerParts[1]);
        }
        request.setHeaders(headers);

        byte[]

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

//GET /path HTTP/1.1
//        Host: localhost:8080
//        cache-control: no-cache
//        Postman-Token: 59703a7a-3a40-4977-aeda-4ca5f335675f

//POST /path HTTP/1.1
//        Host: localhost:8080
//        cache-control: no-cache
//        Postman-Token: 8f2fd50e-ca2f-454e-9ef9-254590e10236
//        Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW
//
//
//        Content-Disposition: form-data; name="login"
//
//        lex
//        ------WebKitFormBoundary7MA4YWxkTrZu0gW--,
//        Content-Disposition: form-data; name="login"
//
//        lex
//        ------WebKitFormBoundary7MA4YWxkTrZu0gW--
//        Content-Disposition: form-data; name="password"
//
//        ***&
//        ------WebKitFormBoundary7MA4YWxkTrZu0gW--

//PUT /new.html HTTP/1.1
//        Host: example.com
//        Content-type: text/html
//        Content-length: 16
//
//<p>Новый файл</p>