import java.io.IOException;
import java.net.*;

public class ClientUDP {
    public static void main(String[] args) throws IOException {
        ClientUDP client = new ClientUDP();
        client.connect();
    }
    private int MSG_NUM = 10;
    private static final int BUFFER_SIZE = 1024;
    private byte[] msgBuffer = new byte[BUFFER_SIZE];
    private static final int PORT = 4445;
    private static final String MSG = "Ping";

    public void connect() throws IOException {
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName("localhost");
            System.out.println("Client trying to connect to server...");

            while(MSG_NUM > 0) {
                msgBuffer = MSG.getBytes();
                DatagramPacket dgPacket = new DatagramPacket(msgBuffer, msgBuffer.length, serverAddress, PORT);
                clientSocket.send(dgPacket);

                clientSocket.receive(dgPacket);
                System.out.println(new String(dgPacket.getData(), 0, dgPacket.getLength()));
                --MSG_NUM;
            }

            System.out.println("Client is disconnecting...");
        }
    }
}
