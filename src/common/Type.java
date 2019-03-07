package common;

import java.io.Serializable;

/**
 * Enumeration class defines the 'Types' of cards that exist, these are:
 * Artifact, Creature, Land, Spell, and Unknown.
 * @author Kevin Filanowski
 * @author Donald Queen
 * @version October, 2018
 */
public enum Type implements Serializable, Comparable<Type> {

	ARTIFACT ("Artifact"),
	CREATURE ("Creature"),
	LAND     ("Land"),
	SPELL    ("Spell"),
	UNKNOWN  ("Unknown");

	/** Name of the card. */
	private final String name;

	/**
	 * Default Constructor for Type enumeration.
	 * @param name - The name of the type.
	 */
	private Type(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of the enum.
	 * @return The name of the enum.
	 */
	@Override
	public String toString() {
		return name;
	}
}
