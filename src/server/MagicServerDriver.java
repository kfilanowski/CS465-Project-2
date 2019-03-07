package server;

import java.io.FileNotFoundException;

/**
 * The main driver for a magic server, which can be tcp or udp 
 * depending on command line arguments.
 * Usage: java MagicServerDriver <tcp|udp> [port]
 * @author Kevin Filanowski
 * @author Donny Queen
 * @version October 2018
 */
public class MagicServerDriver {
	/** Abstraction of a magic server. Can be initialized as TCP or UDP. */
	private AbstractMagicServer server;
	/** The port number the server will be open with. */
	private Integer port;
	/** String representation of tcp */
	private final String TCP = "tcp";
	/** String representation of udp */
	private final String UDP = "udp";

	/**
	 * Constructor initializes a magic server.
	 * @param args - The command line arguments.
	 * @throws FileNotFoundException - Thrown if the cards file is not found.
	 */
	public MagicServerDriver(String[] args) throws FileNotFoundException,
	NumberFormatException {
		if (args.length > 1)
			port = Integer.parseInt(args[1]);
		if (args[0].equalsIgnoreCase(TCP)) {
			if (port != null) 
				server = new TcpMagicServer(port);
			else
				server = new TcpMagicServer();
		} else if (args[0].equalsIgnoreCase(UDP)) {
			if (port != null)
				server = new UdpMagicServer(port);
			else
				server = new UdpMagicServer();
		} else
			printUsageAndExit();
	}

	/**
	 * Prints a simple usage message to stderr and terminates the program.
	 */
	private static void printUsageAndExit() {
		System.out.println("Usage: java MagicServerDriver <tcp|udp> [port] ");
		System.exit(0);
	}

	/** 
	 * Tell the server to begin listening for clients.
	 * @throws MagicServerException - An IOException wrapped in
	 * a MagicServerException so that it applies to both UDP and TCP
	 * implementations.
	 */
	public final void serverListen() throws MagicServerException {
		server.listen();
	}

	/**
	 * This method serves as the entry point of the program.
	 * @param args - Command line arguments to the program.
	 * There must be at least one argument. The first argument specifies if
	 * the server will use a UDP or TCP connection. The second parameter,
	 * if present, must be the port number on which the server will 
	 * listen for requests. Any arguments beyond this will be ignored.
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			printUsageAndExit();
		}

		try {
			MagicServerDriver driver = new MagicServerDriver(args);
			driver.serverListen();
		} catch (FileNotFoundException ex) {
			System.out.println(ex.getMessage());
			System.exit(1);
		} catch (NumberFormatException ex) {
			System.out.println("Invalid Port Number: " + args[1]);
			printUsageAndExit();
		} catch (MagicServerException ex) {
			System.out.println(ex.getMessage());
			System.exit(1);
		}
	}
}
