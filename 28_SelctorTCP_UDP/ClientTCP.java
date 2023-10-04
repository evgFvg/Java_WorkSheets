/****************************************
 *   Selector nio Ping pong server      *
 *   Developer: Evgenii Feigin          *
 *   Reviewer: Or                       *
 *   Date: 13.08.2023                   *
 ****************************************/


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class ClientTCP {

    public static void main(String[] args) throws IOException {
        ClientTCP client = new ClientTCP();
        client.connect();
    }

    private static final Logger logger = UtilityClass.getLogger(ClientTCP.class.getName());
    private FileHandler logHandler = null;
    private static final int SERVER_PORT = 9876;
    private int msgNum = UtilityClass.getRandNumberFromTO(3,10);
    private static final int MSG_WAIT_TIME = UtilityClass.getRandNumberFromTO(3000,10_000);

    private static final String MSG = "Ping_TCP";
    private static final int MSG_SIZE = 1024;
    private static final ByteBuffer writeBuffer = ByteBuffer.wrap(MSG.getBytes());
    private static final ByteBuffer readBuffer = ByteBuffer.allocate(MSG_SIZE);

    public void connect() throws IOException {
        logHandler = UtilityClass.initLogger(logger,"logTcpClient.txt");

        try (SocketChannel clientChannel = SocketChannel.open()) {
            InetAddress localHost = InetAddress.getByName("localhost");
            clientChannel.connect(new InetSocketAddress(localHost, SERVER_PORT));
            System.out.println("Connection to TCP server has been established...");
            logger.fine("Connection with " + clientChannel + "has been established");

            while (msgNum > 0) {
                sendMsg(clientChannel);
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

    private void sendMsg(SocketChannel clientChannel) throws IOException {
        clientChannel.write(writeBuffer);
        logger.info("Message sent to Server");
        writeBuffer.clear();
    }

    private void receiveMsg(SocketChannel clientChannel) throws IOException {
        int readBytes = clientChannel.read(readBuffer);
        logger.info("Message received from Server");
        readBuffer.flip();
        String receivedMsg = new String(readBuffer.array(), 0, readBytes);
        readBuffer.clear();
        System.out.println(receivedMsg);
    }
}
