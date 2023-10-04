import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Client client = new Client();
        client.connect();
    }
    private static final int SERVER_PORT = 9876;
    private int MSG_NUM = 10;
    private static final String MSG = "Ping";

    public void connect() throws IOException, ClassNotFoundException {
        InetAddress localHost = InetAddress.getLocalHost();
        try(Socket socket = new Socket(localHost.getHostName(), SERVER_PORT);
            ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream()))
         {
            System.out.println("Connection to server has been established...");
            while(0 != MSG_NUM) {
                outStream.writeObject(MSG);
                System.out.println((String)inStream.readObject());
                Thread.sleep(1000);
                --MSG_NUM;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
