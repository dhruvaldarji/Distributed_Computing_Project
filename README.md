# Distributed Computing Project User Guide
## Phase 2 

### Introduction
For Phase 2 of this project we designed and implemented a Peer-to-Peer system. The purpose of this phase was to design a Peer-to-Peer system that allows as many pairs of nodes as possible to exchange messages. As in Phase 1, a message that is sent in lowercase is to be converted to uppercase and returned. For the implementation of our Peer-to-Peer system, we modified and used our Client-Server programs from Phase 1. We used four main modules in the implementation of our system; TCPServerRouter, SThread, TCPServer, and TCPClient. We also used our custom statistics module, called Stats, to monitor and keep track of variable data, and our main module, called Main, to dynamically run the system. More detail on each of the modules, and how they were modified from Phase 1, will be found in the Design Modules and Implementation section.

### Classes
* Main
    * The Main class allows the users access to a very simple interface that allows them to run a router, a server or a client. The server and client methods have the option to be run 50 times each as well. The main class allows the user to pre-setup a subnet list with IP's of known routers, as well as ports. Additionally, once the routers are configured, the user's can select names for the clients and server that they would like to connect to, as well as the input text file for the client to send. 

* TCPServerRouter
    * The TCPServerRouter class is the class that sets up and runs the router on a specified port. The Subnet list is also passed through the constructor. When run, the TCPServerRouter serves as the main thread and spawns SThreads whenever a new client connects. The router then passes on the handling of the client to the SThread along with a copy of the socket, the local RoutingTable, and a RoutingInfo object for the new client.

* RoutingInfo
    * The RoutingInfo object holds the ipAddress, port, Socket, usage status, and client name. If the object is a router, it has boolean called isRouter set. The object comes with getters, setters, and a dedicated toString method.

* SThread
    * The SThread class is a derived from the Thread class and  it handles connections to a client for the router. The constructor takes in information about the parent router (ip, port), the local RoutingTable, and a RoutingInfo object with information on the client that has connected. The constructor creates input and output streams for the client, and sets private global variables to be used when the SThread’s run method is called. When the thread is ‘run’, it firsts attempts to get the client’s name and stores the value in the dedicated RoutingInfo object. If the connecting client is another router, we run the ‘router’ method, otherwise we run the ‘clients’ method. 
		*Router: We know that router’s can only be client’s if they are searching the subnet for a node, so we take in the name of the node it is looking for. If the node does not exist within our subnet (our RoutingTable), we tell the router “Bye Bye Bye”. If the node does exist within the subnet (our RoutingTable), then we set socket to read through to the connected client, setup the input and output stream, and return “RingADingDing”. This lets the connecting router know that the node was found and is ready to receive data. Then, until the client says “Bye.”, the routers act as a bridge between the nodes and pass data through.
		*Clients: We know that the first message sent from a client/server is always the name of the destination node it wishes to connect to. So we save the client info, and wait for changes to propagate through to our routing table. If the destination exists within the subnet (our RoutingTable), then the input streams and output streams are created and a clientFound variable is set to true. If the destination is not found in the subnet, the router queries other routers that it knows of that exist within it’s routing table. If the node exists within other subnets, then input and output streams are created clientFound is set to true. Otherwise clientFound is set to false, queries are terminated and process ends with result that the node could not be found. Finally, if clientFound returned true thoughout that process, the router begins passing data from it’s client to its destination node until it receives a “Bye.” At that point, the last termination message is sent, and the SThread terminates. 

* TCPServer

* TCPClient

* Stats

### Configuration



### Execution
