import java.net.*;
import java.io.*;
import java.util.HashMap;

public class TCPServerRouter {

    private Socket clientSocket;
    private HashMap<String, RoutingInfo> RoutingTable;
    private int SockNum;
    private Boolean Running = false;

    /**
     * Constructor
     */
    public TCPServerRouter(){
        clientSocket = null; // socket for the thread
        RoutingTable = new HashMap<>(); // routing table
        SockNum = 5556; // port number
        Running = true;
    }

    /**
     * Constructor
     * @param port : Port
     * @param SubnetList : The list of routers in the known subnet
     */
    public TCPServerRouter(int port, HashMap<String, RoutingInfo> SubnetList){
        clientSocket = null; // socket for the thread
        RoutingTable = new HashMap<>(); // routing table
        SockNum = port; // port number
        Running = true;

        for(String name : SubnetList.keySet()){
            RoutingTable.put(name, SubnetList.get(name));
            System.out.println("Router " + name + " was added to the Subnet.");
        }
    }


    /**
     * The run method
     * @throws IOException
     */
    public void run() throws IOException {

        //Accepting connections
        ServerSocket serverSocket = null; // server socket for accepting connections
        try {
            serverSocket = new ServerSocket(SockNum);
            System.out.println("ServerRouter is Listening on port: " + serverSocket.getLocalPort() + ".");
        } catch (IOException e) {
            if (serverSocket != null) {
                System.err.println("Could not listen on port: " + serverSocket.getLocalPort() + ".");
            }
            else {
                System.err.println("Port is null.");
            }
            System.exit(1);
        }

        // Creating threads with accepted connections
        while (Running) {
            try {
                clientSocket = serverSocket.accept();
                // creates a thread with a random port
                SThread thread = new SThread(InetAddress.getLocalHost().getHostAddress(), SockNum,  RoutingTable, new RoutingInfo(clientSocket, clientSocket.getInetAddress().getHostAddress()));
                thread.start(); // starts the thread
                System.out.println("ServerRouter connected with Client/Server: "
                        + clientSocket.getInetAddress().getHostAddress()
                        + ":" + clientSocket.getLocalPort());
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