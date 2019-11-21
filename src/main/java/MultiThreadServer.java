import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultiThreadServer implements HTTPServer {
    private int port;
    private int coreNumberOfThread;
    private int maxNumberOfThread;

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void setCoreNumberOfThread(int coreNumberOfThread) {
        this.coreNumberOfThread = coreNumberOfThread;
    }

    @Override
    public void setMaxNumberOfThread(int maxNumberOfThread) {
        this.maxNumberOfThread = maxNumberOfThread;
    }

    @Override
    public void startServer(int port, int coreNumberOfThread, int maxNumberOfThread) throws IOException {
        ExecutorService executeIt = new ThreadPoolExecutor(coreNumberOfThread, maxNumberOfThread, 60, TimeUnit.SECONDS, new SynchronousQueue<>());

        ServerSocket ss = new ServerSocket(port);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (!ss.isClosed()) {
            if (br.ready()) {
                String serverCommand = br.readLine();
                if (serverCommand.equalsIgnoreCase("quit")) {
                    ss.close();
                    break;
                }
            }
            Socket client = ss.accept();
            executeIt.execute(new MonoThreadClientHandler(client));
            System.out.print("Connection accepted.");
        }
    }
}

