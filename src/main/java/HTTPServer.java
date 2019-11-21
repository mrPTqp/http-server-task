import java.io.IOException;

public interface HTTPServer {
    void setPort(int port);
    void setCoreNumberOfThread(int coreNumberOfThread);
    void setMaxNumberOfThread(int maxNumberOfThread);
    void startServer(int port, int coreNumberOfThread, int maxNumberOfThread) throws IOException;
}
