import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class SThread extends Thread {
    private HashMap<String, RoutingInfo> RTable; // Dynamic Routing Table
    private PrintWriter out, subnetOut; // writers (for writing back to the machine and to destination)
    private BufferedReader in, subnetIn; // reader (for reading from the machine connected to)
    private String inputLine, outputLine, destination; // communication strings
    private Socket outSocket; // socket for communicating with a destination
    private String ServerAddress;
    private int ServerPort;
    private String name;
    private RoutingInfo ClientInfo;

    // Constructor
    SThread(String sAddress, int sPort, HashMap<String, RoutingInfo> Table, RoutingInfo clientInfo) throws IOException {
        in = new BufferedReader(new InputStreamReader(clientInfo.getClient().getInputStream()));
        out = new PrintWriter(clientInfo.getClient().getOutputStream(), true);

        ServerAddress = sAddress;
        ServerPort = sPort;

        RTable = Table;
        ClientInfo = clientInfo;
        ClientInfo.setPort(clientInfo.getClient().getLocalPort());

    }

    // Run method (will run for each machine that connects to the ServerRouter)
    public void run() {

        //Get Our Name yo
        try {
            name = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // This is a router.
        if (name.startsWith("R")) {
            //Find out which Router connected;
            for(String router : RTable.keySet()){
                RoutingInfo temp = RTable.get(router);
//                println("Temp Router: "+temp.toString());
                if(temp.isRouter()
                        && (temp.getIPAddress().equals(ClientInfo.getIPAddress())
                        && (temp.getPort() == ClientInfo.getPort())) ){
                    temp.setClient(ClientInfo.getClient());
                    ClientInfo = temp;

//                    println("Router connected: " + ClientInfo.toString());
                    router();
                }
            }
        }

        // This is a client/server
        else {
            ClientInfo.setName(name);
            RTable.put(ClientInfo.getName(), ClientInfo); // sockets for communication
            clients();
        }

    }

    private void println(String s){
        System.out.println(Thread.currentThread().getName()+": "+s);
    }

    private void router() {
        try {

            // find out who they are trying to look for.
            destination = in.readLine();
            println("Router " + ClientInfo.getName() + " is looking for " + destination);


            // waits 10 seconds to let the routing table fill with all machines' information
            try {
                Thread.currentThread().sleep(10000);
            } catch (InterruptedException ie) {
                println("Thread interrupted");
            }

            RoutingInfo info = null;
            boolean clientFound = false;

            // Check if key exists within our table
            if (RTable.containsKey(destination)) {
                // Local client found.
                clientFound = true;

                info = RTable.get(destination);
                info.setInUse(true);

                outSocket = info.getClient(); // gets the socket for communication from the table
                subnetIn = new BufferedReader(new InputStreamReader(outSocket.getInputStream()));
                subnetOut = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer

                out.println("RingADingDing");

                println("Found from Subnet: " + destination);
//                subnetOut.println("This is your Router telling you to SPEAK!");
            }
            else {
                out.println("Bye Bye Bye");
            }

            // Client found.
            // Communication loop
            if (clientFound) {
                while ((inputLine = in.readLine()) != null) {

                    println("Client/Server said: " + inputLine);

                    if (inputLine.equals("Bye.")) { // exit statement
                        subnetOut.println("Bye.");
                        info.setInUse(false);
                        break;
                    }
                    outputLine = inputLine; // passes the input from the machine to the output string for the destination

                    subnetOut.println(outputLine); // writes to the destination
                }// end while
            }


        } catch (IOException e) {
            System.err.println("Could not listen to socket.");
            out.println("Bye.");
            System.exit(1);
        }

    }

    private void clients() {
        try {
            // Initial sends/receives
            destination = in.readLine(); // initial read (the destination for writing)
            println("Forwarding " + name + " to " + destination);
            out.println("Connected to the router."); // confirmation of connection

            subnetOut = null; // writers (for writing back to the machine and to destination)
            subnetIn = null; // reader (for reading from the machine connected to)

            // waits 10 seconds to let the routing table fill with all machines' information
            try {
                Thread.currentThread().sleep(10000);
            } catch (InterruptedException ie) {
                println("Thread interrupted");
            }

            RoutingInfo info = null;
            boolean clientFound = false;

            // Check if key exists within our table
            if (RTable.containsKey(destination)) {
                // Local client found.
                clientFound = true;

                info = RTable.get(destination);
                info.setInUse(true);

                outSocket = info.getClient(); // gets the socket for communication from the table
                subnetOut = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
                println("Found Client/Server: " + destination);
            }

            // Else check if key exists within subnet.
            if (!clientFound) {
                println("Looking through Subnet...");
                for(String router : RTable.keySet()){
                    info = RTable.get(router);
                    if(info.isRouter()
                            && !(
                                (info.getIPAddress().equals(ServerAddress))
                                && (info.getPort() == ServerPort)
                            )
                        ){
//                        println("RTable_Item: "+info.toString());

                        try{
//                            println("Found a Router on the Subnet.");

                            outSocket = new Socket(info.getIPAddress(), info.getPort());
                            subnetIn = new BufferedReader(new InputStreamReader(outSocket.getInputStream()));
                            subnetOut = new PrintWriter(outSocket.getOutputStream(), true);
                            subnetOut.println("Router");
                            subnetOut.println(destination);

                            String result = subnetIn.readLine();
                            if(result.equals("RingADingDing")) {
                                println(result+": Subnet router has destination: "+destination);
                                clientFound = true;
                                break;
                            }
                            else {
                                println(result+": Subnet router does NOT have destination, moving on...");
                            }

                        } catch (IOException e){
                            println("Could not connect to Subnet at address: "+info.getIPAddress()+", Moving on...");
                        }
                    }
                }
            }

            // Client found.
            // Communication loop
            if (clientFound) {
                while ((inputLine = in.readLine()) != null) {
                    println("Client/Server said: " + inputLine);
                    if (inputLine.equals("Bye.")) { // exit statement
                        subnetOut.println("Bye.");
                        info.setInUse(false);
                        break;
                    }
                    outputLine = inputLine; // passes the input from the machine to the output string for the destination

                    subnetOut.println(outputLine); // writes to the destination

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