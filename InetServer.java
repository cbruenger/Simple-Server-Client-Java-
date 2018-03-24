
/* This file contains 2 classes, the server which accepts a request from a client, and a worker.
/  The server creates an instance of the worker, and has the worker 'do the work', in it's own thread
/  which is trivial since the main focus of this assignment is the client/server communication.
*/

//Import the Java libraries for input/output and working with networks
import java.io.*;
import java.net.*;

/* This Worker class is created by the server with a socket passed as a parameter. Since it runs
/  in its own thread, the 'run()' method is called automatically, which does the 'work'. In this
/  case, the 'work' is setting up a buffered reader through the given socket to recieve a host name
/  or IP address from the client, and also setting up a print stream to send communication back to the
/  client. Then, the Worker retrieves the IP address and host name for the corresponding IP/host specified
/  via the client, and sends it to the client.
*/
class Worker extends Thread {

	Socket sock;	//This socket is a class member, local to the Worker

	//Constructor, takes a socket as an argument and assigns the class member socket to it
	Worker (Socket s) {
		this.sock = s;
	}

	/* Since the class is setup to function in a multi-threaded environment, this method
	/  is automatically called upon invoking the .start() method on an instance of the class.
	/  This method creates and initializes a print stream to send communication through the
	/  given socket, and also creates a buffered reader to accept communication from client
	/  through the given socket. It prints the name of the host that it receives from the client,
	/  calls the 'printRemoteAddress' method before closing the socket.
	*/
	public void run() {
		PrintStream out = null;	//This print stream variable will be used to send communication to the client
		BufferedReader in = null; //This var is a buffer which will receive characters from the client
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream())); //Initialize buffer reader variable with input stream reader through the given socket
			out = new PrintStream(sock.getOutputStream());	//Initialize the output stream to send communication to the client through the socket

			/* Attempt to retrieve an IP/host name from the buffered reader and call the 'printRemoteAddress' method
			/  Otherwise if there is a problem retrieving the data from the buffer, an IOException is caught
			*/
			try {
				String name;	//A var to hold the user client specified IP/host name 
				name = in.readLine();	//Retrieve the characters from the buffered reader and store in in the 'name' var
				System.out.println("Looking up " + name);	//Print notification to screen including IP/host name
				printRemoteAddress(name, out);	//Call the method to actually retrieve the Ip/host name for the client specified IP/host name
			} catch (IOException x) {
				System.out.println("Server read error");
				x.printStackTrace();
			}
			sock.close();	//Close the socket
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}

	/* This method takes the client specified IP/host name and a print stream as args, and then sends
	/  necessary informative statements through the print stream, including the requested hostName and
	/  IP address
	*/
	static void printRemoteAddress (String name, PrintStream out) {
		try {
			//Send helpful statement through print stream
			out.println("Looking up " + name + "...");

			//This var is a class that represents a machine by its IP, which is found by passing 'name' as an arg into the 'getByName' method
			InetAddress machine = InetAddress.getByName(name);

			//Sends the host name through the print stream
			out.println("Host name : " + machine.getHostName());

			//Sends the IP address through the print stream
			out.println("Host IP : " + toText(machine.getAddress()));
		} catch (UnknownHostException ex) {
			out.println("Failed in attempt to look up " + name);
		}
	}

	/* This method is necessary for portability on 128 bit systems. It takes an array of bytes
	/  as a parameter and appends characters into a new buffer by using the bitwise AND operator.
	/  Returns the resulting buffer
	*/
	static String toText(byte ip[]) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < ip.length; ++i) {
			if (i > 0)
				result.append(".");
			result.append(0xff & ip[i]);
		}
		return result.toString();
	}
}

/* This class represents the server and only contains a main method which creates variables
/  representing a queue length, a port number a socket (which is used to accept connections
/  from clients) and a server socket (which is initialized with the given port and queue length).
/  An informative print statement is printed to the screen, and then the program enters a while
/  loop to wait for client requests. Upon receiving a client request, a new Worker is spawned in
/  in a new thread to do the work.
*/
public class InetServer {

	public static void main(String args[]) throws IOException {
		int q_len = 6;	//Maximum number of client requests to queue
		int port = 50001;	//The port at which the server will accept requests
		Socket sock;	//A socket that will be designated to each client request

		//This variable represents a Server Socket that is constructed on a given port and with a given queue length
		ServerSocket servsock = new ServerSocket(port, q_len);

		System.out.println("Server starting up, listening at port 50001.\n");	//Informative print statement

		/* This loop runs for the live of the program, waiting for client requests, and then calling the accept() method
		/  on there server socket which returns a new socket to be used. For each request, a new Worker class is started in
		/  its own thread to do the work
		*/
		while (true) {
			sock = servsock.accept();	//Assigns the 'sock' var to a new socket to accept a client request
			new Worker(sock).start();	//An instance of Worker is constructed with the given socket and started in its own thread
		}
	}
}

