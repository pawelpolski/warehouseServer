package pawelpolski;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by macbookpro on 29.06.2017.
 */
public class Server extends Thread {
    private Model model;
    private int portNumber;
    private boolean isDone;

    public Server(int portNumber) throws IOException {
        model = new Model();
        this.portNumber = portNumber;
        isDone = false;
    }

    @Override
    public void run() {
        isDone = false;

        try {
            ServerSocket server = new ServerSocket(portNumber);
            System.out.println("Server started on port: " + server.getLocalPort());
            while (!isDone) {
                Socket socket = server.accept();
                if (isDone) {
                    break;
                }
                ClientConnection client = new ClientConnection(socket, model);
                client.start();
            }

            server.close();
        } catch (Exception ex) {

        }
    }

    public void close() {
        isDone = true;

        try {
            new Socket("localhost", portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        model.reset();
    }
}
