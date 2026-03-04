package helloworld.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * This class reads the first 30 bytes from the passed socket and then closes
 * it. It uses InputStream::readNBytes to read the data. The data is output to
 * the console (stdout).
 */
public class ConnectionHandler implements InterruptableTask<Void> {

    private Socket socket;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public boolean canInterrupt() {
        return true;
    }

    @Override
    public void run() {
        System.out.println("Starting new vthread " + Thread.currentThread().threadId());

        try {
            InputStream in = socket.getInputStream();
            byte[] data = in.readNBytes(30);
            System.out.println("Data: " + new String(data, StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.err.println("Error reading socket of client connection: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error on closing socket");
                e.printStackTrace();
            }
        }

        System.out.println("Ending vthread " + Thread.currentThread().threadId());
    }

}
