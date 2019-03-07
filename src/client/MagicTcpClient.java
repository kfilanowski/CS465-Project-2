package client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import common.Card;

/**
 * This class represents a concrete implementation of a 
 * magic client that uses the TCP network layer protocol.
 * @author Kevin Filanowski
 * @author Donny Queen
 * @version October 2018
 */
public class MagicTcpClient extends AbstractMagicClient {

	/**
	 * Initializes a new MagicTcpClient with the specified host and 
	 * the default port and flag.
	 * @param host - The address of the remote host to which to connect.
	 */
	public MagicTcpClient(InetAddress host) {
		super(host);
	}
	/**
	 * Initializes a new MagicTcpClient with the specified host,
	 * port, and default flag.
	 * @param host - The address of the remote host to which to connect.
	 * @param port - The port on the remote host to which to connect.
	 */
	public MagicTcpClient(InetAddress host, int port) {
		super(host, port);
	}

	/**
	 * Initializes a new MagicTcpClient with the specified host,
	 * port, and flag.
	 * @param host - The address of the remote host to which to connect.
	 * @param port - The port on the remote host to which to connect.
	 * @param flag - The arguments to send to the server.
	 */
	public MagicTcpClient(InetAddress host, int port, String flag) {
		super(host, port, flag);
	}

	/** 
	 * Establishes a TCP connection with the client connecting to the server.
	 * Listens for a flag, and sends back the requested types of cards to
	 * the client. The server ends communication by sending a blank string.
	 * @param out - The stream to which to write the random cards received.
	 * @throws IOException - if there is an I/O error while receiving the data.
	 * @throws ClassNotFoundException - If Card is not imported and found.
	 */
	@Override
	public void printToStream(PrintStream out) throws IOException,
													ClassNotFoundException {
		// Holds the object the server sends to the client
		Object object;
		try (
				// Socket will be our connection to a server
				Socket clientSocket = new Socket(getHost(), getPort());
				// Create a 'stream' connected to the server to send data.
				DataOutputStream toServer =
						new DataOutputStream(clientSocket.getOutputStream());
				// Create a 'stream' connected to the server to read data.
				ObjectInputStream clientIn =
						new ObjectInputStream(clientSocket.getInputStream());
				) {
			// Write the flag to the server.
			toServer.writeBytes(getFlag() + "\n");
			toServer.flush();

			// Read in the cards sent from the server and output them.
			do {
				object = clientIn.readObject();
				if (object instanceof Card) {
					out.print(object + "\r\n");
				}
			}
			while (object instanceof Card);
		}
	}
}
