package common;

import java.io.Serializable;

/**
 * Class represents simple cards in the game of magic the gathering.
 * @author Kevin Filanowski
 * @author Donny Queen
 * @version October 2018
 */
public class Card implements Serializable {
	/** Name of the card */
	private String cardName;
	/** Card location in the input file */
	private short id;
	/** Energy required to use this card */
	private String mana;
	/** Which type of card (Artifact, Creature, Land, Spell, Unknown) */
	private Type type;

	/**
	 * Create a single card for magic the gathering.
	 * @param id - unique identifier (currently location in input file)
	 * @param name - name of the card.
	 * @param type - Type of card.
	 * @param mana - energy required to use the card.
	 */
	public Card(short id, String cardName, Type type, String mana) {
		this.id = id;
		this.cardName = cardName;
		this.type = type;
		this.mana = mana;
	}

	/**
	 * Create a single card for magic the gathering.
	 * @param id - unique identifier (currently location in input file)
	 * @param name - name of the card.
	 * @param type - String version of Type of card.
	 * @param mana - energy required to use the card.
	 */
	public Card(short id, String name, String type, String mana) {
		this.id = id;
		cardName = name;
		assignType(type);
		this.mana = mana;
	}

	/**
	 * Given a string representing a card type, change it into the enumeration
	 * version of the type (for safety and efficiency). Creature cards contain
	 * 'Creature' in the string. Land cards contain 'Land' in the string.
	 * Artifact cards contain 'Artifact' in the string. Spell cards contain 
	 * 'Sorcery', 'Enchantment', or 'Instant' in the string.
	 * All other cards are unknown.
	 * @param info - String representing the type.
	 */
	private final void assignType(String info) {
		for (Type c : Type.values()) {
			if (info.contains(c.toString())) {
				type = c;
			}
		}
		if (type == null) {
			if (info.contains("Enchantment") ||
					info.contains("Instant") || 
					info.contains("Sorcery")) {
				type = Type.SPELL;
			} else {
				type = Type.UNKNOWN;
			}
		}
	}

	/**
	 * Get card's unique id number.
	 * @return a card's unique id number.
	 */
	public short getId() {
		return id;
	}

	/**
	 * Get a card's name.
	 * @return A card's name.
	 */
	public String getName() {
		return cardName;
	}

	/**
	 * Get a card's Type.
	 * @return A card's Type.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Change a card's type.
	 * @param type - new type to assign to a card.
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * Returns a formatted string representation of a card.
	 * @return A formatted string containing the cards name, type, and mana.
	 */
	@Override
	public String toString() {
		return String.format("%30s: %11s (%5s)", cardName, type.toString(),
                             mana);
	}
}
