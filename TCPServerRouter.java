import java.net.*;
import java.io.*;

public class TCPServerRouter {

    private int PORT = 5556;
    private Socket clientSocket;
    private Object[][] RoutingTable;
    private int SockNum;
    private Boolean Running = false;
    private int ind = 0;

    /**
     * Constructor
     */
    public TCPServerRouter(){
        clientSocket = null; // socket for the thread
        RoutingTable = new Object[200][2]; // routing table
        SockNum = PORT; // port number
        Running = true;
        ind = 0; // index in the routing table
    }

    /**
     * Constructor
     * @param port
     */
    public TCPServerRouter(int port){
        clientSocket = null; // socket for the thread
        RoutingTable = new Object[200][2]; // routing table
        SockNum = port; // port number
        Running = true;
        ind = 0; // index in the routing table
    }

    /**
     * The run method
     * @throws IOException
     */
    public void run() throws IOException {

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
        while (Running) {
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