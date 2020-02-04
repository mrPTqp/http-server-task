package com.study.server;

import com.study.server.http.HttpResponse;
import com.study.server.http.StatusCode;
import com.study.server.utils.ControllerMock;
import com.study.server.utils.HttpRequestGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestDispatcherImplTest {

    @Test
    @DisplayName("Should return response corresponding request")
    void dispatchGood() {
        RequestDispatcher requestDispatcher = new RequestDispatcherImpl(Set.of(new ControllerMock()));
        HttpResponse expectedResponse = new HttpResponse.Builder()
                .setProtocol("HTTP/1.1")
                .setStatusCode(StatusCode._200.toString())
                .build();

        HttpResponse response = requestDispatcher.dispatch(HttpRequestGenerator.createGetRequest());

        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("Should return response with statusCode 404 if the request is bad")
    void dispatchBad() {
        RequestDispatcher requestDispatcher = new RequestDispatcherImpl(Set.of(new ControllerMock()));
        HttpResponse expectedResponse = new HttpResponse.Builder()
                .setProtocol("HTTP/1.1")
                .setStatusCode(StatusCode._404.toString())
                .build();

        HttpResponse response = requestDispatcher.dispatch(HttpRequestGenerator.createBadRequest());

        assertEquals(expectedResponse, response);
    }
}