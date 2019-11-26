package auxiliary;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;

public class HTTPServer {
    private int port;
    private Handler defaultHandler = null;
    private Map<String, Map<String, Handler>> handlers = new HashMap<>();
    private ExecutorService executor;

    public HTTPServer(int port, int coreNumberOfThreads, int maxNumberOfThreads, int keepAliveTime) {
        this.port = port;
        this.executor = new ThreadPoolExecutor(coreNumberOfThreads, maxNumberOfThreads,
                keepAliveTime, TimeUnit.SECONDS, new SynchronousQueue<>());
    }

    public void addHandler(String method, String path, Handler handler) {
        Map<String, Handler> methodHandlers = handlers.get(method);
        if (methodHandlers == null) {
            methodHandlers = new HashMap<>();
            handlers.put(method, methodHandlers);
        }
        methodHandlers.put(path, handler);
    }

    public void start() throws IOException {
        ServerSocket socket = new ServerSocket(port);
        System.out.println("Listening on port " + port);
        Socket client;
        while ((client = socket.accept()) != null) {
            System.out.println("Received connection from " + client.getRemoteSocketAddress().toString());
            SocketHandler handler = new SocketHandler(client, handlers);
            executor.execute(new SocketHandler(client, handlers));
        }
        executor.shutdown();
    }

    public static void main(String[] args) throws IOException {
        HTTPServer server = new HTTPServer(8080, 2, 4, 60);
        server.addHandler("GET", "/hello", new Handler() {
            public void handle(Request request, Response response) throws IOException {
                String html = "It works, " + request.getParameter("name") + "";
                response.setResponseCode(200, "OK");
                response.addHeader("Content-Type", "text/html");
                response.addBody(html);
            }
        });
        server.addHandler("GET", "/*", (Handler) new FileHandler());  // Default handler
        server.start();
    }
}

