﻿# Distributed Computing Project User Guide
### Phase 2 

## Introduction
For Phase 2 of this project we designed and implemented a Peer-to-Peer system. The purpose of this phase was to design a Peer-to-Peer system that allows as many pairs of nodes as possible to exchange messages. As in Phase 1, a message that is sent in lowercase is to be converted to uppercase and returned. For the implementation of our Peer-to-Peer system, we modified and used our Client-Server programs from Phase 1. We used four main modules in the implementation of our system; TCPServerRouter, SThread, TCPServer, and TCPClient. We also used our custom statistics module, called Stats, to monitor and keep track of variable data, and our main module, called Main, to dynamically run the system.

## Classes
* Main
    * The Main class allows the users access to a very simple interface that allows them to run a router, a server or a client. The server and client methods have the option to be run 50 times each as well. The main class allows the user to pre-setup a subnet list with IP's of known routers, as well as ports. Additionally, once the routers are configured, the user's can select names for the clients and server that they would like to connect to, as well as the input text file for the client to send. 

* TCPServerRouter
    * The TCPServerRouter class is the class that sets up and runs the router on a specified port. The Subnet list is also passed through the constructor. When run, the TCPServerRouter serves as the main thread and spawns SThreads whenever a new client connects. The router then passes on the handling of the client to the SThread along with a copy of the socket, the local RoutingTable, and a RoutingInfo object for the new client.

* RoutingInfo
    * The RoutingInfo object holds the ipAddress, port, Socket, usage status, and client name of a connecting entity (router, server, client). If the object is a router, it has boolean called isRouter set. The object comes with getters, setters, and a dedicated toString method.

* SThread
    * The SThread class is a class that is derived from the Thread class and it handles connections to a client for the router. The constructor takes in information about the parent router (ip, port), the local RoutingTable, and a RoutingInfo object with information on the client that has connected. The constructor creates input and output streams for the client, and sets private global variables to be used when the SThread’s run method is called. When the thread is ‘run’, it firsts attempts to get the client’s name and stores the value in the dedicated RoutingInfo object. If the connecting client is another router, we run the ‘router’ method, otherwise we run the ‘clients’ method. 
		* Router: We know that routers can only be clients if they are searching the subnet for a node, so we take in the name of the node it is looking for. If the node does not exist within our subnet (our RoutingTable), we tell the router “Bye Bye Bye”. If the node does exist within the subnet (our RoutingTable), then we set socket to read through to the connected client, setup the input and output stream, and return “RingADingDing”. This lets the connecting router know that the node was found and is ready to receive data. Then, until the client says “Bye.”, the routers act as a bridge between the nodes and pass data through.
		* Clients: We know that the first message sent from a client/server is always the name of the destination node it wishes to connect to. So we save the client info, and wait for changes to propagate through to our routing table. If the destination exists within the subnet (our RoutingTable), then the input streams and output streams are created and a clientFound variable is set to true. If the destination is not found in the subnet, the router queries other routers that it knows of that exist within its routing table. If the node exists within other subnets, then input and output streams are created and clientFound is set to true. Otherwise, clientFound is set to false, queries are terminated and the process ends with result that the node could not be found. Finally, if clientFound returned true throughout that process, the router begins passing data from its client to its destination node until it receives a “Bye.” At that point, the last termination message is sent, and the SThread terminates. 

* TCPServer
	* The TCPServer class is the class that establishes and runs a “server” connection to a router of the user’s choice. First it sends the router it’s own name for reference, and the name of the client it wishes to connect with. Once connection is established, the only purpose of this class is to spit out in all uppercase whatever it receives from the client. Once it receives a “Bye.” from the client, it exits. 

* TCPClient
	* The TCPClient class is the class that establishes and runs a “client” connection to a router of the user’s choice. First it sends the router its own name for reference, and the name of the server it wishes to connect with. Once connection is established, the class reads in from a pre-specified file. As long as there is data within the file, the client will continue to send data through the router to the server. Once it sends a line, it waits for the server to reply before sending another line. Once the entire file has been sent, the client sends the termination response, “Bye.”, calculates statistics, prints out the results to a data file, and then terminates. 

* Stats
	* The Stats class is used to manage connectionTime, averageTransmissionInSize, averageTransmissionOutSize, AverageTransmissionSize, list of transmissionInSizes, list of transmissionOutSizes, averageTransmissionTime, list of transmissionTimes, efficiency of sending data, averageEfficiency for sending data, and the list of efficiencies for all the data sent in a run. The Stats can also print out the results as a string, as well as print the data to a CSV file for analysis using Excel or comparative software. 


## Configuration
This entire program can be managed via the Main class. In order to run this program, the user must change default IP and port of the router they wish to connect too, routers in the known subnet, the client name, and the server name. 

## Execution
Then the user can run the main class, select whether they want to run a router, server, or client, and watch the magic. Of course, a user is allowed to run multiple instances on the same machine so it is possible to run multiple routers, client, and server on the same machine. 