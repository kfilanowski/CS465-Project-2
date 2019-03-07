package client;

import java.io.IOException;

/**
 * The interface to a magic client component.
 * @author Kevin Filanowski
 * @author Donny Queen
 * @version October 2018
 */
public interface MagicClient {
	/**
	 * Establishes a TCP connection to the host/port specified when this 
	 * object was created, reads a continuous stream of random cards from 
	 * the socket's input stream, and prints that data to the specified 
	 * output stream.
	 * @param out - The stream to which to write the random cards received.
	 * @throws java.io.IOException - If there is an I/O error while receiving
	 * the data.
	 */
	void printToStream(java.io.PrintStream out) throws IOException, 
											ClassNotFoundException;
}
