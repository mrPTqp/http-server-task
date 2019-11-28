package server;

import sun.nio.cs.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class HttpRequestParserImpl implements HttpRequestParser, Runnable {
    private Socket client;
    private String method;
    private String URL;
    private String path;
    private String HTTPversion;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> queryParameters = new HashMap<>();

    public HttpRequestParserImpl(Socket client) {
        this.client = client;
    }

    @Override
    public boolean parse(BufferedReader in) throws IOException {
        String initialLine = in.readLine();
        String[] partOfInit = initialLine.split(" ");
        method = partOfInit[0];
        URL = partOfInit[1];
        HTTPversion = partOfInit[2];
        if (method == null || URL == null || HTTPversion == null) {
            return false;
        }

        StringTokenizer tok = new StringTokenizer(initialLine);
        while (true) {
            String headerLine = in.readLine();
            if (headerLine.length() == 0) {
                break;
            }

            int separator = headerLine.indexOf(":");
            if (separator == -1) {
                return false;
            }
            headers.put(headerLine.substring(0, separator),
                    headerLine.substring(separator + 1));
        }

        if (URL.indexOf("?") == -1) {
            path = URL;
        } else {
            path = URL.substring(0, URL.indexOf("?"));
            parseQueryParameters(URL.substring(URL.indexOf("?") + 1));
        }

        if ("/".equals(path)) {
            path = "/index.html";
        }

        return true;
    }

    private void parseQueryParameters(String queryString) {
        for (String parameter : queryString.split("&")) {
            int separator = parameter.indexOf('=');
            if (separator > -1) {
                queryParameters.put(parameter.substring(0, separator),
                        parameter.substring(separator + 1));
            } else {
                queryParameters.put(parameter, null);
            }
        }
    }

    @Override
    public void run() {
        HttpRequestParserImpl parser = new HttpRequestParserImpl(client);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            parser.parse(in);
            System.out.println(method);
            System.out.println(URL);
            System.out.println(path);
            System.out.println(HTTPversion);
            System.out.println(headers);
            System.out.println(queryParameters);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
