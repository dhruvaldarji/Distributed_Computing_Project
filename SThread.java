import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class SThread extends Thread {
    private HashMap<String, RoutingInfo> RTable; // Dynamic Routing Table
    private PrintWriter out, outTo; // writers (for writing back to the machine and to destination)
    private BufferedReader in; // reader (for reading from the machine connected to)
    private String inputLine, outputLine, destination, addr; // communication strings
    private Socket outSocket; // socket for communicating with a destination
    private String name;

    // Constructor
    SThread(HashMap<String, RoutingInfo> Table, RoutingInfo clientInfo) throws IOException {
        out = new PrintWriter(clientInfo.getClient().getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientInfo.getClient().getInputStream()));
        RTable = Table;
        addr = clientInfo.getClient().getInetAddress().getHostAddress();

        //Get Our Name yo
        name = in.readLine();
        clientInfo.setName(name);

        RTable.put(clientInfo.getName(), clientInfo); // sockets for communication
    }

    // Run method (will run for each machine that connects to the ServerRouter)
    public void run() {
        try {
            // Initial sends/receives
            destination = in.readLine(); // initial read (the destination for writing)
            System.out.println("Forwarding "+ name + " to " + destination);
            out.println("Connected to the router."); // confirmation of connection

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
                System.out.println("Found destination: " + destination);
                outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
            }

            // Else check if key exists within subnet.
            if (!clientFound) {
                System.out.println("I should be checking the subnet but I can't just yet.");
            }

            // Client found.
            // Communication loop
            if(clientFound){
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Client/Server said: " + inputLine);
                    if (inputLine.equals("Bye.")) { // exit statement
                        outTo.println("Bye.");
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