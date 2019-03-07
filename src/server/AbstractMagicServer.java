package server;

import java.io.FileNotFoundException;

/**
 * An abstract class that contains fields and methods 
 * that may be common to implementations of the 'chargen' server.
 * @author Kevin Filanowski
 * @author Donny Queen
 * @version October 2018
 */
public abstract class AbstractMagicServer implements MagicServer {
	/** How many cards will be returned if all types are selected. */
	protected static final int THREE_TYPES = 60;
	/** How many cards will be returned if two types are selected. */
	protected static final int TWO_TYPES = 40;
	/** How many cards will be returned if there is only one type. */
	protected static final int ONE_TYPE = 20;
	/** The default port on which a magic server listens. */
	public static final int DEFAULT_PORT = 5892;
	/** The default number of items to send back. */
	public static final int NUM_ITEMS = 20;
	/** The default path to a csv file to make a card source */
	public static final String DEFAULT_FILE = "./cards.csv";
	/** The port to which the server should 
	 * listen for incoming connections. */
	private int port;
	/** The source of the character stream that the
	 * server will send to the client. */
	private CardSource source;
	/** The number of items to send back before ending connection. */
	private int numItems;

	/**
	 * Initializes a new AbstractMagicServer using the
	 * default port and the default source.
	 * @throws FileNotFoundException - If the source file cannot be found.
	 */
	public AbstractMagicServer() throws FileNotFoundException {
		this(new CardSource(DEFAULT_FILE));
	}

	/**
	 * Initializes a new AbstractMagicServer using the
	 * specified port and the default source.
	 * @param port - The port to which the server will bind and listen
	 * for incoming connections.
	 * @throws FileNotFoundException - If the source file cannot be found.
	 */
	public AbstractMagicServer(int port) throws FileNotFoundException {
		this(port, new CardSource(DEFAULT_FILE), NUM_ITEMS);		
	}

	/**
	 * Initializes a new AbstractMagicServer using the
	 * default port and the specified source.
	 * @param source - The source to use to send cards to connecting clients.
	 */
	public AbstractMagicServer(CardSource source) {
		this(DEFAULT_PORT, source, NUM_ITEMS);
	}

	/**
	 * Initializes a new AbstractMagicServer using the
	 * specified port and the specified source.
	 * @param port - The port to which the server will bind and listen
	 * for incoming connections.
	 * @param source - The source to use to send cards to connecting clients.
	 */
	public AbstractMagicServer(int port, CardSource source) {
		this(port, source, NUM_ITEMS);
	}

	/**
	 * Initializes a new AbstractMagicServer using the
	 * specified port and the specified source.
	 * @param port - The port to which the server will bind and listen
	 * for incoming connections.
	 * @param source - The source to use to send cards to connecting clients.
	 * @param numItems - The number of items to send over the network
	 * before closing the connection.
	 */
	public AbstractMagicServer(int port, CardSource source, int numItems) {
		this.port = port;
		this.source = source;
		this.numItems = numItems;
	}

	/**
	 * Get the port to which the server will bind and listen for
	 * incoming connections.
	 * @return The port to which the server will bind and
	 * listen for incoming connections.
	 */
	protected int getPort() {
		return port;
	}

	/**
	 * Get the CardSource to use for generating the character stream
	 * to send to clients.
	 * @return The CardSource used to generate the cards being sent to clients.
	 */
	protected CardSource getSource() {
		return source;
	}

	/**
	 * Change which source is being used to generate characters for the server.
	 * @param source - a CardSource used to generate cards.
	 */
	protected void changeSource(CardSource source) {
		this.source = source;
	}

	/**
	 * Change how many items to send back to the client.
	 * This number cannot fall below the default of 20 items.
	 * @param numItems - The number of items to send back to the client.
	 */
	protected void changeItemsToSend(int numItems) {
		this.numItems = Math.max(NUM_ITEMS, numItems);
	}

	/**
	 * Determine the current number of items we should be sending
	 * back to the client.
	 * @return The number of items to send back to the client.
	 */
	protected int getItemsToSend() {
		return numItems;
	}

	/**
	 * Determine the type (Spell, Creature, land, or some combination thereof)
	 * as well as the number returned to the client. Three types of cards
	 * returns 3 types 60 cards, 2 types 40, 1 type 20.
	 * @param command - the flag data returned from the server.
	 */
	protected void setCardsReturned(String command) {
		switch (command.trim()) {
	    	case "-L" :
		    	source.setCardType(CardType.LAND);
			    this.numItems = ONE_TYPE;
			    break;
		    case "-S" :
			    source.setCardType(CardType.SPELL);
			    this.numItems = ONE_TYPE;
			    break;
		    case "-C" :
			    source.setCardType(CardType.CREATURE);
			    this.numItems = ONE_TYPE;
			    break;	
	    	case "-LC" :
	    	case "-CL" :
	    		source.setCardType(CardType.LC);
	    		this.numItems = TWO_TYPES;
	    		break;
	    	case "-SC" :
	    	case "-CS" :
	    		source.setCardType(CardType.CS);
	    		this.numItems = TWO_TYPES;
	    		break;
	    	case "-LS" :
	    	case "-SL" :
		    	source.setCardType(CardType.LS);
		    	this.numItems = TWO_TYPES;
		    	break;
	    	default:
		    	source.setCardType(CardType.ALL);
		    	this.numItems = THREE_TYPES;
		    	break;
		}
	}

	/**
	 * Causes the magic server to listen for requests.
	 * @throws MagicServerException - if an error occurs while trying to
	 * listen for connections.
	 */
	public abstract void listen() throws MagicServerException;
}
