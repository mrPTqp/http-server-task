package com.study.server.http;

import com.study.server.exceptions.BadRequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser implements RequestParser {
    private static HttpRequestParser parser = new HttpRequestParser();

    private HttpRequestParser() {
    }

    public static HttpRequestParser getParser() {
        return parser;
    }

    @Override
    public Request parse(InputStream in) {
        Request request = new Request();
        String requestLine = convertRequestToString(in);
        try {
            String[] requestElements = requestLine.trim().split("\\s+", 2);

            request.setMethod(requestElements[0]);

            requestElements = requestElements[1].trim().split("\\s+", 2);

            String path;
            Map<String, String> queryParameters = new HashMap<>();
//как парсить URI без слеша в конце пути?
//а парсинг порта?
            if (requestElements[0].contains("?")) {
                String[] partsOfURI = requestElements[0].trim().split("\\?", 2);
                path = partsOfURI[0].trim();

                String parametersLine = partsOfURI[1].trim();
                for (String parameter : parametersLine.split("&")) {
                    int separator = parameter.indexOf('=');
                    if (separator > -1) {
                        queryParameters.put(parameter.substring(0, separator),
                                parameter.substring(separator + 1));
                    }
                }
                request.setQueryParameters(queryParameters);
            } else {
                path = requestElements[0];
            }

            if (path.endsWith("/")) {
                path = path + "index.html";
            }
            request.setPath(path);

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

            String host = headers.get("Host");
            request.setHost(host);

            if (requestElements.length > 1) {
                String body = requestElements[1];
                request.setBody(body);
            }

            return request;
        } catch (Exception e) {
            throw new BadRequestException("Can't parse request");
        }
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

// Примеры запросов:

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

//PUT /sample/put/json HTTP/1.1
//        Authorization: Bearer mt0dgHmLJMVQhvjpNXDyA83vA_PxH23Y
//        Accept: application/json
//        Content-Type: application/json
//        Content-Length: 85
//        Host: reqbin.com
//
//        {
//        "Id": 12345,
//        "Customer": "John Smith",
//        "Quantity": 1,
//        "Price": 10.00
//        }