/****************************
 *  Written by : Evgenii    *
 *  Reviewer : Daniel       *
 *  Date : 21.08.2023       *
 ****************************/

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;


public class TcpClient {

    public static void main(String[] args) throws IOException {
        TcpClient client = new TcpClient();
        client.connect();
    }

    public TcpClient() throws IOException {
    }

    private static final int SERVER_TCP_PORT = 9876;
    private static final int MSG_SIZE = 1024;
    private final SocketChannel clientChannel = SocketChannel.open();
    private volatile boolean running = true;
    public void connect() throws IOException {
        try {
            InetAddress localHost = InetAddress.getByName("localhost");
            clientChannel.connect(new InetSocketAddress(localHost, SERVER_TCP_PORT));
            System.out.println("Connection to TCP server " + clientChannel.getRemoteAddress() + " has been established...");

            Thread messageSender = new Thread(this::sendMessageThread);
            Thread messageReceiver = new Thread(this::receiveMessageThread);

            messageSender.start();
            messageReceiver.start();

            messageSender.join();
            messageReceiver.join();

            System.out.println("User disconnected from the server");
            clientChannel.close();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessageThread() {
        final Scanner scanner = new Scanner(System.in);
        String line;
        while (running) {
            line = scanner.nextLine();
            if(line.equals("logout")) {
                running = false;
            }
            ByteBuffer writeBuffer = ByteBuffer.wrap(line.getBytes());
            try {
                clientChannel.write(writeBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        scanner.close();
    }

    private void receiveMessageThread() {
        ByteBuffer readBuffer = ByteBuffer.allocate(MSG_SIZE);
        while (running) {
            try {
                int readBytes = clientChannel.read(readBuffer);
                if (-1 != readBytes) {
                    readBuffer.flip();
                    String serverAnswer = new String(readBuffer.array(), 0, readBytes);
                    readBuffer.clear();
                    System.out.println(serverAnswer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
