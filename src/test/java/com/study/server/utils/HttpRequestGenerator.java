package com.study.server.utils;

import com.study.server.http.HttpRequest;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.empty;

public class HttpRequestGenerator {
    private static final Map<String, String> defaultHeaders = Map.of(
            "Host".toLowerCase(), "food.com".toLowerCase(),
            "Accept".toLowerCase(), "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, */*".toLowerCase(),
            "Accept-Language".toLowerCase(), "ru".toLowerCase()
    );

    private static HttpRequest prepareRequest(Optional<String> method,
                                              Optional<String> path,
                                              Optional<String> protocol,
                                              Optional<Map<String, String>> queryParameters,
                                              Optional<Map<String, String>> headers,
                                              Optional<String> host,
                                              Optional<String> port) {
        var methodGen = method.orElse("GET");
        var pathGen = path.orElse("/");
        var protocolGen = protocol.orElse("HTTP/1.1");
        var queryGen = queryParameters.orElse(Collections.emptyMap());
        var headersGen = headers.orElse(defaultHeaders);
        var hostGen = host.orElse("food.com");
        var portGen = port.orElse("80");

        return new HttpRequest.Builder()
                .setMethod(methodGen)
                .setPath(pathGen)
                .setQueryParameters(queryGen)
                .setProtocol(protocolGen)
                .setHost(hostGen)
                .setPort(portGen)
                .setHeaders(headersGen)
                .build();
    }

    public static HttpRequest createGetRequest() {
        var path = "/css/style.css";
        var parameters = Map.of("name", "dima", "age", "27");
        var host = "food.com";

        return prepareRequest(
                empty(),
                Optional.of(path),
                empty(),
                Optional.of(parameters),
                empty(),
                Optional.of(host),
                empty()
        );
    }

    public static HttpRequest createPutRequest() {
        var method = "PUT";
        var path = "";
        var host = "food.com";

        return prepareRequest(
                Optional.of(method),
                Optional.of(path),
                empty(),
                empty(),
                empty(),
                Optional.of(host),
                empty()
        );
    }

    public static HttpRequest createPostRequest() {
        var method = "POST";
        var path = "/index.html";
        var host = "localhost";
        var port = "8080";
        var headers = Map.of(
                "Host".toLowerCase(), "localhost:8080".toLowerCase()
        );

        return prepareRequest(
                Optional.of(method),
                Optional.of(path),
                empty(),
                empty(),
                Optional.of(headers),
                Optional.of(host),
                Optional.of(port)
        );
    }

    public static HttpRequest createDeleteRequest() {
        var method = "DELETE";
        var path = "/index.html";
        var host = "food.com";

        return prepareRequest(
                Optional.of(method),
                Optional.of(path),
                empty(),
                empty(),
                empty(),
                Optional.of(host),
                empty()
        );
    }

    public static HttpRequest createBadRequest() {
        var method = " ";

        return prepareRequest(
                Optional.of(method),
                empty(),
                empty(),
                empty(),
                empty(),
                empty(),
                empty()
        );
    }
}
