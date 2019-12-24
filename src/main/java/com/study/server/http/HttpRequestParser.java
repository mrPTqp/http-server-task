package com.study.server.http;

import com.study.server.exceptions.BadRequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequestParser implements RequestParser {
    private static HttpRequestParser parser = new HttpRequestParser();

    private HttpRequestParser() {
    }

    public static HttpRequestParser getParser() {
        return parser;
    }

    @Override
    public Request parse(InputStream in) {
        Request request = new Request();
        Map<String, String> queryParameters = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        Pattern methodPattern = Pattern.compile("^[A-Z]+");
        Pattern pathPattern = Pattern.compile(" \\/([a-zA-Z\\.\\/]+)?");
        Pattern parametersPattern = Pattern.compile("[a-zA-Z\\d,.]+=[a-zA-Z\\d,.]+");
        Pattern protocolPattern = Pattern.compile("HTTP\\/[\\d].[\\d]");
        Pattern headersPattern = Pattern.compile(".+: .+");
        Pattern hostPattern = Pattern.compile("^[\\w\\.-]+");

        try {
            String first = br.readLine();
            Matcher matcher = methodPattern.matcher(first);
            matcher.find();
            String method = matcher.group(0);
            request.setMethod(method);

            matcher = pathPattern.matcher(first);
            matcher.find();
            String path;

            try {
                path = matcher.group(0).trim();
            } catch (Exception e) {
                path = "";
            }

            if (!path.equals("")) {
                if (path.equals("/")) {
                    path = path + "index.html";
                }
                request.setPath(path);
            } else {
                path = "/index.html";
                request.setPath(path);
            }

            matcher = parametersPattern.matcher(first);

            while (matcher.find()) {
                String[] partsOfParameters = matcher.group().split("=");
                queryParameters.put(partsOfParameters[0], partsOfParameters[1]);
            }
            request.setQueryParameters(queryParameters);

            matcher = protocolPattern.matcher(first);
            matcher.find();
            String protocol = matcher.group(0);
            request.setProtocol(protocol);

            String curLine = br.readLine();
            while (!curLine.equals("")) {
                matcher = headersPattern.matcher(curLine);
                matcher.find();
                String[] partsOfHeaders = matcher.group().split(": ");
                headers.put(partsOfHeaders[0], partsOfHeaders[1]);
                curLine = br.readLine();
            }
            request.setHeaders(headers);

            String nonFormattedHost = headers.get("Host");
            matcher = hostPattern.matcher(nonFormattedHost);
            matcher.find();
            String host = matcher.group(0).toLowerCase();
            request.setHost(host);

            curLine = br.readLine();
            StringBuilder sb = new StringBuilder();
            while (curLine != null) {
                sb.append(curLine).append("\r\n");
                curLine = br.readLine();
            }

            if (sb.toString().length() > 0 & !method.equals("GET") & !method.equals("DELETE")) {
                request.setBody(sb.toString().stripTrailing());
            }
        } catch (IOException e) {
            throw new BadRequestException("Can't parse request");
        }
        return request;
    }
}