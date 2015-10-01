import java.io.*;
import java.net.*;

public class TCPClient {

    private static String ROUTER_NAME = "10.99.19.171";
    private static int PORT = 5556;
    private static String SERVER_ADDRESS = "10.99.25.224";
    private static String INPUT_FILE = "src/file.txt";

    private static Stats stat;

    // Variables for setting up connection and communication
    private static Socket Socket;// socket to connect with ServerRouter
    private static PrintWriter out; // for writing to ServerRouter
    private static BufferedReader in; // for reading form ServerRouter
    private static InetAddress addr;
    private static String host; // Client machine's IP
    private static String routerName; // ServerRouter host name
    private static int SockNum; // port number

    public static void main(String[] args) {

        try{
            run();
        } catch (Exception e){
            System.err.println("SOMETHING BROKE!\n" + e);
        }

        System.exit(0);
    }

    private static void run() throws IOException {

        stat = new Stats();

        // Variables for setting up connection and communication
        Socket = null; // socket to connect with ServerRouter
        out = null; // for writing to ServerRouter
        in = null; // for reading form ServerRouter
        addr = InetAddress.getLocalHost();
        host = addr.getHostAddress(); // Client machine's IP
        routerName = ROUTER_NAME; // ServerRouter host name
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
        Reader reader = new FileReader(INPUT_FILE);
        BufferedReader fromFile = new BufferedReader(reader); // reader for the string file
        String fromServer; // messages received from ServerRouter
        String fromUser; // messages sent to ServerRouter
        String address = SERVER_ADDRESS; // destination IP (Server)
        long t0, t1, t;

        // Communication process (initial sends/receives
        out.println(address);// initial send (IP of the destination Server)
        fromServer = in.readLine();//initial receive from router (verification of connection)
        System.out.println("ServerRouter: " + fromServer);
        out.println(host); // Client sends the IP of its machine as initial send
        t0 = System.currentTimeMillis();

        // Communication while loop
        while ((fromServer = in.readLine()) != null) {
            System.out.println("Server: " + fromServer);
            stat.getTransmissionInSizes().add(fromServer.length());
            t1 = System.currentTimeMillis();
            if (fromServer.equals("Bye.")) {// exit statement
                break;
            }

            t = t1 - t0;
            stat.getTransmissionTimes().add(t);

            System.out.println("Cycle time: " + t);

            fromUser = fromFile.readLine(); // reading strings from a file
            if (fromUser != null) {
                System.out.println("Client: " + fromUser);
                stat.getTransmissionOutSizes().add(fromUser.length());
                out.println(fromUser); // sending the strings to the Server via ServerRouter
                t0 = System.currentTimeMillis();
            }
        }

        stat.ComputeAverages();

        System.out.println("\n"+stat.toString());


        // closing connections
        out.close();
        in.close();
        Socket.close();
    }
}
