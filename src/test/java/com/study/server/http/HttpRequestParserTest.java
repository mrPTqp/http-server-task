package com.study.server.http;

import com.study.server.exceptions.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HttpRequestParserTest {

    @Test
    @DisplayName("Should parse InputStream with a GET request and return the correct Request object")
    void parseGET() {
        Request expectedRequest = new Request();
        String method = "GET";
        String path = "/css/style.css";
        Map<String, String> parameters = new HashMap<>();
        String protocol = "HTTP/1.1";
        String host = "food.com";
        Map<String, String> headers1 = new HashMap<>();
        Map<String, String> headers2 = new HashMap<>();
        String body = "";

        expectedRequest.setMethod(method);
        expectedRequest.setPath(path);

        parameters.put("name", "dima");
        parameters.put("age", "27");
        expectedRequest.setQueryParameters(parameters);

        expectedRequest.setProtocol(protocol);
        expectedRequest.setHost(host);

        headers1.put("Host", "FOOD.com");
        headers1.put("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, */*");
        headers1.put("Accept-Language", "ru");

        headers2.put("Host".toLowerCase(), "FOOD.com".toLowerCase());
        headers2.put("Accept".toLowerCase(), "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, */*".toLowerCase());
        headers2.put("Accept-Language".toLowerCase(), "ru".toLowerCase());
        expectedRequest.setHeaders(headers2);

        RequestLineConstructor rlc = new RequestLineConstructor(method, path, parameters,
                protocol, host, headers1, body);
        String get1 = rlc.getRequestLine();

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
        expectedRequest.getHeaders().put("host", "food.com");
        rlc.setPath("/");
        Map<String, String> mock = new HashMap<>();
        rlc.setQueryParameters(mock);
        String get2 = rlc.getRequestLine();

        byte[] data2 = get2.getBytes();
        InputStream in2 = new ByteArrayInputStream(data2);

        request = parser.parse(in2);

        assertEquals(expectedRequest.getPath(), request.getPath());
        assertEquals(expectedRequest.getHeaders(), request.getHeaders());
        assertEquals(expectedRequest.getHost(), request.getHost());

        expectedRequest.setPath("/index.html");
        expectedRequest.getHeaders().put("host", "food.com");
        rlc.setPath("");
        String get3 = rlc.getRequestLine();

        byte[] data3 = get3.getBytes();
        InputStream in3 = new ByteArrayInputStream(data3);

        request = parser.parse(in3);

        assertEquals(expectedRequest.getPath(), request.getPath());
        assertEquals(expectedRequest.getHeaders(), request.getHeaders());
        assertEquals(expectedRequest.getHost(), request.getHost());

        rlc.setBody("111111111111111111" + "\r\n" + "222222222222222222");
        String get4 = rlc.getRequestLine();

        byte[] data4 = get4.getBytes();
        InputStream in4 = new ByteArrayInputStream(data4);

        request = parser.parse(in4);

        assertEquals(expectedRequest.getBody(), request.getBody());
    }

    @Test
    @DisplayName("Should parse InputStream with a PUT request and return the correct Request object")
    void parsePUT() {
        Request expectedRequest = new Request();
        String method = "PUT";
        String path = "/css/style.css";
        Map<String, String> parameters = new HashMap<>();
        String protocol = "HTTP/1.1";
        String host = "food.com";
        Map<String, String> headers1 = new HashMap<>();
        Map<String, String> headers2 = new HashMap<>();
        String body = "";

        expectedRequest.setMethod(method);
        expectedRequest.setPath(path);
        expectedRequest.setProtocol(protocol);
        expectedRequest.setHost(host);

        headers1.put("Authorization", "Bearer mt0dgHmLJMVQhvjpNXDyA83vA_PxH23Y");
        headers1.put("Accept", "application/json");
        headers1.put("Content-Type", "application/json");
        headers1.put("Content-Length", "85");
        headers1.put("Host", "food.com");

        headers2.put("Authorization".toLowerCase(), "Bearer mt0dgHmLJMVQhvjpNXDyA83vA_PxH23Y".toLowerCase());
        headers2.put("Accept".toLowerCase(), "application/json".toLowerCase());
        headers2.put("Content-Type".toLowerCase(), "application/json".toLowerCase());
        headers2.put("Content-Length".toLowerCase(), "85".toLowerCase());
        headers2.put("Host".toLowerCase(), "food.com".toLowerCase());
        expectedRequest.setHeaders(headers2);

        body = "{" + "\r\n" +
                "\"Id\": 12345," + "\r\n" +
                "\"Customer\": \"John Smith\"," + "\r\n" +
                "\"Quantity\": 1," + "\r\n" +
                "\"Price\": 10.00" + "\r\n" +
                "}";
        expectedRequest.setBody(body);

        RequestLineConstructor rlc = new RequestLineConstructor(method, path, parameters,
                protocol, host, headers1, body);
        String put = rlc.getRequestLine();

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
    @DisplayName("Should parse InputStream with a POST request and return the correct Request object")
    void parsePOST() {
        Request expectedRequest = new Request();
        String method = "POST";
        String path = "/index.html";
        Map<String, String> parameters = new HashMap<>();
        String protocol = "HTTP/1.1";
        String host = "localhost";
        Map<String, String> headers1 = new HashMap<>();
        Map<String, String> headers2 = new HashMap<>();
        String body = "";

        expectedRequest.setMethod(method);
        expectedRequest.setPath(path);
        expectedRequest.setProtocol(protocol);
        expectedRequest.setHost(host);

        headers1.put("Host", "localhost:8080");
        headers1.put("Cache-Control", "no-cache");
        headers1.put("Postman-Token", "8f2fd50e-ca2f-454e-9ef9-254590e10236");
        headers1.put("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");

        headers2.put("Host".toLowerCase(), "localhost:8080".toLowerCase());
        headers2.put("Cache-Control".toLowerCase(), "no-cache".toLowerCase());
        headers2.put("Postman-Token".toLowerCase(), "8f2fd50e-ca2f-454e-9ef9-254590e10236".toLowerCase());
        headers2.put("Content-Type".toLowerCase(), "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW".toLowerCase());
        expectedRequest.setHeaders(headers2);

        body = "\r\n" +
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

        RequestLineConstructor rlc = new RequestLineConstructor(method, path, parameters,
                protocol, host, headers1, body);
        String post = rlc.getRequestLine();

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

    @Test
    @DisplayName("Should parse InputStream with a DELETE request and return the correct Request object")
    void parseDELETE() {
        Request expectedRequest = new Request();
        String method = "DELETE";
        String path = "/index.html";
        Map<String, String> parameters = new HashMap<>();
        String protocol = "HTTP/1.1";
        String host = "localhost";
        Map<String, String> headers1 = new HashMap<>();
        Map<String, String> headers2 = new HashMap<>();
        String body = "";

        expectedRequest.setMethod(method);
        expectedRequest.setPath(path);
        expectedRequest.setProtocol(protocol);
        expectedRequest.setHost(host);

        headers1.put("Host", "localhost:8080");
        headers1.put("Accept", "application/json");
        headers1.put("Content-Type", "application/json");
        headers1.put("Content-Length", "19");

        headers2.put("Host".toLowerCase(), "localhost:8080".toLowerCase());
        headers2.put("Accept".toLowerCase(), "application/json".toLowerCase());
        headers2.put("Content-Type".toLowerCase(), "application/json".toLowerCase());
        headers2.put("Content-Length".toLowerCase(), "19".toLowerCase());
        expectedRequest.setHeaders(headers2);

        RequestLineConstructor rlc = new RequestLineConstructor(method, path, parameters,
                protocol, host, headers1, body);
        String delete = rlc.getRequestLine();

        byte[] data = delete.getBytes();
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
    @DisplayName("Should throw an exception BadRequestException, if the request is incorrect")
    void BadRequestExceptionTest() {
        String method = "";
        String path = "/index.html";
        Map<String, String> parameters = new HashMap<>();
        String protocol = "HTTP/1.1";
        String host = "localhost";
        Map<String, String> headers = new HashMap<>();
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

        RequestLineConstructor rlc = new RequestLineConstructor(method, path, parameters,
                protocol, host, headers, body);
        String post = rlc.getRequestLine();

        byte[] data1 = post.getBytes();
        InputStream in1 = new ByteArrayInputStream(data1);

        HttpRequestParser parser = HttpRequestParser.getParser();

        Throwable exception1 = assertThrows(BadRequestException.class, () -> parser.parse(in1));
        assertEquals("Can't parse request", exception1.getMessage());

        rlc.setMethod("POST");
        rlc.setPath("/hello");
        post = rlc.getRequestLine();

        byte[] data2 = post.getBytes();
        InputStream in2 = new ByteArrayInputStream(data2);

        Throwable exception2 = assertThrows(BadRequestException.class, () -> parser.parse(in2));
        assertEquals("Can't parse request", exception2.getMessage());
    }
}