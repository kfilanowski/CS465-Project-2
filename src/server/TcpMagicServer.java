package server;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.ArrayList;
import common.Card;
/**
 * This class represents a concrete implementation of a magic server that
 * uses the TCP transport layer protocol.
 * @author Kevin Filanowski
 * @author Donny Queen
 * @version October 2018
 */
public class TcpMagicServer extends AbstractMagicServer
implements MagicServer {

	/**
	 * Creates a new TcpMagicServer that listens for connections on
	 * the default magic TCP port, and uses the default card source.
	 * @throws FileNotFoundException - if the file used
	 * to initialize the cards is not found.
	 */
	public TcpMagicServer() throws FileNotFoundException {
		super();
	}

	/**
	 * Creates a new TcpMagicServer that listens for connections on
	 * the specified magic TCP port, and uses the default card source.
	 * @param port - port the server will listen at.
	 * @throws FileNotFoundException - if the input file cannot be located.
	 */
	public TcpMagicServer(int port) throws FileNotFoundException {
		super(port);
	}

	/**
	 * Creates a new TcpMagicServer that listens for connections on
	 * the specified magic TCP port, and uses the specified card source.
	 * @param port - port the server will listen at.
	 * @param source - source used to generate cards.
	 */
	public TcpMagicServer(int port, CardSource source) {
		super(port, source);
	}

	/**
	 * Creates a new TcpMagicServer that listens for connections on
	 * the default magic TCP port, and uses the specified card source.
	 * @param source - source used to generate cards.
	 * @throws FileNotFoundException - if the input file cannot be located.
	 */
	public TcpMagicServer(CardSource source) {
		super(source);
	}

	/**
	 * Causes the magic server to listen for requests.
	 * @throws MagicServerException - if an error occurs while
	 * trying to listen for connections.
	 */
	public void listen() throws MagicServerException {
		System.out.println("TCP Server listening on port: " + getPort());
		// Scanner to read client commands.
		Scanner inClient;
		// String to hold client commands.
		String clientString;
		// Socket to connect to client.
		Socket connect;
		// Object output stream for the client to send cards to client.
		ObjectOutputStream outClient;

		try (ServerSocket serverSocket = new ServerSocket(getPort());)
		{
			Card card = null;

			//ArrayList that holds cards that have already been sent
			ArrayList<Card> usedCards = new ArrayList<>();
			// Consistently process requests until server closes.
			while(!serverSocket.isClosed()) {
				connect = serverSocket.accept();
				outClient = new ObjectOutputStream(connect.getOutputStream());
				// Scan input from client
				inClient = new Scanner(connect.getInputStream());
				clientString = inClient.nextLine();
				setCardsReturned(clientString.toUpperCase());

				int i = 0;
				while( i < this.getItemsToSend()){
					card = getSource().next();
					if(!usedCards.contains(card)){
						usedCards.add(card);
						outClient.writeObject(card);
						outClient.flush();
						i++;
					}
				}
				//Clears the usedCards for the next connection
				usedCards.clear();
				// Writing the finished statement (An empty string).
				outClient.writeObject("");
				outClient.flush();

				// Closing connections.
				inClient.close();
				connect.close();
				outClient.close();
			}
		}
		catch(IOException ioe){
			throw new MagicServerException(ioe.getMessage());
		}
	}
}
