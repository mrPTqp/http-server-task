package com.study.server.controller;

import com.study.server.http.HttpResponse;
import com.study.server.http.StatusCode;
import com.study.server.utils.HttpRequestGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileControllerTest {

    @Test
    @DisplayName("Controller should match request for configured host")
    void match1() {
        var controller = new FileController("food.com", "123");

        assertTrue(controller.match(HttpRequestGenerator.createGetRequest2()));
    }

    @Test
    @DisplayName("The controller should not match request for unrecognized host")
    void match2() {
        var controller = new FileController("food1.com", "123");

        assertFalse(controller.match(HttpRequestGenerator.createGetRequest2()));
    }

    @Test
    @DisplayName("Should return a response matching the request with root path")
    void handle1() throws URISyntaxException, IOException {
        var host = "food.com";
        var hostUri = FileController.class.getClassLoader().getResource(host).toURI();
        var hostPath = Paths.get(hostUri).toString();
        var controller = new FileController(host, hostPath);
        var request = HttpRequestGenerator.createGetRequest2();

        HttpResponse expectedResponse = new HttpResponse.Builder()
                .setProtocol("HTTP/1.1")
                .setStatusCode(StatusCode._200.toString())
                .setBody(getBodyString(Path.of(hostPath + "/index.html")))
                .build();
        HttpResponse response = controller.handle(request);

        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("Should return a response matching the request with \"/css/foo.css\" path")
    void handle2() throws URISyntaxException, IOException {
        var host = "food.com";
        var hostUri = FileController.class.getClassLoader().getResource(host).toURI();
        var hostPath = Paths.get(hostUri).toString();
        var controller = new FileController(host, hostPath);
        var request = HttpRequestGenerator.createGetRequest1();

        HttpResponse expectedResponse = new HttpResponse.Builder()
                .setProtocol("HTTP/1.1")
                .setStatusCode(StatusCode._200.toString())
                .setBody(getBodyString(Path.of(hostPath + "/css/foo.css")))
                .build();
        HttpResponse response = controller.handle(request);

        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("Should return a response with code 404 if the requested file was not found")
    void handle3() throws URISyntaxException {
        var tmpUri = FileController.class.getClassLoader().getResource("food.com").toURI();
        var hostPath = Paths.get(tmpUri).toString().replace("food", "good");
        var controller = new FileController("good.com", hostPath);
        var request = HttpRequestGenerator.createGetRequest2();

        HttpResponse expectedResponse = new HttpResponse.Builder()
                .setProtocol("HTTP/1.1")
                .setStatusCode(StatusCode._404.toString())
                .build();

        HttpResponse response = controller.handle(request);

        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("Should return the same answer upon repeated call")
    void handle4() throws URISyntaxException {
        var host = "food.com";
        var hostUri = FileController.class.getClassLoader().getResource(host).toURI();
        var hostPath = Paths.get(hostUri).toString();
        var controller = new FileController(host, hostPath);
        var request = HttpRequestGenerator.createGetRequest2();

        HttpResponse response1 = controller.handle(request);
        HttpResponse response2 = controller.handle(request);

        assertEquals(response1.toString(), response2.toString());
    }

    private String getBodyString(Path path) throws IOException {
        var fis = Files.readAllLines(path);
        var sb = new StringBuilder();

        for (String b : fis) {
            sb.append(b);
        }
        return sb.toString();
    }
}