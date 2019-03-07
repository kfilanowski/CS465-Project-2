package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import common.Card;
import common.Type;

/**
 * Class that defines the type of cards that can be returned
 * for a deck in Magic the Gathering.
 * @author Kevin Filanowski
 * @author Donny Queen
 * @version October 2018
 */
public class CardSource {
	/** Holds the cards */
	private ArrayList<Card> deck;
	/** Scan in data from the input file */
	private Scanner fileIn;
	/** Used to randomly choose cards */
	private Random generator;
	/** The types of cards we can return */
	private CardType type;

	/**
	 * Create a new CardSource object to store and choose cards to
	 * send back to client.
	 * @param filename - The name of the file to read in cards.
	 * @throws FileNotFoundException - if the input file cannot be found.
	 */
	public CardSource(String filename) throws FileNotFoundException {
		deck = new ArrayList<Card>();
		generator = new Random();
		fileIn = new Scanner(new File(filename));
		type = CardType.ALL;
		initDeck();
	}

	/**
	 * Read in the cards from the input file and place them in the deck.
	 */
	private final void initDeck() {
		// Holds each column in the .csv file of one row.
		String[] line;
		// Holds a single card.
		Card card;

		// Populate the deck with cards.
		while (fileIn.hasNextLine()) {
			line = fileIn.nextLine().split(",");
			card = new Card(Short.parseShort(line[0]),
					line[1], line[2], line[3]);
			if (valid(card)) {
				deck.add(card);
			}
		}
	}

	/**
	 * Change the type of card allowed to be sent back to the client.
	 * Allowed types are specified by @see CardType
	 * @param type - Type of card allowed to be sent via the network.
	 */
	protected void setCardType(CardType type) {
		this.type = type;
	}

	/**
	 * Displays the current deck to the screen.
	 */
	public void displayDeck() {
		for (Card c : deck) {
			System.out.println(c);
		}
	}

	/**
	 * Gets a randomly chosen card to return to the client.
	 * @return a randomly chosen card to return to the client.
	 */
	public Card next() {
		// The card to return to the client.
		Card result = null;

		// Return the card type that the client requests.
		while(!valid(result)) {
			result = deck.get(generator.nextInt(deck.size()));            
		}
		return result;
	}

	/**
	 * Determine if a card is suitable for returning to the client. A card
	 * is suitable if it is one of the type specified by the setCardType.
	 * @param card  - card to test for validity.
	 * @return true if the card is suitable, false otherwise.
	 */
	private boolean valid(Card card) {
		// If the card is null, it is not valid!
		if (card == null)
			return false;

		switch(this.type){
		case CREATURE:
			if (card.getType() == Type.CREATURE) { return true; } break;
		case LAND:
			if (card.getType() == Type.LAND) 	 { return true; } break;
		case SPELL:
			if (card.getType() == Type.SPELL) 	 { return true; } break;
			
		case CS: 
			if (card.getType() == Type.CREATURE
			|| card.getType() == Type.SPELL) 	 { return true; } break;
		case LC:
			if (card.getType() == Type.LAND 
			|| card.getType() == Type.CREATURE)  { return true; } break;
		case LS:
			if (card.getType() == Type.LAND 
			|| card.getType() == Type.SPELL) 	 { return true; } break;
		case ALL:
			if(card.getType() != Type.UNKNOWN
			&& card.getType() != Type.ARTIFACT)  { return true; } break;
		default: return false;
		}
		// If the card's type is unknown:
		return false;
	}
}
