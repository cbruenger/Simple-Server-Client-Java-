This program contains classes for a client/server which can be executed on any machine or locally hosted.

Server: 
	The sever waits at port 5001 for client requests that contain an IP address or a host name of a given website. The Server then retrieves the IP and hostname that was specified in the request and sends it back to the client. If the site doesn't exist then it notifies the client.

Client:
	The client prompts a user to enter an IP address or host name of any website and then receives, from the server, the IP address and hostname of the requested site. If no site exists then the server notifies the client. Communication is sent to the server at port 50001 whether hosted locally or remotely. Any number of clients can be run in separate terminals.

- To compile, enter this command: 'javac *.java'
- To run server, enter this command: 'java InetServer'
- To run client when server is locally hosted: 'java InetClient'
- To run client and specify the IP address of remote server: 'java InetClient ipAddress' where ipAddress is the remote address of the server.
- To exit client program, enter 'quit'.