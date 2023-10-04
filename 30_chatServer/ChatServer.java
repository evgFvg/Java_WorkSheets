/****************************
 *  Written by : Evgenii    *
 *  Reviewer : Daniel       *
 *  Date : 21.08.2023       *
 ****************************/

/*
 * Implementation of Chat Server , based on nio Selector class,
 * Any new user connected has to pass through registration first ("reg name")
 * Any next message, started from the "bm user_name..." will be sent to all users connected
 * for disconnect from the chat Server "logout user_name" should be sent.
 * */

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class ChatServer {

    public static void main(String[] args) throws IOException {
        ChatServer ppServer = new ChatServer();
        ppServer.runServer();
    }

    private static final int TCP_PORT = 9876;
    private SelectorServer selectorServer;

    public void runServer() throws IOException {

        selectorServer = new SelectorServer();

        try (ServerSocketChannel tcpChannel = ServerSocketChannel.open()) {

            tcpChannel.bind(new InetSocketAddress(TCP_PORT));
            tcpChannel.configureBlocking(false);

            selectorServer.register(tcpChannel, new ChatHandler());

            System.out.println("Chat Server is listening on the TCP port " + TCP_PORT);

            Thread serverStopper = new Thread(this::stopSelector);
            serverStopper.start();

            selectorServer.run();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopSelector() {
        final Scanner scanner = new Scanner(System.in);
        while (!scanner.nextLine().equals("quit")) ; // empty on purpose
        try {
            selectorServer.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server is shutting down...");
        scanner.close();
    }
}

class ChatHandler implements Consumer<IPeer> {
    private final HashMap<String, IPeer> users = new HashMap<>();
    private final int MSG_SIZE = 1024;

    @Override
    public void accept(IPeer peer) {
        try {
            byte[] peerMsg = peer.read(MSG_SIZE);
            if (null != peerMsg) {
                String clientMsg = new String(peerMsg).trim().toLowerCase();
                String[] arr = clientMsg.split(" ", 2);
                if (!checkRegistration(clientMsg, peer)) {
                    return;
                }
                if (!arr[0].equals("reg")) {
                    handleMsg(clientMsg, peer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkRegistration(String clientMsg, IPeer peer) throws IOException {
        String name = getUsersName(clientMsg);
        if (clientMsg.equals("logout")) {
            logout(peer);
            return false;
        }
        if (users.containsValue(peer) && clientMsg.matches("^reg .*")) { // if registered user tries to reg with another name
            peer.write("Double registration is not allowed".getBytes());
            return false;
        }
        if (users.containsKey(name) && users.get(name).equals(peer)) {
            return true;
        }
        String registrationPattern = "reg\\s[0-9a-zA-Z]+";

        if (clientMsg.matches(registrationPattern)) {
            if (users.containsKey(name)) {
                peer.write("Name is occupied already. Choose another one".getBytes());
                return false;
            }
            users.put(name, peer);
            String successMsg = "User " + name + " successfully connected to chat";
            peer.write(successMsg.getBytes());
            broadcast(name, " connected to chat...");
            return true;
        } else {
            String warningMsg = "Authentication error. Only numbers or letters are allowed in the name";
            peer.write(warningMsg.getBytes());
            return false;
        }
    }

    private void logout(IPeer peer) throws IOException {
        String name = null;
        for (Map.Entry<String, IPeer> entry : users.entrySet()) {
            if (entry.getValue().equals(peer)) {
                name = entry.getKey();
            }
        }
        if (null != name) {
            broadcast("Server", "User " + name + " disconnected ");
            users.remove(name);
        }
        peer.close();
    }

    private String getUsersName(String clientMsg) throws IOException {
        int firstSpace = clientMsg.indexOf(' ');
        int secondSpace = clientMsg.indexOf(' ', firstSpace + 1);
        if (-1 != firstSpace) {
            if (secondSpace != -1) {
                return clientMsg.substring(firstSpace + 1, secondSpace);
            } else {
                return clientMsg.substring(firstSpace + 1);
            }
        } else {
            return "";
        }
    }

    private void handleMsg(String clientMsg, IPeer peer) throws IOException {
        String broadCastPattern = "^bm\\s[0-9a-zA-Z]+\\s.*$";
        String logoutPattern = "^logout\\s[0-9a-zA-Z]+$";
        String name = getUsersName(clientMsg);

        if (clientMsg.matches(broadCastPattern)) {
            String msg = clientMsg.split(" ", 3)[2];
            if (users.get(name).equals(peer)) {
                broadcast(name, msg);
                return;
            }
        } else {
            peer.write("Wrong message pattern".getBytes());
        }
//        if(clientMsg.matches(logoutPattern)){
//            peer.write("Goodbye".getBytes());
//            broadcast("Server", "User " + name + " disconnected ");
//            peer.close();
//            users.remove(getUsersName(clientMsg));
    }

    private void broadcast(String name, String msg) throws IOException {
        for (Map.Entry<String, IPeer> e : users.entrySet()) {
            if (e.getKey().equals(name)) {
                continue;
            }
            String namedMsg = name + " : " + msg;
            e.getValue().write(namedMsg.getBytes());
        }
    }

}



