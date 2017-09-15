package com.example;

import java.util.*;

public class Player
{
    private  ArrayList<Card> hand = new ArrayList<Card>(); 
    
    //---------------------------------------------------------------
    //  Default constructor; Currently does nothing
    //---------------------------------------------------------------
    public Player()
    {
	
    }
    
    //---------------------------------------------------------------
    //  Adds the top card of the deck to the Player's hand
    //---------------------------------------------------------------
    public void hit(Deck d)
    {
	hand.add(d.draw());
    }
    
    //---------------------------------------------------------------
    //  Gets the value of the player's hand based on the rules of
    //  blackjack. Changes the values of aces from 11 to 1 
    //  automatically to avoid busting.
    //---------------------------------------------------------------
    public int getHandValue()
    {
	int value = 0;
	int aces = 0;
	for (Card i: hand)
	{
	    if(i.getValue() == 1)
	    {
		aces++;
		value += 11;
	    }else if(i.getValue() > 10)
	    {
		value += 10;
	    }else
	    {
		value += i.getValue();
	    }
	    
	    while(value > 21 && aces > 0)
	    {
		value -= 10;
		aces -= 1;
	    }
	}
	
	return value;
    }
    
    //---------------------------------------------------------------
    //  Returns all the cards in a player's hand; mostly for testing
    //  purposes at the moment
    //---------------------------------------------------------------
    public String toString()
    {
	return hand.toString();
    }
}
