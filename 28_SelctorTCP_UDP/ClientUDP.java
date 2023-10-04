/****************************************
 *   Selector nio Ping pong server      *
 *   Developer: Evgenii Feigin          *
 *   Reviewer: Or                       *
 *   Date: 13.08.2023                   *
 ****************************************/


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class ClientUDP {

    public static void main(String[] args) throws IOException {
        ClientUDP client = new ClientUDP();
        client.connect();
    }

    private static final Logger logger = UtilityClass.getLogger(ClientUDP.class.getName());
    private FileHandler logHandler = null;
    private static final int SERVER_PORT = 9877;
    private int msgNum = UtilityClass.getRandNumberFromTO(3,10);
    private static final int MSG_WAIT_TIME = UtilityClass.getRandNumberFromTO(3000, 10_000);
    private static final String MSG = "Ping_UDP";
    private static final int MSG_SIZE = 1024;
    ByteBuffer readBuffer = ByteBuffer.allocate(MSG_SIZE);


    public void connect() throws IOException {
        logHandler = UtilityClass.initLogger(logger, "logUdpClient.txt");

        try (DatagramChannel clientChannel = DatagramChannel.open()) {
            System.out.println("Connection to UDP server has been established...");
            logger.fine("UDP_Connection with " + SERVER_PORT + " port has been established");
            InetSocketAddress serverAddress = new InetSocketAddress("localhost", SERVER_PORT);

            while (msgNum > 0) {
                sendMsg(clientChannel, serverAddress);
                receiveMsg(clientChannel);

                Thread.sleep(MSG_WAIT_TIME);
                --msgNum;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            UtilityClass.closeLogger(logHandler);
        }
    }

    private void sendMsg(DatagramChannel clientChannel, InetSocketAddress serverAddress) throws IOException {
        clientChannel.send(ByteBuffer.wrap(MSG.getBytes()), serverAddress);
        logger.info("UDP_Message sent to Server");
    }

    private void receiveMsg(DatagramChannel clientChannel) throws IOException {
        InetSocketAddress serverResponse = (InetSocketAddress) clientChannel.receive(readBuffer);
        readBuffer.flip();
        byte[] response = new byte[readBuffer.limit()];
        readBuffer.get(response);
        logger.info("UdpMessage received from Server");
        String res = new String(response);
        System.out.println("Received from : " + serverResponse + " " + res);
    }
}

