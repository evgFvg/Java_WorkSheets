/****************************
 *  Written by : Evgenii    *
 *  Reviewer : Mor          *
 *  Date : 19.08.2023       *
 ****************************/

/*
 * Implementation of Server Infrastructure, based on nio Selector class,
 *  that allows to register connections of any type ( tcp / udp) in the selector,
 * with business logic implemented in IPeer class successor (TCP_Peer / UDP_Peer)
 * */

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;
import java.util.Scanner;
import java.util.function.Consumer;

public class TesterPingPongServer {

    public static void main(String[] args) throws IOException {
        TesterPingPongServer ppServer = new TesterPingPongServer();
        ppServer.runServer();
    }

    private static final int UDP_PORT = 9877;
    private static final int TCP_PORT = 9876;
    private SelectorServer selectorServer;

    public void runServer() throws IOException {

        selectorServer = new SelectorServer();

        try (ServerSocketChannel tcpChannel = ServerSocketChannel.open();
             DatagramChannel udpChannel = DatagramChannel.open()) {

            tcpChannel.bind(new InetSocketAddress(TCP_PORT));
            tcpChannel.configureBlocking(false);

            udpChannel.bind(new InetSocketAddress(UDP_PORT));
            udpChannel.configureBlocking(false);

            selectorServer.register(tcpChannel, new TcpPeer());
            selectorServer.register(udpChannel, new UdpPeer());

            System.out.println("Ping Pong Server is listening on the TCP port " + TCP_PORT);
            System.out.println("Ping Pong Server is listening on the UDP port " + UDP_PORT);

            Thread serverStopper = new Thread(this::stopSelector);
            serverStopper.start();

            selectorServer.run();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopSelector() {
        final Scanner scanner = new Scanner(System.in);
        while (!scanner.nextLine().equals("quit")); // empty on purpose
        try {
            selectorServer.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server is shutting down...");
        scanner.close();
    }
}

class TcpPeer implements Consumer<IPeer> {
    @Override
    public void accept(IPeer peer) {
        try {
            byte[] peerMsg = peer.read(8);
            if(null != peerMsg) {
                System.out.println(new String(peerMsg));
                peer.write("TcpPong".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

class UdpPeer implements Consumer<IPeer> {
    @Override
    public void accept(IPeer peer) {
        try {
            byte[] peerMsg = peer.read(8);
            System.out.println(new String(peerMsg));
            peer.write("UdpPong".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

