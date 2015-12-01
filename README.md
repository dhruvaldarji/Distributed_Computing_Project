# Distributed Computing Project User Guide
## Phase 2 

### Introduction
For Phase 2 of this project we designed and implemented a Peer-to-Peer system. The purpose of this phase was to design a Peer-to-Peer system that allows as many pairs of nodes as possible to exchange messages. As in Phase 1, a message that is sent in lowercase is to be converted to uppercase and returned. For the implementation of our Peer-to-Peer system, we modified and used our Client-Server programs from Phase 1. We used four main modules in the implementation of our system; TCPServerRouter, SThread, TCPServer, and TCPClient. We also used our custom statistics module, called Stats, to monitor and keep track of variable data, and our main module, called Main, to dynamically run the system. More detail on each of the modules, and how they were modified from Phase 1, will be found in the Design Modules and Implementation section.

#### Classes
* Main
    * Our main class allows the users access to a very simple interface that allows them to run a router, a server or a client. The server and client runs have the option to be run 50 times each as well. The main class allows the user to pre-setup a subnet list with IP's of known routers, as well as ports. Additionally, once the routers are configured, the user's can select names for the clients and server that they would like to connect to. 

* TCPServerRouter

* SThread

* TCPServer

* TCPClient

* Stats

* RoutingInfo

### Configuration



### Execution
