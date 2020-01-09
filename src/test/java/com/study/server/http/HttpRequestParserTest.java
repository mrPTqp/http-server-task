package com.study.server.http;

import com.study.server.exceptions.BadRequestException;
import com.study.server.utils.HttpRequestGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;

import static com.study.server.utils.TestUtils.readFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HttpRequestParserTest {

    @Test
    @DisplayName("Should parse GET request case-insensitive headers")
    void parseGET() {
        var expectedRequest = HttpRequestGenerator.createGetRequest();

        try {
            var is = readFile("GET");
            var bis = new BufferedInputStream(is);
            var request = HttpRequestParser.parse(bis);

            assertEquals(expectedRequest, request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Should parse PUT request without path")
    void parsePUT() {
        var expectedRequest = HttpRequestGenerator.createPutRequest();

        try {
            var is = readFile("PUT");
            var bis = new BufferedInputStream(is);
            var request = HttpRequestParser.parse(bis);

            assertEquals(expectedRequest, request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Should parse POST request with Host header only")
    void parsePOST() {
        var expectedRequest = HttpRequestGenerator.createPostRequest();

        try {
            var is = readFile("POST");
            var bis = new BufferedInputStream(is);
            var request = HttpRequestParser.parse(bis);

            assertEquals(expectedRequest, request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Should DELETE request in which header Host is not in the first place")
    void parseDELETE() {
        var expectedRequest = HttpRequestGenerator.createDeleteRequest();

        try {
            var is = readFile("DELETE");
            var bis = new BufferedInputStream(is);
            var request = HttpRequestParser.parse(bis);

            assertEquals(expectedRequest, request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Should throw BadRequestException, if the method is absent")
    void BadRequestExceptionTest1() {

        try {
            var is = readFile("bad1");
            var bis = new BufferedInputStream(is);

            Throwable exception = assertThrows(BadRequestException.class, () -> HttpRequestParser.parse(bis));
            assertEquals("Can't parse request", exception.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Should throw BadRequestException, if the method is incorrect or not supported")
    void BadRequestExceptionTest2() {

        try {
            var is = readFile("bad2");
            var bis = new BufferedInputStream(is);

            Throwable exception = assertThrows(BadRequestException.class, () -> HttpRequestParser.parse(bis));
            assertEquals("Can't parse request", exception.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Should throw BadRequestException, if the version of HTTP protocol difference by HTTP/1.1")
    void BadRequestExceptionTest3() {

        try {
            var is = readFile("bad3");
            var bis = new BufferedInputStream(is);

            Throwable exception = assertThrows(BadRequestException.class, () -> HttpRequestParser.parse(bis));
            assertEquals("Can't parse request", exception.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Should throw BadRequestException, if the path contain non-ASCII symbols")
    void BadRequestExceptionTest4() {

        try {
            var is = readFile("bad4");
            var bis = new BufferedInputStream(is);

            Throwable exception = assertThrows(BadRequestException.class, () -> HttpRequestParser.parse(bis));
            assertEquals("Can't parse request", exception.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Should throw BadRequestException, if the headers contain non-ASCII symbols")
    void BadRequestExceptionTest5() {

        try {
            var is = readFile("bad5");
            var bis = new BufferedInputStream(is);

            Throwable exception = assertThrows(BadRequestException.class, () -> HttpRequestParser.parse(bis));
            assertEquals("Can't parse request", exception.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Should throw BadRequestException, if the Host header absent")
    void BadRequestExceptionTest6() {

        try {
            var is = readFile("bad6");
            var bis = new BufferedInputStream(is);

            Throwable exception = assertThrows(BadRequestException.class, () -> HttpRequestParser.parse(bis));
            assertEquals("Can't parse request", exception.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}