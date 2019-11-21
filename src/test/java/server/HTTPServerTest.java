package server;

import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HTTPServerTest {

    @Test
    public void main() throws InterruptedException {


        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 5; i++) {
            executor.execute(new TestRunnableClientTester());
            Thread.sleep(10);
        }
        executor.shutdown();
    }
}

class TestRunnableClientTester implements Runnable {

    static Socket socket;

    public TestRunnableClientTester() {
        try {

            socket = new Socket("localhost", 8080);
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {


        try {
            DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
            DataInputStream ois = new DataInputStream(socket.getInputStream());

            for (int i = 0; i < 5; i++) {
                oos.writeUTF("clientCommand " + i);
                oos.flush();
                Thread.sleep(10);
                String in = ois.readUTF();
                System.out.println(in);
                Thread.sleep(5000);

            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}