import java.io.*;
import java.net.*;

public class TCPServer {
    private String SERVER_ROUTER_NAME;
    private int PORT;
    private String CLIENT_ADDRESS;

    // Variables for setting up connection and communication
    private Socket Socket; // socket to connect with ServerRouter
    private PrintWriter out; // for writing to ServerRouter
    private BufferedReader in; // for reading form ServerRouter
    private InetAddress addr;
    private String host; // Server machine's IP
    private String routerName; // ServerRouter host name
    private int SockNum; // port number

    /**
     * Constructor
     * @throws IOException
     */
    public TCPServer() throws IOException{
        // Variables
        SERVER_ROUTER_NAME = "10.99.26.144";
        PORT = 5556;
        CLIENT_ADDRESS = "10.99.26.144";


        // Variables for setting up connection and communication
        Socket = null; // socket to connect with ServerRouter
        out = null; // for writing to ServerRouter
        in = null; // for reading form ServerRouter
        addr = InetAddress.getLocalHost();
        host = addr.getHostAddress(); // Server machine's IP
        routerName = SERVER_ROUTER_NAME; // ServerRouter host name
        SockNum = PORT; // port number
    }

    /**
     * Constructor
     * @param serverRouterName
     * @param port
     * @param clientAddress
     * @throws IOException
     */
    public TCPServer(String serverRouterName, int port, String clientAddress) throws IOException{
        // Variables
        SERVER_ROUTER_NAME = serverRouterName;
        PORT = port;
        CLIENT_ADDRESS = clientAddress;


        // Variables for setting up connection and communication
        Socket = null; // socket to connect with ServerRouter
        out = null; // for writing to ServerRouter
        in = null; // for reading form ServerRouter
        addr = InetAddress.getLocalHost();
        host = addr.getHostAddress(); // Server machine's IP
        routerName = SERVER_ROUTER_NAME; // ServerRouter host name
        SockNum = PORT; // port number
    }

    /**
     * This is the run method.
     * @throws IOException
     */
    public void run() throws IOException {

        // Tries to connect to the ServerRouter
        try {
            Socket = new Socket(routerName, SockNum);
            out = new PrintWriter(Socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about router: " + routerName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + routerName);
            System.exit(1);
        }

        // Variables for message passing
        String fromServer; // messages sent to ServerRouter
        String fromClient; // messages received from ServerRouter
        String address = CLIENT_ADDRESS; // destination IP (Client)

        // Communication process (initial sends/receives)
        out.println(address);// initial send (IP of the destination Client)
        fromClient = in.readLine();// initial receive from router (verification of connection)
        System.out.println("ServerRouter: " + fromClient);

        // Communication while loop
        while ((fromClient = in.readLine()) != null) {
            System.out.println("Client said: " + fromClient);
            if (fromClient.equals("Bye.")){
                out.println("Bye.");
                break;      // exit statement
            }

            fromServer = fromClient.toUpperCase(); // converting received message to upper case
            System.out.println("Server said: " + fromServer);
            out.println(fromServer); // sending the converted message back to the Client via ServerRouter
        }

        // closing connections
        out.close();
        in.close();
        Socket.close();
    }

}
