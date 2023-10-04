import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class BroadCastUdpClient {
    public static void main(String[] args) throws IOException {
        BroadCastUdpClient client = new BroadCastUdpClient();
        client.sendBroadcastMsg();
    }

    private static final int UDP_SERVER_PORT = 9877;
    private static final String broadCastAddress = "10.1.255.255";
    private int msgNum = 5;
    private static final String MSG = "Broadcast_Msg";
    private static final int MSG_SIZE = 1024;
    ByteBuffer readBuffer = ByteBuffer.allocate(MSG_SIZE);


    public void sendBroadcastMsg() throws IOException {
        try (DatagramChannel clientChannel = DatagramChannel.open()) {
            clientChannel.socket().setBroadcast(true);
            System.out.println("Connection from BroadCast client to UDP server has been established...");
            InetSocketAddress serverAddress = new InetSocketAddress(broadCastAddress, UDP_SERVER_PORT);

            while (msgNum > 0) {
                sendMsg(clientChannel, serverAddress);
                receiveMsg(clientChannel);

                Thread.sleep(1000);
                --msgNum;
            }
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
