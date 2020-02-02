package com.study.server.sh;

import com.study.server.SocketHandlerImpl;
import com.study.server.http.HttpRequestParser;
import com.study.server.http.HttpResponse;
import com.study.server.http.StatusCode;
import com.study.server.utils.RequestDispatcherMock;
import com.study.server.utils.SocketMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.study.server.utils.TestUtils.readFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SocketHandlerImplTest {

    @Test
    @DisplayName("Should process the request if it is correct")
    void dispatch() throws IOException {
        var socketMock = new SocketMock("GET");
        var requestDispatcherMock = new RequestDispatcherMock();
        var shGet = new SocketHandlerImpl(socketMock, requestDispatcherMock);

        shGet.run();

        assertEquals(1, requestDispatcherMock.verifyRequest(socketMock.getInterceptedRequests()));
    }

    @Test
    @DisplayName("Should respond to a specific request with the expected response")
    void output() {
        var socketMock = new SocketMock("GET");
        var requestDispatcherMock = new RequestDispatcherMock();
        var sh = new SocketHandlerImpl(socketMock, requestDispatcherMock);

        sh.run();

        assertTrue(socketMock
                .verifyResponse(requestDispatcherMock.dispatch(HttpRequestParser.parse(readFile("GET")))));
    }

    @Test
    @DisplayName("Should pass response with statusCode 500 if the request is bad")
    void badRequest() {
        var socketMock = new SocketMock("bad1");
        var requestDispatcherMock = new RequestDispatcherMock();
        var sh = new SocketHandlerImpl(socketMock, requestDispatcherMock);

        var expectedResponse = new HttpResponse.Builder().setProtocol("HTTP/1.1")
                .setStatusCode(StatusCode._400.toString())
                .build();

        sh.run();

        assertTrue(socketMock.verifyResponse(expectedResponse));
    }
}