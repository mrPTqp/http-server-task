package com.study.server.utils;

import com.study.server.http.HttpRequest;
import com.study.server.http.HttpRequestParser;
import com.study.server.http.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static com.study.server.utils.TestUtils.readFile;

public class SocketMock extends Socket {
    private final String requestFileName;
    private ByteArrayOutputStream os;
    private final Map<String, Integer> counts = new HashMap<>();

    public SocketMock(String requestFileName) {
        this.requestFileName = requestFileName;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        byte[] input = readFile(requestFileName).readAllBytes();
        String requestLine = getStringFromBytesArray(input);

        if (counts.get(requestLine) == null) {
            counts.put(requestLine, 1);
        } else {
            var i = counts.get(requestLine);
            ++i;
            counts.put(requestLine, i);
        }

        return readFile(requestFileName);
    }

    @Override
    public OutputStream getOutputStream() {
        os = new ByteArrayOutputStream();
        return os;
    }

    public HttpRequest getInterceptedRequests() throws IOException {
        return HttpRequestParser.parse(getInputStream());
    }

    public boolean verifyResponse(HttpResponse expectedResponse) {
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        HttpResponse response = HttpResponseParser.parse(is);

        return expectedResponse.equals(response);
    }

    public Integer verifyRequest(InputStream is) throws IOException {
        byte[] input = is.readAllBytes();
        String requestLine = getStringFromBytesArray(input);

        return counts.get(requestLine);
    }

    public void clearMock() {
        counts.clear();
        os = null;
    }

    private String getStringFromBytesArray(byte[] input) {
        StringBuilder sb = new StringBuilder();

        for (byte b : input) {
            sb.append(b).append("\r\n");
        }

        return sb.toString();
    }
}