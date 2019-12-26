package com.study.server.http;

import com.study.server.exceptions.BadRequestException;

import java.io.BufferedReader;
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
        Pattern mainString = Pattern.compile("(?<method>[A-Z])+ ((?<path>[a-zA-Z\\.\\/]+)((\\?)?" +
                "(((?<parameters>[a-zA-Z\\d,.=])+)(&)?)+)?)? (?<protocol>HTTP\\/[\\d].[\\d])");
        Pattern headersPattern = Pattern.compile("[\\x20-\\x7D&&[^:]]+:( )?[\\x20-\\x7D]+");
        Pattern hostPattern = Pattern.compile("^[\\w\\.-]+");

        try {
            String curLine = br.readLine();
            Matcher matcher = mainString.matcher(curLine);
            matcher.find();
            request.setMethod(matcher.group("method"));
            String path;

            try {
                path = matcher.group("path");
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

            String parameters = matcher.group("parameters");
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

            curLine = br.readLine();
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
        } catch (Exception e) {
            throw new BadRequestException("Can't parse request");
        }
        return request;
    }
}