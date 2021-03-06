import java.io.*;
import java.net.*;

public class TCPClient {

    private Stats stat;

    // Variables for setting up connection and communication
    private Socket Socket;// socket to connect with ServerRouter
    private PrintWriter out; // for writing to ServerRouter
    private BufferedReader in; // for reading form ServerRouter
    private String NAME;
    private String ServerName;
    private String host; // Client machine's IP
    private String routerName; // ServerRouter host name
    private int SockNum; // port number
    private String INPUT_FILE;
    private long connectionTime;
    private long efficiency;
    private long initialTime;
    private long sentTime;
    private long dataTransferTime;


    /**
     * Constructor
     * @param router : The local Router
     * @param port : The Router Port
     * @param clientName : Our Name
     * @param serverName: ServerName
     * @param inputFile : The input file to use.
     * @throws IOException
     */
    public TCPClient(String router, int port, String clientName, String serverName, String inputFile) throws IOException {
        stat = new Stats();

        // Variables for setting up connection and communication
        Socket = null; // socket to connect with ServerRouter
        out = null; // for writing to ServerRouter
        in = null; // for reading form ServerRouter
        InetAddress addr = InetAddress.getLocalHost();
        host = addr.getHostAddress(); // Client machine's IP
        routerName = router; // ServerRouter host name
        SockNum = port; // port number

        NAME = clientName;
        ServerName = serverName;
        INPUT_FILE = inputFile;
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
        long t0, t1, t;

        // Communication process (initial sends/receives
        out.println(NAME);// initial send (Our Name)
        out.println(ServerName);// secondary send (IP of the destination Server)
        fromServer = in.readLine();//initial receive from router (verification of connection)
        System.out.println("ServerRouter: " + fromServer);
        out.println("It's Bob Ross Time!");
        boolean init = true;
        t0 = System.currentTimeMillis();

        // Communication while loop
        while ((fromServer = in.readLine()) != null) {
            System.out.println("Server: " + fromServer);

            if(!init){
                stat.getTransmissionInSizes().add(fromServer.length());
            }

            t1 = System.currentTimeMillis();

            t = t1 - t0;

            // If this is the initial send.
            if(init){
                connectionTime = t;
                stat.setConnectionTime(connectionTime);
                init = false;
            }
            else{
                stat.getTransmissionTimes().add(t);
            }

            System.out.println("Cycle time: " + t);

            if (fromServer.equals("Bye.")) {// exit statement
                break;
            }

            fromUser = fromFile.readLine(); // reading strings from a file
            if (fromUser != null) {
                System.out.println("Client: " + fromUser);
                stat.getTransmissionOutSizes().add(fromUser.length());
                initialTime = System.currentTimeMillis();
                out.println(fromUser); // sending the strings to the Server via ServerRouter
                sentTime = System.currentTimeMillis();
                t0 = System.currentTimeMillis();
                dataTransferTime = sentTime - initialTime;
                if(dataTransferTime<1){
                    // The data transferred in less than a second
                    // So we will just mark it as a 100% effecient send.

                    dataTransferTime = 1;
                }
                efficiency = fromUser.length() / dataTransferTime;
                stat.getEfficiencies().add(efficiency);
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
