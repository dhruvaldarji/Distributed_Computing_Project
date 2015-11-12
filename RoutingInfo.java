/**
 * Created by Deion on 11/12/2015.
 */

import java.net.*;
import java.io.*;
import java.util.HashMap;

public class RoutingInfo {
    private String IPAddress;
    private Socket client;
    private Boolean inUse;
    private String name;

    public RoutingInfo() {
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
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
}
