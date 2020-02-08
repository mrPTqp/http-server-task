package com.study.server.http;

import com.study.server.exceptions.BadRequestException;
import com.study.server.utils.HttpRequestGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.IOException;

import static com.study.server.utils.TestUtils.readFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HttpRequestParserTest {

    @Test
    @DisplayName("Should parse GET request case-insensitive headers")
    void parseGet() {
        var expectedRequest = HttpRequestGenerator.createGetRequest1();

        var is = readFile("GET");
        var bis = new BufferedInputStream(is);
        var request = HttpRequestParser.parse(bis);

        assertEquals(expectedRequest, request);
    }

    @Test
    @DisplayName("Should parse PUT request without path")
    void parsePut() {
        var expectedRequest = HttpRequestGenerator.createPutRequest();

        var is = readFile("PUT");
        var bis = new BufferedInputStream(is);
        var request = HttpRequestParser.parse(bis);

        assertEquals(expectedRequest, request);
    }

    @Test
    @DisplayName("Should parse POST request with Host header only")
    void parsePost() {
        var expectedRequest = HttpRequestGenerator.createPostRequest();

        var is = readFile("POST");
        var bis = new BufferedInputStream(is);
        var request = HttpRequestParser.parse(bis);

        assertEquals(expectedRequest, request);
    }

    @Test
    @DisplayName("Should DELETE request in which header Host is not in the first place")
    void parseDelete() {
        var expectedRequest = HttpRequestGenerator.createDeleteRequest();

        var is = readFile("DELETE");
        var bis = new BufferedInputStream(is);
        var request = HttpRequestParser.parse(bis);

        assertEquals(expectedRequest, request);
    }

    @Test
    @DisplayName("Should throw BadRequestException, if the method is absent")
    void badRequestExceptionTest1() throws IOException {
        var is = readFile("bad1");
        var bis = new BufferedInputStream(is);

        assertThrows(BadRequestException.class, () -> HttpRequestParser.parse(bis));
        bis.close();
    }

    @Test
    @DisplayName("Should throw BadRequestException, if the method is incorrect or not supported")
    void badRequestExceptionTest2() throws IOException {
        var is = readFile("bad2");
        var bis = new BufferedInputStream(is);

        assertThrows(BadRequestException.class, () -> HttpRequestParser.parse(bis));
        bis.close();
    }

    @Test
    @DisplayName("Should throw BadRequestException, if the version of HTTP protocol difference by HTTP/1.1")
    void badRequestExceptionTest3() throws IOException {
        var is = readFile("bad3");
        var bis = new BufferedInputStream(is);

        assertThrows(BadRequestException.class, () -> HttpRequestParser.parse(bis));
        bis.close();
    }

    @Test
    @DisplayName("Should throw BadRequestException, if the path contain non-ASCII symbols")
    void badRequestExceptionTest4() throws IOException {
        var is = readFile("bad4");
        var bis = new BufferedInputStream(is);

        assertThrows(BadRequestException.class, () -> HttpRequestParser.parse(bis));
        bis.close();
    }

    @Test
    @DisplayName("Should throw BadRequestException, if the headers contain non-ASCII symbols")
    void badRequestExceptionTest5() throws IOException {
        var is = readFile("bad5");
        var bis = new BufferedInputStream(is);

        assertThrows(BadRequestException.class, () -> HttpRequestParser.parse(bis));
        bis.close();
    }

    @Test
    @DisplayName("Should throw BadRequestException, if the Host header absent")
    void badRequestExceptionTest6() throws IOException {
        var is = readFile("bad6");
        var bis = new BufferedInputStream(is);

        assertThrows(BadRequestException.class, () -> HttpRequestParser.parse(bis));
        bis.close();
    }
}