package com.study.server.utils;

import com.study.server.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class HttpResponseParser {
    public static final Pattern statusString = Pattern.compile("(?<protocol>HTTP/[\\d].[\\d])( )"
            + "(?<statusCode>[\\x41-\\x5A[\\x61-\\x7A[\\x30-\\x39[ ]]]]+)"
    );

    public static final Pattern headersPattern = Pattern.compile("(?<key>[\\x20-\\x7D&&[^:]]+)"
            + "(: )(?<value>[\\x20-\\x7D]+)"
    );

    private HttpResponseParser() {
    }

    public static HttpResponse parse(InputStream in) {
        var br = new BufferedReader(new InputStreamReader(in));
        var builder = new HttpResponse.Builder();

        String curLine;
        try {
            curLine = br.readLine();
            var statusMatcher = statusString.matcher(curLine);
            statusMatcher.find();

            var protocol = statusMatcher.group("protocol");
            var statusCode = statusMatcher.group("statusCode");

            curLine = br.readLine();
            Map<String, String> headers = new HashMap<>();
            while (!curLine.equals("")) {
                Map.Entry<String, String> pair = headersParse(curLine);
                headers.put(pair.getKey(), pair.getValue());
                curLine = br.readLine();
            }

            curLine = br.readLine();
            StringBuilder body = new StringBuilder();
            while (curLine != null) {
                body.append(curLine);
                curLine = br.readLine();
                if (curLine != null) {
                    body.append("\n");
                }
            }

            builder.setProtocol(protocol)
                    .setStatusCode(statusCode)
                    .setHeaders(headers)
                    .setBody(body.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.build();
    }

    private static Map.Entry<String, String> headersParse(String curLine) {
        var matcher = headersPattern.matcher(curLine);
        matcher.find();
        var key = matcher.group("key");
        var value = matcher.group("value");

        return Map.entry(key, value);
    }
}
