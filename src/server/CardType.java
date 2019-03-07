package server;

import java.io.Serializable;

/**
 * Simple Enumeration for Card Types
 * @author Kevin Filanowski
 * @author Donny Queen
 * @version October 2018
 */
public enum CardType implements Serializable, Comparable<CardType>{
	ALL(),
	CREATURE(),
	CS(),
	LAND(),
	LC(),
	LS(),
	SPELL();
}
