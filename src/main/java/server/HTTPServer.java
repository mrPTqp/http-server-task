package server;

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
        executor.execute(new HTTPParser(client));
        executor.shutdown();
    }
}

