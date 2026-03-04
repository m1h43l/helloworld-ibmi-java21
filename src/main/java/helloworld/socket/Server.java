package helloworld.socket;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadFactory;

public class Server {

    public void start() {
        HelidonTaskExecutor executor = ThreadPerTaskExecutor.create(virtualThreadFactory());
        boolean running = true;

        try (ServerSocket serverSocket = new ServerSocket(35810)) {
            while (running) {
                Socket socket = serverSocket.accept();
                executor.execute(new ConnectionHandler(socket));
            }
        } catch (Exception e) {
            System.err.println("Error during listening for connection");
            e.printStackTrace();
        }
    }

    private static ThreadFactory virtualThreadFactory() {
        return Thread.ofVirtual().factory();
    }
}
