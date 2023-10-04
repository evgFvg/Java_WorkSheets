/****************************
 *  Written by : Evgenii    *
 *  Reviewer : Mor          *
 *  Date : 19.08.2023       *
 ****************************/

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class TcpClient {

    public static void main(String[] args) throws IOException {
        TcpClient client = new TcpClient();
        client.connect();
    }

    private static final int SERVER_TCP_PORT = 9876;
    private int msgNum = 5;
    private static final String MSG = "Ping_TCP";
    private static final int MSG_SIZE = 1024;
    private static final ByteBuffer writeBuffer = ByteBuffer.wrap(MSG.getBytes());
    private static final ByteBuffer readBuffer = ByteBuffer.allocate(MSG_SIZE);

    public void connect() throws IOException {

        try (SocketChannel clientChannel = SocketChannel.open()) {
            InetAddress localHost = InetAddress.getByName("localhost");
            clientChannel.connect(new InetSocketAddress(localHost, SERVER_TCP_PORT));
            System.out.println("Connection to TCP server " + clientChannel.getRemoteAddress() + " has been established...");

            while (msgNum > 0) {
                sendMsg(clientChannel);
                receiveMsg(clientChannel);

                Thread.sleep(1000);
                --msgNum;
            }
            System.out.println("TCP Client " + clientChannel.getLocalAddress() + " has been disconnected from server " + clientChannel.getRemoteAddress());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(SocketChannel clientChannel) throws IOException {
        clientChannel.write(writeBuffer);
        writeBuffer.clear();
    }

    private void receiveMsg(SocketChannel clientChannel) throws IOException {
        int readBytes = clientChannel.read(readBuffer);
        readBuffer.flip();
        String receivedMsg = new String(readBuffer.array(), 0, readBytes);
        readBuffer.clear();
        System.out.println("Received from " + clientChannel.getRemoteAddress() + " : " + receivedMsg);
    }
}
