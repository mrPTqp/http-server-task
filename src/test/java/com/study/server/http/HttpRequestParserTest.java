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
        expectedRequest.setPath("/food.com/index.html");

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

        String get = "GET" + " " + "/food.com/" + "?" + "name=dima" + "&" + "age=27" + " " + "HTTP/1.1" + "\r\n" +
                "Host:" + " " + "www.food.com" + "\r\n" +
                "Accept:" + " " + "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, */*" + "\r\n" +
                "Accept-Language:" + " " + "ru" + "\r\n\r\n";

        byte[] data = get.getBytes();
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
    @DisplayName("Should parse parse InputStream with a PUT request and return the correct Request object")
    public void parsePUT() {
        Request expectedRequest = new Request();

        expectedRequest.setMethod("PUT");
        expectedRequest.setPath("/food.com/index.html");
        expectedRequest.setProtocol("HTTP/1.1");

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer mt0dgHmLJMVQhvjpNXDyA83vA_PxH23Y");
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("Content-Length", "85");
        headers.put("Host", "reqbin.com");
        expectedRequest.setHeaders(headers);

        String body = "{" + "\r\n" +
                "\"Id\": 12345," + "\r\n" +
                "\"Customer\": \"John Smith\"," + "\r\n" +
                "\"Quantity\": 1," + "\r\n" +
                "\"Price\": 10.00" + "\r\n" +
                "}";
        expectedRequest.setBody(body);

        String put = "PUT" + " " + "/sample/put/json" + " " + "HTTP/1.1" + "\r\n" +
                "Authorization:" + " " + "Bearer mt0dgHmLJMVQhvjpNXDyA83vA_PxH23Y" + "\r\n" +
                "Accept:" + " " + "application/json" + "\r\n" +
                "Content-Type:" + " " + "application/json" + "\r\n" +
                "Content-Length:" + " " + "85" + "\r\n" +
                "Host:" + " " + "reqbin.com" + "\r\n\r\n" +
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

}