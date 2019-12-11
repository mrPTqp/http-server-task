package com.study.server;

import com.study.server.http.Request;
import com.study.server.http.RequestParserImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketHandlerImpl implements SocketHandler, Runnable {
    private Socket client;

    public SocketHandlerImpl(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        String sitesAndConfigDirectory = System.getenv().get("SitesAndConfigDirectory");


//        String inputLine = readLine();
//        RequestParserImpl parser = new RequestParserImpl(inputLine);

        try {
            in = clientSocket.getInputStream();
            out = clientSocket.getOutputStream();

//            if (!parser.parse(inputLine)) {
//                respond(500, "Unable to parse request", out);
//                return;
//            }

//            Request request = parser.getRequest();
//            RequestDispatcherImpl dispatcher = new RequestDispatcherImpl(request);
//            response = dispatcher.dispatch();
//            sendResponse(response);


            File myFile = new File(sitesAndConfigDirectory + "\\www.food.com\\index.html");
            byte[] mybytearray = new byte[(int) myFile.length()];
            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(mybytearray, 0, mybytearray.length);
            out.write(("HTTP/1.1 200 OK" + "\r\n").getBytes());
            out.write(mybytearray, 0, mybytearray.length);
            out.flush();
            System.out.println("File is transfered");

            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readLine() {
        try {
            final BufferedReader inBuff = new BufferedReader(new InputStreamReader(in));
            String inputLine;
            final StringBuilder content = new StringBuilder();

            while (!(inputLine = inBuff.readLine()).equals("")) {
                content.append(inputLine).append("\n");
            }

            return content.toString();
        } catch (final Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
