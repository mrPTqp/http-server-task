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
        Pattern mainString = Pattern.compile("(?<method>[A-Z]+)( )((?<path>[a-zA-Z./]+)((\\?)(?<parameters>[a-zA-Z\\d,.=&]+))?)? (?<protocol>HTTP/[\\d].[\\d])");
        Pattern pairsPattern = Pattern.compile("(?<key>[a-zA-Z\\d]+)=(?<value>[a-zA-Z\\d]+)");
        Pattern headersPattern = Pattern.compile("(?<key>[\\x20-\\x7D&&[^:]]+):( )?(?<value>[\\x20-\\x7D]+)");
        Pattern hostPattern = Pattern.compile("(?<host>[\\x20-\\x7D&&[^:]]+)(:)?(?<port>\\d+)?");

        try {
            String curLine = br.readLine();
            Matcher matcher = mainString.matcher(curLine);
            matcher.find();
            String method = matcher.group("method");
            request.setMethod(method);
            String path;

            if (curLine.contains(" /")) {
                path = matcher.group("path");
            } else {
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

            if (curLine.contains("?")) {
                String parameters = matcher.group("parameters");
                Matcher pairsMatcher = pairsPattern.matcher(parameters);

                while (pairsMatcher.find()) {
                    String key = pairsMatcher.group("key").toLowerCase();
                    String value = pairsMatcher.group("value").toLowerCase();
                    queryParameters.put(key, value);
                }
                request.setQueryParameters(queryParameters);
            }

            String protocol = matcher.group("protocol");
            request.setProtocol(protocol);

            curLine = br.readLine();
            while (!curLine.equals("")) {
                matcher = headersPattern.matcher(curLine);
                matcher.find();
                String key = matcher.group("key").toLowerCase();
                String value = matcher.group("value").toLowerCase();
                headers.put(key, value);
                curLine = br.readLine();
            }
            request.setHeaders(headers);

            String hostLine = headers.get("host");
            matcher = hostPattern.matcher(hostLine);
            matcher.find();
            String host = matcher.group("host");
            String port;
            try {
                port = matcher.group("port");
            } catch (Exception e) {
                port = "80";
            }
            request.setHost(host);
            request.setPort(port);

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