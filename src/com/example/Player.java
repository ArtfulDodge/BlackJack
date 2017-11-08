package com.example;

import java.util.*;

public class Player
{
    private ArrayList<Card> hand = new ArrayList<Card>();
    public int bet = 0;
    public int money;

    // ---------------------------------------------------------------
    // Default constructor
    // ---------------------------------------------------------------
    public Player()
    {
	money = Integer.MAX_VALUE;
    }

    // ---------------------------------------------------------------
    // Overloaded constructor
    // Allows you to set the amount of money the player starts with
    // ---------------------------------------------------------------
    public Player(int m)
    {
	money = m;
    }
    
    // ---------------------------------------------------------------
    // Adds the top card of the deck to the Player's hand
    // ---------------------------------------------------------------
    public void hit(Deck d)
    {
	hand.add(d.draw());
    }

    // ---------------------------------------------------------------
    // Gets the value of the player's hand based on the rules of
    // blackjack. Changes the values of aces from 11 to 1
    // automatically to avoid busting.
    // ---------------------------------------------------------------
    public int handValue()
    {
	int value = 0;
	int aces = 0;
	for (Card i : hand)
	{
	    if (i.getValue() == 1)
	    {
		aces++;
		value += 11;
	    } else if (i.getValue() > 10)
	    {
		value += 10;
	    } else
	    {
		value += i.getValue();
	    }

	    while (value > 21 && aces > 0)
	    {
		value -= 10;
		aces -= 1;
	    }
	}

	return value;
    }

    // ---------------------------------------------------------------
    // Returns the value of the Player's hand, minus the first
    // Card
    // ---------------------------------------------------------------
    public int showingValue()
    {
	int value = 0;
	int aces = 0;
	for (Card i : hand)
	{
	    if (hand.indexOf(i) == 0)
	    {
		value += 0;
	    } else if (i.getValue() == 1)
	    {
		aces++;
		value += 11;
	    } else if (i.getValue() > 10)
	    {
		value += 10;
	    } else
	    {
		value += i.getValue();
	    }

	    while (value > 21 && aces > 0)
	    {
		value -= 10;
		aces -= 1;
	    }
	}

	return value;
    }

    // ---------------------------------------------------------------
    // Returns all the Cards in a Player's hand
    // ---------------------------------------------------------------
    public String toString()
    {
	String result = "";
	for (int i = 0; i < hand.size(); i++)
	{
	    if (i == 0)
	    {
		result += hand.get(i);
	    } else
	    {
		result += ", " + hand.get(i);
	    }
	}

	return result;
    }

    // ---------------------------------------------------------------
    // Returns all the Cards in the Player's hand, minus the first
    // ---------------------------------------------------------------
    public String showing()
    {
	String result = "";
	for (int i = 0; i < hand.size(); i++)
	{
	    if (i == 0)
	    {
		result += "an unknown card";
	    } else
	    {
		result += ", " + hand.get(i);
	    }
	}

	return result;
    }

    // ---------------------------------------------------------------
    // Returns the card at the desired index in hand
    // ---------------------------------------------------------------
    public Card cardInPos(int index)
    {
	return hand.get(index);
    }

    // ---------------------------------------------------------------
    // Returns the number of Cards in hand
    // ---------------------------------------------------------------
    public int cardsInHand()
    {
	return hand.size();
    }

    // ---------------------------------------------------------------
    // Removes the last card in the Player's hand.
    // ---------------------------------------------------------------
    public void removeCard()
    {
	hand.remove(hand.size() - 1);
    }

    // ---------------------------------------------------------------
    // Adds the specified Card to the Player's hand
    // ---------------------------------------------------------------
    public void addCard(Card c)
    {
	hand.add(c);
    }

    // ---------------------------------------------------------------
    // Creates a card with the specified suit and value and adds
    // it to the player's hand
    // ---------------------------------------------------------------
    public void addCard(int suit, int value)
    {
	Card c = new Card(suit, value);
	hand.add(c);
    }

    // ---------------------------------------------------------------
    // Clears the Player's hand
    // ---------------------------------------------------------------
    public void clearHand()
    {
	hand = new ArrayList<Card>();
    }
}
