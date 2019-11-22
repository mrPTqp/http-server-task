package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HTTPParser implements Runnable{
    private static Socket clientDialog;

    public HTTPParser(Socket client) {
        HTTPParser.clientDialog = client;
    }

    @Override
    public void run() {
        try {
            DataOutputStream out = new DataOutputStream(clientDialog.getOutputStream());
            DataInputStream in = new DataInputStream(clientDialog.getInputStream());

//            GET /wiki/страница HTTP/1.1
//            Host: ru.wikipedia.org
//            User-Agent: Mozilla/5.0 (X11; U; Linux i686; ru; rv:1.9b5) Gecko/2008050509 Firefox/3.0b5
//            Accept: text/html
//            Connection: close
//            (пустая строка)

            while (!clientDialog.isClosed()) {
                String entry = in.readUTF();
                System.out.println("READ from clientDialog message:" + entry);

                if (entry.equalsIgnoreCase("quit")) {
                    System.out.println("Client initialize connection suicide");
                    out.writeUTF("Server closed");
                    break;
                }

                String headerMethod = entry

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
