import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * A class that creates a server that allows multiple clients to connect and send messages to multiple clients
 */

public class Server {

    int user_count = 0;
    Vector<ThreadedServer> servers = new Vector<>();

    public Server() {
        try {
            ServerSocket ss = new ServerSocket(16789);
            Socket socket = null;

            while(true) {
                socket = ss.accept();
                System.out.println("Got a connection");
                user_count++;
                ThreadedServer ts = new ThreadedServer(socket, this, user_count);
                ts.start();
                servers.add(ts);
            }
        } catch(IOException io) {
            System.out.println("IO Exception occurred");
        }
    }

    /**
     * Sends a message to all the connected clients
     * @param message - The message to send to all the clients
     */
    public void writeToAllServers(String message) {
        if(!message.equals("")) {
            for (int i = 0; i < servers.size(); i++) {
                ThreadedServer ts = servers.get(i);
                ts.pw.println(message);
                // System.out.println("sending the message: " + message);
                ts.pw.flush();
            }
        }
    }

    public void disconnect(ThreadedServer ts) {
        servers.remove(ts);
        ts.interrupt();
        System.out.println("A user disconnected");
    }

    /**
     * Creates a new Server object which starts the program and creates the server
     * @param args
     */
    public static void main(String[] args) {
        new Server();
    }

}
