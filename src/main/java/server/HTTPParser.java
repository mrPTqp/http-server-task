package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class HTTPParser implements Runnable {
    private static Socket clientDialog;

    public HTTPParser(Socket client) {
        HTTPParser.clientDialog = client;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientDialog.getInputStream()));
            PrintWriter out = new PrintWriter(clientDialog.getOutputStream());

//            GET /wiki/страница HTTP/1.1
//            Host: ru.wikipedia.org
//            User-Agent: Mozilla/5.0 (X11; U; Linux i686; ru; rv:1.9b5) Gecko/2008050509 Firefox/3.0b5
//            Accept: text/html
//            Connection: close
//            (пустая строка)

            while (!clientDialog.isClosed()) {
                String input = in.readLine();
                StringTokenizer parse = new StringTokenizer(input);
                String method = parse.nextToken().toUpperCase();
                String fileRequested = parse.nextToken().toLowerCase();

                out.writeUTF("Server reply - " + entry + " - OK");
                out.flush();
            }

            in.close();
            out.close();
            clientDialog.close();
            System.out.println("Closing connections & channels - DONE.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
