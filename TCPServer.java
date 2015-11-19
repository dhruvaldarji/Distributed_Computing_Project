import java.io.*;
import java.net.*;

public class TCPServer {

    // Variables for setting up connection and communication
    private Socket Socket; // socket to connect with ServerRouter
    private PrintWriter out; // for writing to ServerRouter
    private BufferedReader in; // for reading form ServerRouter
    private String routerName; // ServerRouter host name
    private int SockNum; // port number

    private String MyName;
    private String ClientName;

    /**
     * Constructor
     * @param serverRouterName : Server Router Name
     * @param port : Rouer Port
     * @param myName : My Name
     * @param clientName : The Client Name
     * @throws IOException
     */
    public TCPServer(String serverRouterName, int port, String myName, String clientName) throws IOException{

        // Variables for setting up connection and communication
        Socket = null; // socket to connect with ServerRouter
        out = null; // for writing to ServerRouter
        in = null; // for reading form ServerRouter
//        InetAddress addr = InetAddress.getLocalHost();
//        String host = addr.getHostAddress();
        routerName = serverRouterName; // ServerRouter host name
        SockNum = port; // port number

        MyName = myName;
        ClientName = clientName;
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

        // Communication process (initial sends/receives)
        out.println(MyName);// initial send (Our Name)
        out.println(ClientName);// secondary send (IP of the destination Client)
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
