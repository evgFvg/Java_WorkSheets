import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Server server = new Server();
        server.getConnection();
    }
    private static final int PORT = 9876;
    private int msgNum = 10;
    private static final String MSG = "Pong";

    public void getConnection() throws IOException, ClassNotFoundException, InterruptedException {
        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Socket socket = serverSocket.accept();
             ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
        ) {
            System.out.println("Server has been started...");
            while (msgNum != 0) {
                System.out.println((String) inStream.readObject());
                outStream.writeObject(MSG);
                --msgNum;
                Thread.sleep(1000);
            }
        }
    }

}
