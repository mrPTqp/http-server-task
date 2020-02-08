package com.study.server.controller;

import com.study.server.http.HttpResponse;
import com.study.server.http.StatusCode;
import com.study.server.utils.HttpRequestGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileControllerTest {

    @Test
    @DisplayName("Should match the request to the controller if the controller and host have the same host")
    void match1() {
        var controller = new FileController("food.com", "123");

        assertTrue(controller.match(HttpRequestGenerator.createGetRequest2()));
    }

    @Test
    @DisplayName("Should not match the request to the controller if the controller and host have different host")
    void match2() {
        var controller = new FileController("food1.com", "123");

        assertFalse(controller.match(HttpRequestGenerator.createGetRequest2()));
    }

    @Test
    @DisplayName("Should return a response matching the request")
    void handle1() {
        var sourceDir = System.getenv().get("CONF_DIR");
        var expectedHost = "food.com";
        var controller = new FileController(expectedHost, sourceDir + File.separator + expectedHost);
        var request = HttpRequestGenerator.createGetRequest2();

        var file = new File(sourceDir
                + File.separator + expectedHost
                + File.separator + request.getPath()
                + "index.html");
        HttpResponse expectedResponse = new HttpResponse.Builder()
                .setProtocol("HTTP/1.1")
                .setStatusCode(StatusCode._200.toString())
                .setBody(getBodyLine(file))
                .build();
        HttpResponse response = controller.handle(request);

        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("Should return a response matching the request")
    void handle2() {
        var sourceDir = System.getenv().get("CONF_DIR");
        var expectedHost = "food.com";
        var controller = new FileController(expectedHost, sourceDir + File.separator + expectedHost);
        var request = HttpRequestGenerator.createGetRequest1();

        var file = new File(sourceDir
                + File.separator + expectedHost
                + File.separator + request.getPath());
        HttpResponse expectedResponse = new HttpResponse.Builder()
                .setProtocol("HTTP/1.1")
                .setStatusCode(StatusCode._200.toString())
                .setBody(getBodyLine(file))
                .build();
        HttpResponse response = controller.handle(request);

        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("Should return a response with code 404 if the requested file was not found")
    void handle3() {
        var sourceDir = System.getenv().get("CONF_DIR");
        var expectedHost = "good.com";
        var controller = new FileController(expectedHost, sourceDir + File.separator + expectedHost);
        var request = HttpRequestGenerator.createGetRequest2();

        HttpResponse expectedResponse = new HttpResponse.Builder()
                .setProtocol("HTTP/1.1")
                .setStatusCode(StatusCode._404.toString())
                .build();

        HttpResponse response = controller.handle(request);

        assertEquals(expectedResponse, response);
    }

    private String getBodyLine(File file) {
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] fis = fileInputStream.readAllBytes();

            for (byte b : fis) {
                sb.append(b).append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}