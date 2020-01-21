package com.study.server.http;

import com.study.server.exceptions.BadRequestException;
import com.study.server.utils.ParsingPatterns;
import com.study.server.utils.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class HttpRequestParser {

    private HttpRequestParser() {
    }

    public static HttpRequest parse(InputStream in) {
        var br = new BufferedReader(new InputStreamReader(in));
        var builder = new HttpRequest.Builder();

        try {
            var curLine = br.readLine();
            var matcher = ParsingPatterns.mainString.matcher(curLine);
            matcher.find();

            var method = matcher.group("method");
            if (!StringUtils.isEmpty(method)) {
                builder.setMethod(methodParse(method));
            } else {
                throw new BadRequestException("Method is mandatory!");
            }

            var path = matcher.group("path");
            if (!StringUtils.isEmpty(path)) {
                builder.setPath(path);
            } else {
                builder.setPath("");
            }

            var parameters = matcher.group("parameters");
            if (!StringUtils.isEmpty(parameters)) {
                builder.setQueryParameters(queryParse(parameters));
            }

            var protocol = matcher.group("protocol");
            if (checkProtocol(protocol)) {
                builder.setProtocol(protocol);
            } else {
                throw new BadRequestException("Supported only HTTP/1.1");
            }

            curLine = br.readLine();
            Map<String, String> headers = new HashMap<>();
            while (!curLine.equals("")) {
                Map.Entry<String, String> pair = headersParse(curLine);
                headers.put(pair.getKey(), pair.getValue());
                curLine = br.readLine();
            }
            builder.setHeaders(headers);

            var host = extractHost(headers);
            builder.setHost(host);

            var port = extractPort(headers);
            builder.setPort(port);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Can't parse request");
        }
        return builder.build();
    }

    private static String methodParse(String method) {
        var methodIsSupported = false;

        for (HttpMethods elem : HttpMethods.values()) {
            if (elem.name().equals(method)) {
                methodIsSupported = true;
                break;
            }
        }

        if (methodIsSupported) {
            return method;
        } else {
            throw new BadRequestException("Method not supported");
        }
    }

    private static Map<String, String> queryParse(String parameters) {
        Map<String, String> queryParameters = new HashMap<>();
        Matcher pairsMatcher = ParsingPatterns.pairsPattern.matcher(parameters);

        while (pairsMatcher.find()) {
            var key = pairsMatcher.group("key").toLowerCase();
            var value = pairsMatcher.group("value").toLowerCase();
            queryParameters.put(key, value);
        }

        return queryParameters;
    }

    private static boolean checkProtocol(String protocol) {
        var defaultProtocol = "HTTP/1.1";
        return defaultProtocol.equals(protocol);
    }

    private static Map.Entry<String, String> headersParse(String curLine) {
        var matcher = ParsingPatterns.headersPattern.matcher(curLine);
        matcher.find();
        var key = matcher.group("key").toLowerCase();
        var value = matcher.group("value").trim().toLowerCase();
        var headers = Map.entry(key, value);

        if (headers.getKey().equals("") || headers.getValue().equals("")) {
            throw new BadRequestException("Syntax error in header");
        } else {
            return headers;
        }
    }

    private static String extractHost(Map<String, String> headers) {
        var hostLine = headers.get("host");
        var matcher = ParsingPatterns.hostPattern.matcher(hostLine);
        matcher.find();

        return matcher.group("host");
    }

    private static String extractPort(Map<String, String> headers) {
        var hostLine = headers.get("host");
        var matcher = ParsingPatterns.hostPattern.matcher(hostLine);
        matcher.find();

        if (matcher.group("port") == null) {
            return "80";
        } else {
            return matcher.group("port");
        }
    }
}
