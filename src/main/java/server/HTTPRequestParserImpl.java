package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class HTTPRequestParserImpl implements HTTPRequestParser, Runnable {
    private Socket client;
    private String method;
    private String URL;
    private String path;
    private String HTTPversion;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> queryParameters = new HashMap<>();

    public HTTPRequestParserImpl(Socket client) {
        this.client = client;
    }

    @Override
    public boolean parse(BufferedReader in) throws IOException {
        String initialLine = in.readLine();
        StringTokenizer tok = new StringTokenizer(initialLine);
        if(tok.hasMoreElements()) {
            method = tok.nextToken();
            URL = tok.nextToken();
            HTTPversion = tok.nextToken();
        } else {
            return false;
        }

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
        HTTPRequestParserImpl parser = new HTTPRequestParserImpl(client);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            parser.parse(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
