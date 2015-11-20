/**
 * Created by Deion on 11/12/2015.
 */

import java.net.*;
import java.io.*;
import java.util.HashMap;

public class RoutingInfo {
    private String IPAddress;
    private int port;
    private Socket client;
    private Boolean inUse = false;
    private String name;
    private boolean isRouter = false;

    public RoutingInfo(Socket client, String IPAddress) {
        this.client = client;
        this.IPAddress = IPAddress;
    }

    public RoutingInfo(Socket client, String IPAddress, String name) {
        this.client = client;
        this.IPAddress = IPAddress;
        this.name = name;
    }

    public RoutingInfo(Socket client, String IPAddress, int port, String name, Boolean isRouter) {
        this.client = client;
        this.IPAddress = IPAddress;
        this.port = port;
        this.name = name;
        this.isRouter = isRouter;
    }

    public boolean isRouter() {
        return isRouter;
    }

    public void setIsRouter(boolean isRouter) {
        this.isRouter = isRouter;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public Boolean getInUse() {
        return inUse;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RoutingInfo{" +
                "client=" + client +
                ", IPAddress='" + IPAddress + '\'' +
                ", port=" + port +
                ", inUse=" + inUse +
                ", name='" + name + '\'' +
                ", isRouter=" + isRouter +
                '}';
    }
}
