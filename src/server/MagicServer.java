package server;

/**
 * The interface to a magic server.
 * @author Kevin Filanowski
 * @author Donny Queen
 * @version October 2018
 */
public interface MagicServer {
	/**
	 * Causes the magic server to listen for requests
	 * @throws MagicServerException - If an error occurs while trying to
	 * listen for connections.
	 */
	void listen() throws MagicServerException;
}
