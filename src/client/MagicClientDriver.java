package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The main driver for a magic client, which connects to a magic server.
 * The magic client can be either TCP or UDP.
 * Usage: java MagicClientDriver <tcp|udp> <host> [port]
 * @author Kevin Filanowski
 * @author Donny Queen
 * @version October 2018
 */
public class MagicClientDriver {
	/** The default port to connect to. */
	private final static int DEFAULT_PORT = 5892;
	/** The default flag to send to the server. */
	public final static String DEFAULT_FLAG = "-A";
	/** Abstraction of a magic client, which can be UDP or TCP. */
	private AbstractMagicClient client;
	/** String representation of tcp */
	private final static String TCP = "tcp";
	/** String representation of udp */
	private final static String UDP = "udp";

	/**
	 * Constructor initializes a magic client.
	 * @param protocol - The protocol to setup the client to. UDP or TCP.
	 * @param host - The host to connect to.
	 * @throws UnknownHostException - If the host cannot be reached.
	 */
	public MagicClientDriver(String protocol, String host)
			throws UnknownHostException {
		this(protocol, host, DEFAULT_PORT, DEFAULT_FLAG);
	}

	/**
	 * Constructor initializes a magic client.
	 * @param protocol - The protocol to setup the client to. UDP or TCP.
	 * @param host - The host to connect to.
	 * @param port - The port number to connect to.
	 * @throws UnknownHostException - If the host cannot be reached.
	 */
	public MagicClientDriver(String protocol, String host, int port)
			throws UnknownHostException {
		this(protocol, host, port, DEFAULT_FLAG);
	}
	/**
	 * Constructor initializes a magic client.
	 * @param protocol - The protocol to setup the client to. UDP or TCP.
	 * @param host - The host to connect to.
	 * @param flag - The flag to send to the host.
	 * @throws UnknownHostException - If the host cannot be reached.
	 */
	public MagicClientDriver(String protocol, String host, String flag)
			throws UnknownHostException {
		this(protocol, host, DEFAULT_PORT, flag);
	}

	/**
	 * Constructor initializes a magic client.
	 * @param protocol - The protocol to setup the client to. UDP or TCP.
	 * @param host - The host to connect to.
	 * @param port - The port number to connect to.
	 * @param flag - The flag to send to the host.
	 * @throws UnknownHostException - If the host cannot be reached.
	 */
	public MagicClientDriver(String protocol, String host,
			int port, String flag) throws UnknownHostException {
		if (protocol.equalsIgnoreCase(TCP)) {
			client = new MagicTcpClient(InetAddress.getByName(host),
					port, flag);
		}
		else if (protocol.equalsIgnoreCase(UDP)) {
			client = new MagicUdpClient(InetAddress.getByName(host),
					port, flag);
		} else {
			printUsageAndExit();
		}
	}

	/**
	 * Prints a simple usage message to stderr and terminates the program.
	 */
	private static void printUsageAndExit() {
		System.out.println("Usage:\n" + 
				"java MagicClientDriver <tcp|udp> <host> [port] -[flag]\n" +
				"or: java MagicClientDriver <tcp|udp> <host> -[flag]\n" +
				"or: java MagicClientDriver <tcp|udp> <host> [port]");
		System.exit(1);
	}

	/**
	 * Provides the entry point of the program.
	 * @param args - Command line arguments to the program. There must be 
	 * at least two arguments. The first argument must be either "tcp" 
	 * or "udp". The second argument must be the hostname or IP address 
	 * of a remote host running a magic server for the specified protocol. 
	 * The third parameter, if present is either the port number or the flag.
	 * The fourth argument, if present must be the flag (in which case the 
	 * third argument must be the port number).
	 */
	public static void main(String[] args) {
		/** Magic client driver to initialize a client */
		MagicClientDriver driver;
		try {
			switch(args.length) {
				case 4: { // All four arguments specified.
					driver = new MagicClientDriver(args[0], args[1],
							Integer.parseInt(args[2]), args[3]);
					driver.go();
				} break;
				case 3: { // Third argument can be flag or port.
					if (args[2].contains("-")) {
						driver = new MagicClientDriver(args[0], args[1], 
																args[2]);
						driver.go();
					} else {
						driver = new MagicClientDriver(args[0], args[1],
								Integer.parseInt(args[2]));
						driver.go();
					}
				} break;
				case 2: { // Protocol and Host specified.
					driver = new MagicClientDriver(args[0], args[1]);
					driver.go();
				} break;
				default: { // Wrong number of arguments.
					printUsageAndExit();
				}
			}
		} catch (NumberFormatException ex) {
			System.out.println(ex.getMessage());
			printUsageAndExit();
		} catch(IOException ex){
			System.out.println(ex.getMessage());
			printUsageAndExit();
		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
			printUsageAndExit();
		}
	}

	/**
	 * Runs the client, to send a request to the server.
	 * @throws ClassNotFoundException - If the common/Card class is not found.
	 * @throws IOException - If the client could not write to the server.
	 */
	public final void go() throws ClassNotFoundException, IOException{
		client.printToStream(System.out);
	}
}
