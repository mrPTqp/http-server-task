package server;

import java.io.IOException;

public interface HTTPServer {
    void start(int port, int coreNumberOfThreads, int maxNumberOfThreads, int keepAliveTime) throws IOException;
}
