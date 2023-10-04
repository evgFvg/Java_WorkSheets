/****************************
 *  Written by : Evgenii    *
 *  Reviewer : Mor          *
 *  Date : 19.08.2023       *
 ****************************/


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;


public class UdpClient {

    public static void main(String[] args) throws IOException {
        UdpClient client = new UdpClient();
        client.connect();
    }

    private static final int SERVER_PORT = 9877;
    private int msgNum = 5;
    private static final String MSG = "Ping_UDP";
    private static final int MSG_SIZE = 1024;
    private final ByteBuffer readBuffer = ByteBuffer.allocate(MSG_SIZE);
    private InetSocketAddress serverAddress;


    public void connect() throws IOException {

        try (DatagramChannel clientChannel = DatagramChannel.open()) {
            System.out.println("Connection to UDP server has been established...");
            serverAddress = new InetSocketAddress("localhost", SERVER_PORT);


            while (msgNum > 0) {
                sendMsg(clientChannel, serverAddress);
                receiveMsg(clientChannel);

                Thread.sleep(1000);
                --msgNum;
            }
            System.out.println("UDP Client  has been disconnected from server " + serverAddress);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(DatagramChannel clientChannel, InetSocketAddress serverAddress) throws IOException {
        clientChannel.send(ByteBuffer.wrap(MSG.getBytes()), serverAddress);
    }

    private void receiveMsg(DatagramChannel clientChannel) throws IOException {
        InetSocketAddress serverResponse = (InetSocketAddress) clientChannel.receive(readBuffer);
        readBuffer.flip();
        byte[] response = new byte[readBuffer.limit()];
        readBuffer.get(response);
        String res = new String(response);
        System.out.println("Received from : " + serverResponse + " " + res);
    }
}

