package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HTTPHTTPServerImpl implements HTTPServer {
    private ExecutorService executor;

    public HTTPHTTPServerImpl(int port, int coreNumberOfThreads, int maxNumberOfThreads, int keepAliveTime) {
        this.executor = new ThreadPoolExecutor(coreNumberOfThreads, maxNumberOfThreads,
                keepAliveTime, TimeUnit.SECONDS, new SynchronousQueue<>());
    }

    @Override
    public void start(int port, int coreNumberOfThreads, int maxNumberOfThreads, int keepAliveTime) throws IOException {
        HTTPHTTPServerImpl server = new HTTPHTTPServerImpl(8080, 2, 8, 60);
        ServerSocket socket = new ServerSocket(port);
        Socket client;
        while ((client = socket.accept()) != null) {
            System.out.println("Received connection from " + client.getRemoteSocketAddress().toString());
            executor.execute(new HTTPParserImpl(client));
        }
        executor.shutdown();
    }
}
