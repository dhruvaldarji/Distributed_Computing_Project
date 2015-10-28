import java.io.*;
import java.net.*;

public class TCPClient {

    private String ROUTER_NAME;
    private int PORT;
    private String SERVER_ADDRESS;
    private String INPUT_FILE;

    private Stats stat;

    // Variables for setting up connection and communication
    private Socket Socket;// socket to connect with ServerRouter
    private PrintWriter out; // for writing to ServerRouter
    private BufferedReader in; // for reading form ServerRouter
    private InetAddress addr;
    private String host; // Client machine's IP
    private String routerName; // ServerRouter host name
    private int SockNum; // port number

    /**
     * Constructor
     * @throws IOException
     */
    public TCPClient() throws IOException {
        ROUTER_NAME = "10.99.19.171";
        PORT = 5556;
        SERVER_ADDRESS = "10.99.25.224";
        INPUT_FILE = "src/file.txt";


        stat = new Stats();

        // Variables for setting up connection and communication
        Socket = null; // socket to connect with ServerRouter
        out = null; // for writing to ServerRouter
        in = null; // for reading form ServerRouter
        addr = InetAddress.getLocalHost();
        host = addr.getHostAddress(); // Client machine's IP
        routerName = ROUTER_NAME; // ServerRouter host name
        SockNum = PORT; // port number
    }

    /**
     * Constructor
     * @param router
     * @param port
     * @param serverAddress
     * @param inputFile
     * @throws IOException
     */
    public TCPClient(String router, int port, String serverAddress, String inputFile) throws IOException {
        ROUTER_NAME = router;
        PORT = port;
        SERVER_ADDRESS = serverAddress;
        INPUT_FILE = inputFile;


        stat = new Stats();

        // Variables for setting up connection and communication
        Socket = null; // socket to connect with ServerRouter
        out = null; // for writing to ServerRouter
        in = null; // for reading form ServerRouter
        addr = InetAddress.getLocalHost();
        host = addr.getHostAddress(); // Client machine's IP
        routerName = ROUTER_NAME; // ServerRouter host name
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
