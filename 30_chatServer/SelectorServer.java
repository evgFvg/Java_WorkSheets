/****************************
 *  Written by : Evgenii    *
 *  Reviewer : Mor          *
 *  Date : 19.08.2023       *
 ****************************/

/*
 * Implementation of Server Infrastructure, based on nio Selector class.
 * Allows to register connections of any type ( tcp / udp)
 * */

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class SelectorServer {

    private final Selector selector = Selector.open();
    private final HashMap<Object, IPeer> channelMap = new HashMap<>();
    private boolean running = true;
    private static final int TIMEOUT = 10_000;

    public SelectorServer() throws IOException {
    }

    public void run() throws IOException {
        try {
            while (running) {
                int nChannels = selector.select();
                if (0 == nChannels) {
                    //Some logging
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    if (key.isAcceptable()) {
                        regTcpChannel(key);
                    }
                    if (key.isReadable()) {
                        launchHandler(key);
                    }
                    iter.remove();
                }
            }
            selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void launchHandler(SelectionKey key) {
        ((Consumer<IPeer>) key.attachment()).accept(channelMap.get(key.channel()));
    }

    private void regTcpChannel(SelectionKey key) throws IOException {
        ServerSocketChannel tcpChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = tcpChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ, key.attachment());
        channelMap.put(clientChannel, new TcpPeer(clientChannel));

    }

    private static class TcpPeer implements IPeer {
        private final SocketChannel clientChannel;
        private final SocketAddress clientRemoteAddress;

        TcpPeer(SocketChannel clientChannel) throws IOException {
            this.clientChannel = clientChannel;
            this.clientRemoteAddress = clientChannel.getRemoteAddress();
        }

        @Override
        public byte[] read(int bytesToRead) throws IOException {
            ByteBuffer readBuffer = ByteBuffer.allocate(bytesToRead);
            int readBytes = clientChannel.read(readBuffer);
            if (readBytes == -1) {
                this.close();
                return null;
            }
            readBuffer.flip();
            return readBuffer.array();
        }

        @Override
        public void write(byte[] data) throws IOException {
            ByteBuffer res = ByteBuffer.allocate(data.length);
            res.put(data);
            res.rewind();
            clientChannel.write(res);
        }

        @Override
        public void close() throws IOException {
            System.out.println("Tcp Client " + clientRemoteAddress + " has been disconnected");
            clientChannel.close();
        }
    }

    private static class UdpPeer implements IPeer {
        private final DatagramChannel udpChannel;
        private InetSocketAddress clientRemoteAddress;

        UdpPeer(DatagramChannel udpChannel) {
            this.udpChannel = udpChannel;
        }

        @Override
        public byte[] read(int bytesToRead) throws IOException {
            byte[] resBuffer = null;
            ByteBuffer readBuffer = ByteBuffer.allocate(bytesToRead);
            clientRemoteAddress = (InetSocketAddress) udpChannel.receive(readBuffer);
            if (null != clientRemoteAddress) {
                readBuffer.flip();
                resBuffer = new byte[readBuffer.limit()];
                readBuffer.get(resBuffer);
            }

            return resBuffer;
        }

        @Override
        public void write(byte[] data) throws IOException {
            ByteBuffer readBuffer = ByteBuffer.wrap(data);
            readBuffer.put(data);
            readBuffer.rewind();
            udpChannel.send(readBuffer, clientRemoteAddress);
        }

        @Override
        public void close() throws IOException {
            System.out.println("UDP_Client " + clientRemoteAddress + " has been disconnected");
            udpChannel.close();
        }
    }

    public void register(Object channel, Consumer<IPeer> handler) throws IOException {
        if (!(channel instanceof AbstractSelectableChannel currChannel)) {
            throw new ClassCastException("Provided channel neither TCP or UDP");
        }
        if (currChannel instanceof ServerSocketChannel) {
            currChannel.register(selector, SelectionKey.OP_ACCEPT, handler);
        }
        if (currChannel instanceof DatagramChannel) {
            currChannel.register(selector, SelectionKey.OP_READ, handler);
            channelMap.put(channel, new UdpPeer((DatagramChannel) currChannel));
        }
    }

    public void stop() throws IOException {
        for (Map.Entry<Object, IPeer> entry : channelMap.entrySet()) {
            ((AbstractSelectableChannel) entry.getKey()).close();
        }
        channelMap.clear();
        running = false;
    }
}
