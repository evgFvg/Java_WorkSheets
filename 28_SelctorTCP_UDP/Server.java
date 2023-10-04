/****************************************
 *   Selector nio Ping pong server      *
 *   Developer: Evgenii Feigin          *
 *   Reviewer: Or                       *
 *   Date: 13.08.2023                   *
 ****************************************/

/*
 * Simple implementation of TCP / UDP Server , that is listening on two ports
 * each "Ping" received from Client (Both UDP and TCP) accompanied by "Pong" sent.
 * In case of receiving "quit" message  - the server shuts down
 * */

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Server {

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startServer();
    }

    private static final int TCP_PORT = 9876;
    private static final int UDP_PORT = 9877;
    private static final int TIMEOUT = 7000;
    private static final int MSG_SIZE = 1024;
    private boolean running = true;
    private static final String MSG = "Pong";
    private static final Logger serverLogger = Logger.getLogger(Server.class.getName());
    private FileHandler logHandler = null;

    private static final ByteBuffer tcpReadBuffer = ByteBuffer.allocate(MSG_SIZE);
    private static final ByteBuffer tcpWriteBuffer = ByteBuffer.wrap(MSG.getBytes());
    private static final ByteBuffer udpReadBuffer = ByteBuffer.allocate(MSG_SIZE);
    private static final ByteBuffer udpWriteBuffer = ByteBuffer.wrap(MSG.getBytes());


    public void startServer() throws IOException {

        try (Selector selector = Selector.open();
             ServerSocketChannel tcpServerSocket = ServerSocketChannel.open();
             DatagramChannel udpChannel = DatagramChannel.open()) {

            initServer(tcpServerSocket, udpChannel, selector);
            logHandler = UtilityClass.initLogger(serverLogger, "logServer.txt");

            while (running) {
                int readyForIO = selector.select(TIMEOUT);
                if (0 == readyForIO) {
                    serverLogger.info("Nobody connected for the last " + TIMEOUT / 1000 + " seconds");
                    continue;
                }
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    if (key.isAcceptable()) {
                        registerServerChannel(key, selector, tcpServerSocket);
                    }
                    if (key.isReadable()) {
                        receiveMsg(key);
                    }
                    iter.remove();
                }
            }
            serverLogger.info("Server is shutting down");
            UtilityClass.closeLogger(logHandler);
        }
    }

    private void initServer(ServerSocketChannel serverSocket, DatagramChannel udpChannel, Selector selector) throws IOException {
        serverSocket.bind(new InetSocketAddress("localHost", TCP_PORT));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("TCP_Server is listening on port " + TCP_PORT);

        udpChannel.bind(new InetSocketAddress(UDP_PORT));
        udpChannel.configureBlocking(false);
        udpChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("UDP_Server is listening on port " + UDP_PORT);

        new Thread(this::stdInScanner).start();
    }


    private void receiveMsg(SelectionKey key) throws IOException {
        if (key.channel() instanceof SocketChannel) {
            handleTcpChannel(key);
        } else {
            handleUdpChannel(key);
        }
    }

    private void handleUdpChannel(SelectionKey key) throws IOException {
        DatagramChannel udpChannel = (DatagramChannel) key.channel();
        udpReadBuffer.clear();
        InetSocketAddress clientAddress = (InetSocketAddress) udpChannel.receive(udpReadBuffer);

        if (null != clientAddress) {
            udpReadBuffer.flip();
            byte[] receivedMsg = new byte[udpReadBuffer.limit()];
            udpReadBuffer.get(receivedMsg);
            String msg = new String(receivedMsg);

            System.out.println("Received from " + clientAddress + " " + msg);
            udpChannel.send(udpWriteBuffer, clientAddress);
        }
    }

    private void handleTcpChannel(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        int readBytes = clientChannel.read(tcpReadBuffer);
        if (readBytes == -1) {
            clientChannel.close();
            System.out.println("TCP client channel has been closed");
            return;
        }
        tcpReadBuffer.flip();
        String receivedMsg = new String(tcpReadBuffer.array(), 0, readBytes);
        tcpReadBuffer.clear();

        System.out.println("Received from " + clientChannel.getRemoteAddress() + " " + receivedMsg);
        clientChannel.write(tcpWriteBuffer);
        tcpWriteBuffer.clear();
    }

    private void registerServerChannel(SelectionKey key, Selector selector, ServerSocketChannel serverSocket) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
    }

    private void stdInScanner() {
        Scanner scanner = new Scanner(System.in);
        while (!scanner.nextLine().equals("quit")); // empty on purpose
        running = false;
        System.out.println("Server is shutting down...");
        scanner.close();
    }


}
