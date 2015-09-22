import java.net.*;
import java.io.*;

public class TCPServerRouter {

    private static int PORT = 5556;

    public static void main(String[] args) throws IOException {
        Socket clientSocket = null; // socket for the thread
        Object[][] RoutingTable = new Object[10][2]; // routing table
        int SockNum = PORT; // port number
        Boolean Running = true;
        int ind = 0; // index in the routing table

        //Accepting connections
        ServerSocket serverSocket = null; // server socket for accepting connections
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("ServerRouter is Listening on port: " + serverSocket.getLocalPort() + ".");
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + serverSocket.getLocalPort() + ".");
            System.exit(1);
        }

        // Creating threads with accepted connections
        while (Running == true) {
            try {
                clientSocket = serverSocket.accept();
                SThread thread = new SThread(RoutingTable, clientSocket, ind); // creates a thread with a random port
                thread.start(); // starts the thread
                ind++; // increments the index
                System.out.println("ServerRouter connected with Client/Server: " + clientSocket.getInetAddress().getHostAddress());
            } catch (IOException e) {
                System.err.println("Client/Server failed to connect.");
                System.exit(1);
            }
        }//end while

        //closing connections
        clientSocket.close();
        serverSocket.close();

    }
}