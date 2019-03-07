package client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;

/**
 * An abstract class that contains fields and methods that may 
 * be common to implementations of the 'magic' protocol.
 * @author Kevin Filanowski
 * @author Donny Queen
 * @version Octobor 2018
 */
public abstract class AbstractMagicClient implements MagicClient {
	/** The default port on which a magic server listens. */
	public static final String DEFAULT_FLAG = "-A";
	/** The default port on which a magic server listens. */
	public static final int DEFAULT_PORT = 5892;
	/** The flag to send to the server to determine what cards to send back */
	private String flag;
	/** The remote host to which to connect. */
	private InetAddress host;
	/** The port on the remote host to which to connect. */
	private int port;

	/**
	 * Initializes a new AbstractMagicClient with the specified host,
	 * and the default port.
	 * @param host - The address of the remote host to which to connect.
	 */
	public AbstractMagicClient(InetAddress host) {
		this(host, DEFAULT_PORT, DEFAULT_FLAG);
	}

	/**
	 * Initializes a new AbstractMagicClient with the specified host and port.
	 * @param host - The address of the remote host to which to connect.
	 * @param port - The port on the remote host to connect to.
	 */
	public AbstractMagicClient(InetAddress host, int port) {
		this(host, port, DEFAULT_FLAG);
	}

	/**
	 * Initializes a new AbstractMagicClient with the specified host, port,
	 * and flag.
	 * @param host - The address of the remote host to which to connect.
	 * @param port - The port on the remote host to connect to.
	 * @param flag - The flags which determine which cards to send back.
	 */
	public AbstractMagicClient(InetAddress host, int port, String flag) {
		this.host = host;
		this.port = port;
		this.flag = flag;
	}

	/**
	 * Returns the flags that we want to send to the server.
	 * @return The flags to send to the server.
	 */
	protected String getFlag() {
		return flag;
	}

	/**
	 * Returns the address of the host to which to connect.
	 * @return The address of the host to which to connect.
	 */
	protected InetAddress getHost() {
		return host;
	}

	/**
	 * Returns the port on which the remote host is listening.
	 * @return The port on which the remote host is listening.
	 */
	protected int getPort() {
		return port;
	}

	/** 
	 * Establishes a TCP connection to the host/port specified when this
	 * object was created, reads a continuous stream of random cards from
	 * the socket's input stream, and prints that data to the specified
	 * output stream.
	 * @param out - The stream to which to write the random cards received.
	 * @throws IOException - if there is an I/O error while receiving the data.
	 */
	public abstract void printToStream(PrintStream out) throws IOException, 
													ClassNotFoundException;
}
