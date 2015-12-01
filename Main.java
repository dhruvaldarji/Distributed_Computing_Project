import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Main{

    public static TCPServerRouter router;
    public static TCPServer server;
    public static TCPClient client;

    public static String dhruvalIP = "10.99.1.221";
    public static String deionIP = "10.99.30.7";
    public static String mirelaIP = "10.99.16.205";

    public static int dhruvalPort = 5555;
    public static int deionPort = 5555;
    public static int mirelaPort = 5555;

    public static String inputFile = "src/file.txt";

    /*********************************************************/
    /** Default IPs and Ports for this instance. **/
    public static String ip = dhruvalIP;
    public static int port = dhruvalPort;
    /*********************************************************/
    public static String CLIENT_NAME = "C1";
    public static String SERVER_NAME = "S1";

    private static Scanner in;
    private static String user = "";

    public static void main( String [] args ) throws IOException {
        System.out.println("Thank you for running the Client/Server Manager");
        System.out.println("What would you like to run? ('(r)outer', '(s)erver'/ (s50), or '(c)lient'/ (c50) )");

        in = new Scanner(System.in);
        user = in.nextLine();
        int answer = -1;

        if(user.startsWith("r")) answer = 0;
        else if (user.startsWith("s50")) answer = 3;
        else if (user.startsWith("c50")) answer = 4;
        else if (user.startsWith("s")) answer = 1;
        else if (user.startsWith("c")) answer = 2;


        switch(answer){
            case 0 :
                router();
                break;
            case 1 :
                server();
                break;
            case 2 :
                client();
                break;
            case 3:
                server50();
                break;
            case 4 :
                client50();
                break;
            default :
                System.out.println("I don't understand! BYE!!!");
                break;
        }

    }

    private static void router() throws IOException{
        System.out.println("Running Router...");

        HashMap<String, RoutingInfo> subnetList = new HashMap<>();

        subnetList.put("R1", new RoutingInfo(null, dhruvalIP, 5555, "R1", true));
        subnetList.put("R2", new RoutingInfo(null, deionIP, 5556, "R2", true));
        subnetList.put("R3", new RoutingInfo(null, mirelaIP, 5557, "R3", true));

        router = new TCPServerRouter(port, subnetList);
        router.run();
    }

    private static void server() throws IOException{
        System.out.println("Running Server...");

        String SERVER_ROUTER_NAME = ip;

        server = new TCPServer(SERVER_ROUTER_NAME, port, SERVER_NAME, CLIENT_NAME);
        server.run();
    }

    private static void client()throws IOException{
        System.out.println("Running Client...");

        String SERVER_ROUTER_NAME = ip;
        String INPUT_FILE = inputFile;

        client = new TCPClient(SERVER_ROUTER_NAME, port, CLIENT_NAME, SERVER_NAME, INPUT_FILE);
        client.run();
    }


    private static void server50() throws IOException {
        for(int i = 0; i < 50; i++){
            System.out.println("Running Server50...");

            String SERVER_ROUTER_NAME = ip;

            server = new TCPServer(SERVER_ROUTER_NAME, port, SERVER_NAME, CLIENT_NAME);
            server.run();
        }
    }

    private static void client50() throws IOException {
        for(int i = 0; i < 50; i++){
            System.out.println("Running Client50...");

            String SERVER_ROUTER_NAME = ip;
            String INPUT_FILE = inputFile;

            client = new TCPClient(SERVER_ROUTER_NAME, port, CLIENT_NAME, SERVER_NAME, INPUT_FILE);
            client.run();
        }
    }
}