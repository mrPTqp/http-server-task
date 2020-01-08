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
    private static final HttpRequestParser parser = new HttpRequestParser();
    private static Pattern mainString = Pattern.compile("(?<method>[A-Z]+)( )((?<path>[a-zA-Z./]+)" +
            "((\\?)(?<parameters>[a-zA-Z\\d,.=&]+))?)? (?<protocol>HTTP/[\\d].[\\d])");
    private static Pattern pairsPattern = Pattern.compile("(?<key>[a-zA-Z\\d]+)=(?<value>[a-zA-Z\\d]+)");
    private static Pattern headersPattern = Pattern.compile("(?<key>[\\x20-\\x7D&&[^:]]+)" +
            ":( )?(?<value>[\\x20-\\x7D]+)");
    private static Pattern hostPattern = Pattern.compile("(?<host>[\\x20-\\x7D&&[^:]]+)(:)?(?<port>\\d+)?");

    private HttpRequestParser() {
    }

    public static HttpRequest parse(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        HttpRequest.Builder builder = new HttpRequest.Builder();

        try {
            String curLine = br.readLine();
            Matcher matcher = mainString.matcher(curLine);

            String method = methodParse(matcher);
            builder.setMethod(method);

            String path = pathParse(curLine, matcher);
            builder.setPath(path);

            Map<String, String> queryParameters;
            if (curLine.contains("?")) {
                queryParameters = queryParse(matcher);
                builder.setQueryParameters(queryParameters);
            }

            String protocol = protocolParse(matcher);
            builder.setProtocol(protocol); // TODO: 08.01.2020 использовать только HTTP/1.1

            curLine = br.readLine();
            Map<String, String> headers = new HashMap<>();
            while (!curLine.equals("")) {
                String[] pair = headersParse(curLine);
                headers.put(pair[0], pair[1]);
                curLine = br.readLine();
            }
            builder.setHeaders(headers);

            String host = hostParse(headers);
            builder.setHost(host);

            String port = portParse(headers);
            builder.setPort(port);
        } catch (Exception e) {
            throw new BadRequestException("Can't parse request");
        }
        return builder.build();
    }

    private static String methodParse(Matcher matcher) {
        matcher.find();
        String method = matcher.group("method");

        return method;
    }

    private static String pathParse(String curLine, Matcher matcher) {
        String path;

        if (curLine.contains(" /")) {
            path = matcher.group("path");
        } else {
            path = "";
        }

        return path;
    }

    private static Map<String, String> queryParse(Matcher matcher) {
        Map<String, String> queryParameters = new HashMap<>();
        String parameters = matcher.group("parameters");
        Matcher pairsMatcher = pairsPattern.matcher(parameters);

        while (pairsMatcher.find()) {
            String key = pairsMatcher.group("key").toLowerCase();
            String value = pairsMatcher.group("value").toLowerCase();
            queryParameters.put(key, value);
        }

        return queryParameters;
    }

    private static String protocolParse(Matcher matcher) {
        String protocol = matcher.group("protocol");

        return protocol;
    }

    private static String[] headersParse(String curLine) {
        Matcher matcher = headersPattern.matcher(curLine);
        String[] headers = new String[2];
        matcher.find();
        headers[0] = matcher.group("key").toLowerCase();
        headers[1] = matcher.group("value").toLowerCase();

        return headers;
    }

    private static String hostParse(Map<String, String> headers) {
        String hostLine = headers.get("host");
        Matcher matcher = hostPattern.matcher(hostLine);
        matcher.find();
        String host = matcher.group("host");

        return host;
    }

    private static String portParse(Map<String, String> headers) {
        String hostLine = headers.get("host");
        Matcher matcher = hostPattern.matcher(hostLine);
        matcher.find();
        String port;

        if (matcher.group("port") == null) {
            port = "80";
        } else {
            port = matcher.group("port");
        }

        return port;
    }
}
