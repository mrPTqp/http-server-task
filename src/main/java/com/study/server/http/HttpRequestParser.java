package com.study.server.http;

import com.study.server.exceptions.BadRequestException;
import com.study.server.utils.HttpPatternsUtils;
import com.study.server.utils.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;

public final class HttpRequestParser {
    @SuppressWarnings("PMD.FieldNamingConventions")
    private static final Logger log = Logger.getLogger(HttpRequestParser.class.getName());

    private HttpRequestParser() {
    }

    public static HttpRequest parse(InputStream in) {
        var br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        var builder = new HttpRequest.Builder();

        try {
            var curLine = br.readLine();
            var matcher = HttpPatternsUtils.MAIN_STRING.matcher(curLine);
            matcher.find();

            var method = matcher.group("method");
            if (StringUtils.isEmpty(method)) {
                log.severe("Method is mandatory!");
                throw new BadRequestException("Method is mandatory!");
            } else {
                builder.setMethod(methodParse(method));
            }

            var path = matcher.group("path");
            if (StringUtils.isEmpty(path)) {
                builder.setPath("");
            } else {
                builder.setPath(path);
            }

            var parameters = matcher.group("parameters");
            if (!StringUtils.isEmpty(parameters)) {
                builder.setQueryParameters(queryParse(parameters));
            }

            var protocol = matcher.group("protocol");
            if (checkProtocol(protocol)) {
                builder.setProtocol(protocol);
            } else {
                log.severe("Supported only HTTP/1.0 or HTTP/1.1");
                throw new BadRequestException("Supported only HTTP/1.0 or HTTP/1.1");
            }

            curLine = br.readLine();
            Map<String, String> headers = new HashMap<>();
            while (curLine != null && !curLine.equals("")) {
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
            log.severe("Can't parse request");
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
            log.severe("Method not supported");
            throw new BadRequestException("Method not supported");
        }
    }

    private static Map<String, String> queryParse(String parameters) {
        Map<String, String> queryParameters = new HashMap<>();
        Matcher pairsMatcher = HttpPatternsUtils.PAIRS_PATTERN.matcher(parameters);

        while (pairsMatcher.find()) {
            var key = pairsMatcher.group("key").toLowerCase();
            var value = pairsMatcher.group("value").toLowerCase();
            queryParameters.put(key, value);
        }

        return queryParameters;
    }

    private static boolean checkProtocol(String protocol) {
        var defaultProtocol1 = "HTTP/1.0";
        var defaultProtocol2 = "HTTP/1.1";
        return defaultProtocol1.equals(protocol) || defaultProtocol2.equals(protocol);
    }

    private static Map.Entry<String, String> headersParse(String curLine) {
        var matcher = HttpPatternsUtils.HEADERS_PATTERN.matcher(curLine);
        matcher.find();
        var key = matcher.group("key").toLowerCase();
        var value = matcher.group("value").trim().toLowerCase();
        var headers = Map.entry(key, value);

        if (headers.getKey().equals("") || headers.getValue().equals("")) {
            log.severe("Syntax error in header");
            throw new BadRequestException("Syntax error in header");
        } else {
            return headers;
        }
    }

    private static String extractHost(Map<String, String> headers) {
        var hostLine = headers.get("host");
        var matcher = HttpPatternsUtils.HOST_PATTERN.matcher(hostLine);
        matcher.find();

        return matcher.group("host");
    }

    private static String extractPort(Map<String, String> headers) {
        var hostLine = headers.get("host");
        var matcher = HttpPatternsUtils.HOST_PATTERN.matcher(hostLine);
        matcher.find();

        if (matcher.group("port") == null) {
            return "80";
        } else {
            return matcher.group("port");
        }
    }
}
