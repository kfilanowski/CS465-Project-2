package server;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import common.Card;
/**
 * This class represents a concrete implementation of a magic server
 * that uses the UDP transport layer protocol.
 * @author Kevin Filanowski
 * @author Donny Queen
 * @version October 2018
 */
public class UdpMagicServer extends AbstractMagicServer implements MagicServer {
	private static final int SIZE = 1024;
	
	/**
	 * Creates a new UdpMagicServer that listens for connections on
	 * the default magic TCP port, and uses the default card source.
	 * @throws FileNotFoundException - if the file used
	 * to initialize the cards is not found.
	 */
	public UdpMagicServer() throws FileNotFoundException {
		super();
	}

	/**
	 * Creates a new UdpMagicServer that listens for connections on
	 * the specified magic TCP port, and uses the default card source.
	 * @param port - port the server will listen at.
	 * @throws FileNotFoundException - if the input file cannot be located.
	 */
	public UdpMagicServer(int port) throws FileNotFoundException {
		super(port);
	}

	/**
	 * Creates a new UdpMagicServer that listens for connections on
	 * the specified magic TCP port, and uses the specified card source.
	 * @param port - port the server will listen at.
	 * @param source - source used to generate cards.
	 */
	public UdpMagicServer(int port, CardSource source) {
		super(port, source);
	}

	/**
	 * Creates a new UdpMagicServer that listens for connections on
	 * the default magic TCP port, and uses the specified card source.
	 * @param source - source used to generate cards.
	 * @throws FileNotFoundException - if the input file cannot be located.
	 */
	public UdpMagicServer(CardSource source) {
		super(source);
	}

	/**
	 * Causes the magic server to listen for requests.
	 * Listens for a flag, and sends back the requested types of cards to
	 * the client, using UDP. The server ends communication by
	 * sending an empty packet to the client.
	 * @throws MagicServerException - if an error occurs while
	 * trying to listen for connections.
	 * @throws SocketException 
	 */
	public void listen() throws MagicServerException {
		ArrayList<Card> usedCards = new ArrayList<>();
		System.out.println("Udp Server listening on port: " + getPort());
		try (DatagramSocket serverSocket = new DatagramSocket(getPort());)
		{
			while(!serverSocket.isClosed()){ 
				// Create a packet to receive data.
				byte[] receiveData = new byte[SIZE];
				DatagramPacket receivePacket = new DatagramPacket(receiveData,
						receiveData.length);

				// Receive data.
				serverSocket.receive(receivePacket);
				setCardsReturned(new String(
								receivePacket.getData()).toUpperCase());

				// Extract return address and port from packet.
				InetAddress IPAddress = receivePacket.getAddress();
				int port = receivePacket.getPort();

				// Create packet to send data. Set up sending data.
				DatagramPacket sendPacket;				
				
				// Counter for the number of unique and correct cards have been sent
				int i = 0;	
				// Write random cards of client's requested type to client.
				Card card;
				while (i < this.getItemsToSend()) {    
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				ObjectOutputStream objOut = new ObjectOutputStream(outStream);
				byte[] sendData;
					card = getSource().next();
					// Ensure we do not send duplicates.
					if (!usedCards.contains(card)) {
						usedCards.add(card);
						objOut.writeObject(card);
						objOut.flush();
						sendData = outStream.toByteArray();
						sendPacket = new DatagramPacket(sendData,
								sendData.length, IPAddress, port);
						serverSocket.send(sendPacket);
						i++;
					}
				}
				usedCards.clear();
				// Send empty packet to close.
				byte[] sendEmpty = new byte[0];
				sendPacket = new DatagramPacket(sendEmpty, sendEmpty.length,
						IPAddress, port);
				serverSocket.send(sendPacket);
			}
		} catch (IOException ex) {
			throw new MagicServerException(ex.getMessage());
		}
	}
}
