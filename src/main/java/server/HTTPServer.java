package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HTTPServer {
    public static void main(String[] args) throws Throwable {
        int port = 8080;
        int coreNumberOfThreads = 2;
        int maxNumberOfThreads = 4;
        int keepAliveTime = 120;

        ExecutorService executor = new ThreadPoolExecutor(coreNumberOfThreads, maxNumberOfThreads,
                keepAliveTime, TimeUnit.SECONDS, new SynchronousQueue<>());

        ServerSocket server = new ServerSocket(port);
        Socket client = server.accept();
        executor.execute(new Task(client));
        executor.shutdown();
    }
}

class Task implements Runnable {

    private static Socket clientDialog;

    public Task(Socket client) {
        Task.clientDialog = client;
    }


    @Override
    public void run() {
        try {
            DataOutputStream out = new DataOutputStream(clientDialog.getOutputStream());
            DataInputStream in = new DataInputStream(clientDialog.getInputStream());

            while (!clientDialog.isClosed()) {
                String entry = in.readUTF();
                System.out.println("READ from clientDialog message:" + entry);

                if (entry.equalsIgnoreCase("quit")) {
                    System.out.println("Client initialize connection suicide");
                    out.writeUTF("Server closed");
                    break;
                }

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
