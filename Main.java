import sun.misc.Cleaner;

import java.io.IOException;
import java.util.Scanner;

public class Main{

    public static TCPServerRouter router;
    public static TCPServer server;
    public static TCPClient client;

    private static Scanner in;
    private static String user = "";

    public static void main( String [] args ) throws IOException {
        System.out.println("Thank you for running the Client/Server Manager");
        System.out.println("What would you like to run? ('(r)outer', '(s)erver', or '(c)lient')");

        in = new Scanner(System.in);
        user = in.nextLine();
        int answer = -1;

        if(user.startsWith("r")) answer = 0;
        else if (user.startsWith("s100")) answer = 3;
        else if (user.startsWith("c100")) answer = 4;
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
                server100();
                break;
            case 4 :
                client100();
                break;
            default :
                System.out.println("I don't understand! BYE!!!");
                break;
        }

    }

    private static void router() throws IOException{
        System.out.println("Running Router...");

        int port = 5556;

        router = new TCPServerRouter(port);
        router.run();
    }

    private static void server() throws IOException{
        System.out.println("Running Server...");

        String SERVER_ROUTER_NAME = "10.99.10.214";
        int PORT = 5556;
        String CLIENT_ADDRESS = "10.99.27.97";

        server = new TCPServer(SERVER_ROUTER_NAME, PORT, CLIENT_ADDRESS);
        server.run();
    }

    private static void client()throws IOException{
        System.out.println("Running Client...");

        String SERVER_ROUTER_NAME = "10.99.10.214";
        int PORT = 5556;
        String SERVER_ADDRESS = "10.99.2.181";
        String INPUT_FILE = "src/file.txt";

        client = new TCPClient(SERVER_ROUTER_NAME, PORT, SERVER_ADDRESS, INPUT_FILE);
        client.run();
    }


    private static void server100() throws IOException {
        for(int i = 0; i < 100; i ++){
            System.out.println("Running Server100...");

            String SERVER_ROUTER_NAME = "10.99.10.214";
            int PORT = 5556;
            String CLIENT_ADDRESS = "10.99.27.97";

            server = new TCPServer(SERVER_ROUTER_NAME, PORT, CLIENT_ADDRESS);
            server.run();
        }
    }

    private static void client100() throws IOException {
        for(int i = 0; i < 100; i ++){
            System.out.println("Running Client100...");

            String SERVER_ROUTER_NAME = "10.99.10.214";
            int PORT = 5556;
            String SERVER_ADDRESS = "10.99.2.181";
            String INPUT_FILE = "src/file.txt";

            client = new TCPClient(SERVER_ROUTER_NAME, PORT, SERVER_ADDRESS, INPUT_FILE);
            client.run();
        }
    }
}