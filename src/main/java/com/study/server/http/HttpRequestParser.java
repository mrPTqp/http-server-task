package com.study.server.http;

import com.study.server.exceptions.BadRequestException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequestParser {
    private static Pattern mainString = Pattern.compile("(?<method>[\\x41-\\x5A]+)( )" +
            "((?<path>[\\x41-\\x5A[\\x61-\\x7A[\\x30-\\x39[./]]]]+)" +
            "((\\?)(?<parameters>[\\x41-\\x5A[\\x61-\\x7A[\\x30-\\x39[,.=&]]]]+))?)? " +
            "(?<protocol>HTTP/[\\d].[\\d])"
    );
    private static Pattern pairsPattern = Pattern.compile("(?<key>[a-zA-Z\\d]+)=(?<value>[a-zA-Z\\d]+)");
    private static Pattern headersPattern = Pattern.compile("(?<key>[\\x20-\\x7D&&[^:]]+):(?<value>[\\x20-\\x7D]+)");
    private static Pattern hostPattern = Pattern.compile("(?<host>[\\x20-\\x7D&&[^:]]+)(:)?(?<port>\\d+)?");

    private HttpRequestParser() {
    }

    public static HttpRequest parse(InputStream in) {
        var br = new BufferedReader(new InputStreamReader(in));
        var builder = new HttpRequest.Builder();

        try {
            var curLine = br.readLine();
            var matcher = mainString.matcher(curLine);
            matcher.find();

            var method = matcher.group("method");
            if (!method.isEmpty()) {
                builder.setMethod(methodParse(method));
            } else {
                throw new BadRequestException("Method is mandatory!");
            }

            if (curLine.contains(" /")) {
                builder.setPath(matcher.group("path"));
            } else {
                builder.setPath("");
            }

            if (curLine.contains("?")) {
                var parameters = matcher.group("parameters");
                builder.setQueryParameters(queryParse(parameters));
            }

            var protocol = matcher.group("protocol");
            builder.setProtocol(protocolCheck(protocol));

            curLine = br.readLine();
            Map<String, String> headers = new HashMap<>();
            while (!curLine.equals("")) {
                Map.Entry<String, String> pair = headersParse(curLine);
                headers.put(pair.getKey(), pair.getValue());
                curLine = br.readLine();
            }
            builder.setHeaders(headers);

            var host = hostParse(headers);
            builder.setHost(host);

            var port = portParse(headers);
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
        Matcher pairsMatcher = pairsPattern.matcher(parameters);

        while (pairsMatcher.find()) {
            var key = pairsMatcher.group("key").toLowerCase();
            var value = pairsMatcher.group("value").toLowerCase();
            queryParameters.put(key, value);
        }

        return queryParameters;
    }

    private static String protocolCheck(String protocol) {
        var defaultProtocol = "HTTP/1.1";
        if (defaultProtocol.equals(protocol)) {
            return protocol;
        } else {
            throw new BadRequestException("Supported only HTTP/1.1");
        }
    }

    private static Map.Entry<String, String> headersParse(String curLine) {
        var matcher = headersPattern.matcher(curLine);
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

    private static String hostParse(Map<String, String> headers) {
        var hostLine = headers.get("host");
        var matcher = hostPattern.matcher(hostLine);
        matcher.find();

        return matcher.group("host");
    }

    private static String portParse(Map<String, String> headers) {
        var hostLine = headers.get("host");
        var matcher = hostPattern.matcher(hostLine);
        matcher.find();

        if (matcher.group("port") == null) {
            return "80";
        } else {
            return matcher.group("port");
        }
    }
}
