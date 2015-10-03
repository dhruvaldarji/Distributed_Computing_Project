import java.io.*;
import java.net.*;

public class TCPServer {

    private static String SERVER_ROUTER_NAME = "10.99.26.144";
    private static int PORT = 5556;
    private static String CLIENT_ADDRESS = "10.99.26.144";

    // Variables for setting up connection and communication
    private static Socket Socket; // socket to connect with ServerRouter
    private static PrintWriter out; // for writing to ServerRouter
    private static BufferedReader in; // for reading form ServerRouter
    private static InetAddress addr;
    private static String host; // Server machine's IP
    private static String routerName; // ServerRouter host name
    private static int SockNum; // port number

    public static void main(String[] args) throws IOException {

        try {
            run();
        } catch (Exception e) {
            System.err.println("SOMETHING BROKE!\n" + e);
        }
    }

    private static void run() throws IOException {
        // Variables for setting up connection and communication
        Socket = null; // socket to connect with ServerRouter
        out = null; // for writing to ServerRouter
        in = null; // for reading form ServerRouter
        addr = InetAddress.getLocalHost();
        host = addr.getHostAddress(); // Server machine's IP
        routerName = SERVER_ROUTER_NAME; // ServerRouter host name
        SockNum = PORT; // port number

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

        //System.exit(0);
    }

}
