import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class SThread extends Thread {
    private HashMap<String, RoutingInfo> RTable; // Dynamic Routing Table
    private PrintWriter out, outTo, subnetOut; // writers (for writing back to the machine and to destination)
    private BufferedReader in, subnetIn; // reader (for reading from the machine connected to)
    private String inputLine, outputLine, destination, addr; // communication strings
    private Socket outSocket; // socket for communicating with a destination
    private int defaultPort = 5556;
    private String name;
    boolean IM_A_FUCKING_ROUTER = false;

    // Constructor
    SThread(HashMap<String, RoutingInfo> Table, RoutingInfo clientInfo) throws IOException {
        in = new BufferedReader(new InputStreamReader(clientInfo.getClient().getInputStream()));
        out = new PrintWriter(clientInfo.getClient().getOutputStream(), true);

        RTable = Table;
        addr = clientInfo.getClient().getInetAddress().getHostAddress();

        //Get Our Name yo
        name = in.readLine();

        // This is a router.
        if (name.startsWith("R")) {
            System.out.println("Router "+ addr +" is connected.");
            IM_A_FUCKING_ROUTER = true;
        }
        // This is a client/server
        else {
            clientInfo.setName(name);
            RTable.put(clientInfo.getName(), clientInfo); // sockets for communication
        }
    }

    // Run method (will run for each machine that connects to the ServerRouter)
    public void run() {

        if (IM_A_FUCKING_ROUTER) {
            router();
        } else {
            runOtherBitches();
        }

    }

    private void router() {
        try {

            // find out who they are trying to look for.
            destination = in.readLine();
            System.out.println("Router " + name + " is looking for " + destination);
            out.println("Connected; Searching subnet for " + destination + ",");


            // waits 10 seconds to let the routing table fill with all machines' information
            try {
                Thread.currentThread().sleep(10000);
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted");
            }

            RoutingInfo r = null;
            boolean clientFound = false;

            // Check if key exists within our table
            if (RTable.containsKey(destination)) {
                // Local client found.
                clientFound = true;

                out.println("RingADingDing");

                r = RTable.get(destination);
                r.setInUse(true);

                outSocket = r.getClient(); // gets the socket for communication from the table
                subnetIn = new BufferedReader(new InputStreamReader(outSocket.getInputStream()));
                subnetOut = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
                System.out.println("Found destination: " + destination);
            }
            else {
                out.println("Bye Bye Bye");
            }

            // Client found.
            // Communication loop
            if (clientFound) {
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Client/Server said: " + inputLine);
                    if (inputLine.equals("Bye.")) { // exit statement
                        subnetOut.println("Bye.");
                        out.println("Bye.");
                        r.setInUse(false);
                        break;
                    }

                    // passes the input from the machine to the output string for the destination
                    outputLine = inputLine;

                    if (outSocket != null) {
                        subnetOut.println(outputLine); // writes to the destination
                    }
                }// end while
            }


        } catch (IOException e) {
            System.err.println("Could not listen to socket.");
            out.println("Bye.");
            System.exit(1);
        }

    }

    private void runOtherBitches() {
        try {
            // Initial sends/receives
            destination = in.readLine(); // initial read (the destination for writing)
            System.out.println("Forwarding " + name + " to " + destination);
            out.println("Connected to the router."); // confirmation of connection

            subnetOut = null; // writers (for writing back to the machine and to destination)
            subnetIn = null; // reader (for reading from the machine connected to)

            // waits 10 seconds to let the routing table fill with all machines' information
            try {
                Thread.currentThread().sleep(10000);
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted");
            }

            RoutingInfo r = null;
            boolean clientFound = false;

            // Check if key exists within our table
            if (RTable.containsKey(destination)) {
                // Local client found.
                clientFound = true;

                r = RTable.get(destination);
                r.setInUse(true);

                outSocket = r.getClient(); // gets the socket for communication from the table
                outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
                System.out.println("Found destination: " + destination);
            }

            // Else check if key exists within subnet.
            if (!clientFound) {

                System.out.println("Looking through Subnet...");
                for(String rName : RTable.keySet()){
                    if(!clientFound && RTable.get(rName).isRouter() && !(RTable.get(rName).getIPAddress().equals(InetAddress.getLocalHost().toString()))){
                        RoutingInfo router = RTable.get(rName);
                         // Tries to connect to the ServerRouter
                        try {
                            router.setClient(new Socket(router.getIPAddress(), defaultPort));

                            subnetIn = new BufferedReader(new InputStreamReader(router.getClient().getInputStream()));
                            subnetOut = new PrintWriter(router.getClient().getOutputStream(), true);

                            subnetOut.println("Router Motherfucker!");
                            subnetOut.println(destination);

                            String result = subnetIn.readLine();
                            if (result.equals("RingADingDing")){
                                System.out.println("Subnet: "+result);
                                clientFound = true;
                                break;
                            }

                        } catch (Exception e) {
                            System.out.println("Router at "+router.getIPAddress()+" is unavailable. Moving on...");
                        }

                    }
                }
            }

            // Client found.
            // Communication loop
            if (clientFound) {
                while ((inputLine = subnetIn.readLine()) != null) {
                    System.out.println("Client/Server said: " + inputLine);
                    if (inputLine.equals("Bye.")) { // exit statement
                        subnetOut.println("Bye.");
                        out.println("Bye.");
                        r.setInUse(false);
                        break;
                    }
                    outputLine = inputLine; // passes the input from the machine to the output string for the destination

                    if (outSocket != null) {
                        outTo.println(outputLine); // writes to the destination
                    }
                }// end while
            }

        }// end try
        catch (IOException e) {
            System.err.println("Could not listen to socket.");
            out.println("Bye.");
            System.exit(1);
        }
    }
}