import java.io.IOException;
import java.net.*;

public class ServerUDP {

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerUDP server = new ServerUDP();
        server.getConnection();
    }

    private int MSG_NUM = 10;
    private static final int BUFFER_SIZE = 1024;
    private byte[] msgBuffer = new byte[BUFFER_SIZE];
    private static final int PORT = 4445;
    private static final String MSG = "Pong";

    public void getConnection() throws IOException, InterruptedException {
        try(DatagramSocket serverSocket = new DatagramSocket(PORT)){

            System.out.println("Server is running on the Port " + PORT);

            while(MSG_NUM > 0) {
                DatagramPacket receivedPacket = new DatagramPacket(msgBuffer, msgBuffer.length);
                serverSocket.receive(receivedPacket);
                System.out.println(new String(receivedPacket.getData(), 0 , receivedPacket.getLength()));

                InetAddress clientAddress = receivedPacket.getAddress();
                int clientPort = receivedPacket.getPort();
                msgBuffer = MSG.getBytes();

                receivedPacket = new DatagramPacket(msgBuffer, msgBuffer.length, clientAddress, clientPort);
                serverSocket.send(receivedPacket);
                --MSG_NUM;
                Thread.sleep(1000);
            }

            System.out.println("Server is shutting down");
        }
    }
}
