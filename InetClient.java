//Import the Java libraries for input/output and working with networks 
import java.io.*;
import java.net.*;


/* This class represents the Client. It contains a main method which takes user input for a server name and
a hostname. Contains a method "getRemoteAddress" which communicates with a server in order to retrieve the IP address
of the user specified hostname and prints it. Contains a method "toText" */
public class InetClient {

	
	public static void main (String args[]) {

		/* Assign the serverName to a default of "localHost" unless an argument is given.
		/ If one or more arguments are present, the serverName is set as the first. 
		*/
		String serverName;
		if (args.length < 1)
			serverName = "localHost";
		else 
			serverName = args[0];

		/* Print info to screen (Assignment title, serverName to connect to and 
		/  port number to connect to). I changed the port to 50001.
		*/
		System.out.println("Clark Elliott's Inet Client, 1.8.\n");
		System.out.println("Using server: " + serverName + ", Port: 50001");

		//Setup a buffered reader to read characters from input stream
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		//This try block may throw an IOException which will be caught after
		try {

			//Create a variable to store a hostname or IP Address as a string
			String name;

			/* Acquire a hostname or IP address from user, exiting the loop only
			/ when user enters 'quit'
			*/
			do {
				//Request input from user and flush the buffer
				System.out.print("Enter a hostname or an IP address, (quit) to end: ");
				System.out.flush();

				//Store input in 'name' variable
				name = in.readLine();
				if (name.indexOf("quit") < 0)
					//Call helper method 'getRemoteAddress' which communicates with server
					getRemoteAddress(name, serverName);
			} while (name.indexOf("quit") < 0);
			System.out.println("Cancelled by user request.");
		} catch (IOException x) {
			x.printStackTrace();
		}
	}

	/* This method is not called in this class. It could be useful if we were required to make it portable for
	128 bit systems, but as the code in this class is currently structured, this method could be removed and
	everything would still work. It takes an array of bytes, representing the IP address retreived from the 
	server, and converts it is to characters for printing. Returns the array.
	*/
	static String toText (byte ip[]) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < ip.length; ++i) {
			if (i > 0)
				result.append(".");
			result.append(0xff & ip[i]);
		}
		return result.toString();
	}

	/* This method takes 2 arguments, 'name' holds the name of a host or IP address
	/  from user input, 'serverName' is either the local host or IP address of another
	/  server. The method communicates the 'name' with the given server and receives
	/  a corresponding IP address which is printed to the screen, unless there was a
	/  socket error.
	*/
	static void getRemoteAddress(String name, String serverName) {

		Socket sock;	//Create a socket to accept requests
		BufferedReader fromServer;	//This var is a buffer which will receive characters as a response from the server 
		PrintStream toServer;	//This print stream will be used to send communication to the server
		String textFromServer;	//A string var use to print individual lines of the server's response

		/* Attempt to connect the socket to the given port number on the given server
		/  Throws an IOException if there is a socket error
		*/
		try {
			sock = new Socket(serverName, 50001);	//Attempt to connect the socket to the server at the given port
			fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream())); //Initialize the buffer reader with an input stream through the socket
			toServer = new PrintStream(sock.getOutputStream());	//Initialize the output stream to send communication to the server through the socket
			
			toServer.println(name);	//Send the user input (name/IP of the host) to the server via print stream through the connected socket
			toServer.flush();	//Clear the print stream

			//Iterate through the buffered reader of response from the server, printing up to 2 lines
			for (int i = 1; i <= 3; i++) {
				textFromServer = fromServer.readLine();
				if (textFromServer != null)
					System.out.println(textFromServer);
			}

			sock.close();	//Close the socket

		} catch (IOException x) {
			System.out.println("Socket error.");
			x.printStackTrace();
		}
	}
}

