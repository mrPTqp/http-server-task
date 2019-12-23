package com.study.server.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpRequestParserTest {

    @Test
    public void testGetParser() {
    }

    @Test
    @DisplayName("Should parse parse InputStream with a GET request and return the correct Request object")
    public void parseGET() {
        Request expectedRequest = new Request();

        expectedRequest.setMethod("GET");
        expectedRequest.setPath("/css/style.css");

        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", "dima");
        parameters.put("age", "27");
        expectedRequest.setQueryParameters(parameters);

        expectedRequest.setProtocol("HTTP/1.1");
        expectedRequest.setHost("www.food.com");

        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "www.food.com");
        headers.put("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, */*");
        headers.put("Accept-Language", "ru");
        expectedRequest.setHeaders(headers);

        String get1 = "GET" + " " + "/css/style.css" + "?" + "name=dima" + "&" + "age=27" + " " + "HTTP/1.1" + "\r\n" +
                "Host:" + " " + "www.food.com" + "\r\n" +
                "Accept:" + " " + "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, */*" + "\r\n" +
                "Accept-Language:" + " " + "ru" + "\r\n\r\n";

        byte[] data1 = get1.getBytes();
        InputStream in1 = new ByteArrayInputStream(data1);

        HttpRequestParser parser = HttpRequestParser.getParser();
        Request request = parser.parse(in1);

        assertEquals(expectedRequest.getMethod(), request.getMethod());
        assertEquals(expectedRequest.getPath(), request.getPath());
        assertEquals(expectedRequest.getQueryParameters(), request.getQueryParameters());
        assertEquals(expectedRequest.getProtocol(), request.getProtocol());
        assertEquals(expectedRequest.getHeaders(), request.getHeaders());
        assertEquals(expectedRequest.getHost(), request.getHost());
        assertEquals(expectedRequest.getBody(), request.getBody());

        expectedRequest.setPath("/index.html");
        String get2 = "GET" + " " + "/" + "?" + "name=dima" + "&" + "age=27" + " " + "HTTP/1.1" + "\r\n" +
                "Host:" + " " + "www.food.com" + "\r\n" +
                "Accept:" + " " + "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, */*" + "\r\n" +
                "Accept-Language:" + " " + "ru" + "\r\n\r\n";

        byte[] data2 = get2.getBytes();
        InputStream in2 = new ByteArrayInputStream(data2);

        request = parser.parse(in2);

        assertEquals(expectedRequest.getMethod(), request.getMethod());
        assertEquals(expectedRequest.getPath(), request.getPath());
        assertEquals(expectedRequest.getQueryParameters(), request.getQueryParameters());
        assertEquals(expectedRequest.getProtocol(), request.getProtocol());
        assertEquals(expectedRequest.getHeaders(), request.getHeaders());
        assertEquals(expectedRequest.getHost(), request.getHost());
        assertEquals(expectedRequest.getBody(), request.getBody());

        expectedRequest.setPath("/index.html");
        String get3 = "GET" + "/" + "?" + "name=dima" + "&" + "age=27" + " " + "HTTP/1.1" + "\r\n" +
                "Host:" + " " + "www.food.com" + "\r\n" +
                "Accept:" + " " + "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, */*" + "\r\n" +
                "Accept-Language:" + " " + "ru" + "\r\n\r\n";

        byte[] data3 = get3.getBytes();
        InputStream in3 = new ByteArrayInputStream(data3);

        request = parser.parse(in3);

        assertEquals(expectedRequest.getMethod(), request.getMethod());
        assertEquals(expectedRequest.getPath(), request.getPath());
        assertEquals(expectedRequest.getQueryParameters(), request.getQueryParameters());
        assertEquals(expectedRequest.getProtocol(), request.getProtocol());
        assertEquals(expectedRequest.getHeaders(), request.getHeaders());
        assertEquals(expectedRequest.getHost(), request.getHost());
        assertEquals(expectedRequest.getBody(), request.getBody());
    }

    @Test
    @DisplayName("Should parse parse InputStream with a PUT request and return the correct Request object")
    public void parsePUT() {
        Request expectedRequest = new Request();

        expectedRequest.setMethod("PUT");
        expectedRequest.setPath("/css/style.css");
        expectedRequest.setProtocol("HTTP/1.1");
        expectedRequest.setHost("www.food.com");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer mt0dgHmLJMVQhvjpNXDyA83vA_PxH23Y");
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("Content-Length", "85");
        headers.put("Host", "www.food.com");
        expectedRequest.setHeaders(headers);

        String body = "{" + "\r\n" +
                "\"Id\": 12345," + "\r\n" +
                "\"Customer\": \"John Smith\"," + "\r\n" +
                "\"Quantity\": 1," + "\r\n" +
                "\"Price\": 10.00" + "\r\n" +
                "}";
        expectedRequest.setBody(body);

        String put = "PUT" + " " + "/css/style.css" + " " + "HTTP/1.1" + "\r\n" +
                "Authorization:" + " " + "Bearer mt0dgHmLJMVQhvjpNXDyA83vA_PxH23Y" + "\r\n" +
                "Accept:" + " " + "application/json" + "\r\n" +
                "Content-Type:" + " " + "application/json" + "\r\n" +
                "Content-Length:" + " " + "85" + "\r\n" +
                "Host:" + " " + "www.food.com" + "\r\n\r\n" +
                "{" + "\r\n" +
                "\"Id\": 12345," + "\r\n" +
                "\"Customer\": \"John Smith\"," + "\r\n" +
                "\"Quantity\": 1," + "\r\n" +
                "\"Price\": 10.00" + "\r\n" +
                "}";

        byte[] data = put.getBytes();
        InputStream in = new ByteArrayInputStream(data);

        HttpRequestParser parser = HttpRequestParser.getParser();
        Request request = parser.parse(in);

        assertEquals(expectedRequest.getMethod(), request.getMethod());
        assertEquals(expectedRequest.getPath(), request.getPath());
        assertEquals(expectedRequest.getQueryParameters(), request.getQueryParameters());
        assertEquals(expectedRequest.getProtocol(), request.getProtocol());
        assertEquals(expectedRequest.getHost(), request.getHost());
        assertEquals(expectedRequest.getHeaders(), request.getHeaders());
        assertEquals(expectedRequest.getBody(), request.getBody());
    }

    @Test
    @DisplayName("Should parse parse InputStream with a POST request and return the correct Request object")
    public void parsePOST() {
        Request expectedRequest = new Request();

        expectedRequest.setMethod("POST");
        expectedRequest.setPath("/index.html");
        expectedRequest.setProtocol("HTTP/1.1");
        expectedRequest.setHost("localhost:8080");

        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "localhost:8080");
        headers.put("Cache-Control", "no-cache");
        headers.put("Postman-Token", "8f2fd50e-ca2f-454e-9ef9-254590e10236");
        headers.put("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
        expectedRequest.setHeaders(headers);

        String body = "\r\n" +
                "Content-Disposition: form-data; name=\"login\"" + "\r\n" +
                "\r\n" +
                "lex" + "\r\n" +
                "------WebKitFormBoundary7MA4YWxkTrZu0gW--," + "\r\n" +
                "Content-Disposition: form-data; name=\"login\"" + "\r\n" +
                "\r\n" +
                "lex" +
                "------WebKitFormBoundary7MA4YWxkTrZu0gW--" + "\r\n" +
                "Content-Disposition: form-data; name=\"password\"" + "\r\n" +
                "\r\n" +
                "***&" + "\r\n" +
                "------WebKitFormBoundary7MA4YWxkTrZu0gW--";
        expectedRequest.setBody(body);

        String post = "POST" + " " + "/" + " " + "HTTP/1.1" + "\r\n" +
                "Host:" + " " + "localhost:8080" + "\r\n" +
                "Cache-Control:" + " " + "no-cache" + "\r\n" +
                "Postman-Token:" + " " + "8f2fd50e-ca2f-454e-9ef9-254590e10236" + "\r\n" +
                "Content-Type:" + " " + "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW" + "\r\n\r\n" +
                "\r\n" +
                "Content-Disposition: form-data; name=\"login\"" + "\r\n" +
                "\r\n" +
                "lex" + "\r\n" +
                "------WebKitFormBoundary7MA4YWxkTrZu0gW--," + "\r\n" +
                "Content-Disposition: form-data; name=\"login\"" + "\r\n" +
                "\r\n" +
                "lex" +
                "------WebKitFormBoundary7MA4YWxkTrZu0gW--" + "\r\n" +
                "Content-Disposition: form-data; name=\"password\"" + "\r\n" +
                "\r\n" +
                "***&" + "\r\n" +
                "------WebKitFormBoundary7MA4YWxkTrZu0gW--";

        byte[] data = post.getBytes();
        InputStream in = new ByteArrayInputStream(data);

        HttpRequestParser parser = HttpRequestParser.getParser();
        Request request = parser.parse(in);

        assertEquals(expectedRequest.getMethod(), request.getMethod());
        assertEquals(expectedRequest.getPath(), request.getPath());
        assertEquals(expectedRequest.getQueryParameters(), request.getQueryParameters());
        assertEquals(expectedRequest.getProtocol(), request.getProtocol());
        assertEquals(expectedRequest.getHost(), request.getHost());
        assertEquals(expectedRequest.getHeaders(), request.getHeaders());
        assertEquals(expectedRequest.getBody(), request.getBody());
    }
}