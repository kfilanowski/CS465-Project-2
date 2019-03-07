package client;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import common.Card;

/**
 * This class represents a concrete implementation of a 
 * magic client that uses the UDP network layer protocol.
 * @author Kevin Filanowski
 * @author Donny Queen
 * @version October 2018
 */
public class MagicUdpClient extends AbstractMagicClient {

	/** 
	 * The size of the payload a UDP server should 
	 * send in response to a magic request. 
	 */
	private static final int SIZE = 1024;

	/** The length of time to wait for a timeout */
	private static final int TIMEOUT = 500;
	/**
	 * Initializes a new MagicUdpClient with the specified host and 
	 * the default port and flag.
	 * @param host - The address of the remote host to which to connect.
	 */
	public MagicUdpClient(InetAddress host) {
		super(host);
	}
	/**
	 * Initializes a new MagicUdpClient with the specified host,
	 * port, and default flag.
	 * @param host - The address of the remote host to which to connect.
	 * @param port - The port on the remote host to which to connect.
	 */
	public MagicUdpClient(InetAddress host, int port) {
		super(host, port);
	}

	/**
	 * Initializes a new MagicUdpClient with the specified host,
	 * port, and flag.
	 * @param host - The address of the remote host to which to connect.
	 * @param port - The port on the remote host to which to connect.
	 * @param flag - The arguments to send to the server.
	 */
	public MagicUdpClient(InetAddress host, int port, String flag) {
		super(host, port, flag);
	}

	/** 
	 * Send a request out to the server and output the random cards received.
	 * @param out - The stream to which to write the random cards received.
	 * @throws IOException - if there is an I/O error while receiving the data.
	 */
	@Override
	public void printToStream(PrintStream out) throws IOException,
												ClassNotFoundException {
		// Open a socket to send data. Not the same as a TCP connection!
		DatagramSocket clientSocket = new DatagramSocket();
		// Set a timeout in case we get no response.
		clientSocket.setSoTimeout(TIMEOUT);

		// Create an array of bytes to send data.
		byte[] sendData = new byte[SIZE];
		sendData = getFlag().getBytes();

		// New packet to send to server.
		DatagramPacket sendPacket = new DatagramPacket(sendData, 
				sendData.length, getHost(), getPort());

		// Push the data into the socket.
		clientSocket.send(sendPacket);

		// Create storage to receive a packet from the server.
        byte[] receiveData = new byte[SIZE];
		DatagramPacket receivePacket = 
				new DatagramPacket(receiveData, receiveData.length);

        Card card = null;

		// Constantly receive cards until server sends empty packet or timeout.
		while (receivePacket.getLength() > 0) {
                clientSocket.receive(receivePacket);
				ByteArrayInputStream inStream = new 
											ByteArrayInputStream(receiveData);
				ObjectInputStream objIn = new ObjectInputStream(
										new BufferedInputStream(inStream));			
				card = (Card) objIn.readObject();

			if (receivePacket.getLength() > 0){
				out.print(card.toString() + "\r\n");
			}
		// Close the socket.
		}
		clientSocket.close();
	}
}
