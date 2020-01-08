package com.study.server.http;

import com.study.server.exceptions.BadRequestException;
import com.study.server.utils.HttpRequestGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.InputStream;

import static com.study.server.utils.TestUtils.readFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HttpRequestParserTest {

    @Test
    @DisplayName("Should parse GET request case-insensitive headers")
    void parseGET() {
        HttpRequest expectedRequest = HttpRequestGenerator.createGetRequest();

        try {
            InputStream is = readFile("GET");
            BufferedInputStream bis = new BufferedInputStream(is);
            HttpRequest request = HttpRequestParser.parse(bis);

            assertEquals(expectedRequest, request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Should parse PUT request without path")
    void parsePUT() {
        HttpRequest expectedRequest = HttpRequestGenerator.createPutRequest();

        try {
            InputStream is = readFile("PUT");
            BufferedInputStream bis = new BufferedInputStream(is);
            HttpRequest request = HttpRequestParser.parse(bis);

            assertEquals(expectedRequest, request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Should parse InputStream with a POST request and return the correct Request object")
    void parsePOST() {
        HttpRequest expectedRequest = HttpRequestGenerator.createPostRequest();

        try {
            InputStream is = readFile("POST");
            BufferedInputStream bis = new BufferedInputStream(is);
            HttpRequest request = HttpRequestParser.parse(bis);

            assertEquals(expectedRequest, request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Should parse InputStream with a DELETE request and return the correct Request object")
    void parseDELETE() {
        HttpRequest expectedRequest = HttpRequestGenerator.createDeleteRequest();

        try {
            InputStream is = readFile("DELETE");
            BufferedInputStream bis = new BufferedInputStream(is);
            HttpRequest request = HttpRequestParser.parse(bis);

            assertEquals(expectedRequest, request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Should throw an exception BadRequestException, if the request is incorrect")
    void BadRequestExceptionTest() {

        try {
            InputStream is = readFile("bad");
            BufferedInputStream bis = new BufferedInputStream(is);

            Throwable exception1 = assertThrows(BadRequestException.class, () -> HttpRequestParser.parse(bis));
            assertEquals("Can't parse request", exception1.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}