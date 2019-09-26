import java.net.*;
import java.io.*;


/**
 * A thread dedicated to a specific client that listens for their input and notifies the other servers to write
 * the received message to the other clients
 * @author Kevin Liu, Blake Wesel, Kim Ngo, Coleman Link, Austin Trumble
 * @version 2019-03-01
 */

public class ThreadedServer extends Thread {

    Socket socket;
    Server server;
    BufferedReader br = null;
    PrintWriter pw = null;
    int user_number;

    /**
     * Saves the socket and the main server object to be referenced later
     * @param socket - The socket the client connected to
     * @param server - The main server object that has access to all the threaded individual servers
     */
    public ThreadedServer(Socket socket, Server server, int user_number) {
        this.socket = socket;
        this.server = server;
        setName("User " + user_number);
    }

    /**
     * Reads the input for the client to the server and calls a method that sends it to all the clients
     */
    public void run() {
        while(true) {
            try {
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                pw = new PrintWriter(socket.getOutputStream());
                String line = br.readLine();

                //System.out.println(line);
                server.writeToAllServers(getName() + ": " + line);

            } catch (Exception e) {
                server.disconnect(this);
                break;
            }
        }
    }
}
