package com.example;

public class Dealer extends Player
{
    public Dealer()
    {
	super(Long.MAX_VALUE);
    }

    // ---------------------------------------------------------------
    // Returns either all the cards in the Dealer's hand or all the
    // cards in the Dealer's hand minus the first depending on
    // context.
    // ---------------------------------------------------------------
    public String toString()
    {
	if (handValue() <= 21)
	{
	    return showing();
	} else
	{
	    return super.toString();
	}
    }
    
    // ---------------------------------------------------------------
    // Only used for the final hand reveal. Shows the whole hand if
    // isFinal is true.
    // ---------------------------------------------------------------
    public String toString(Boolean isFinal)
    {
	if (isFinal)
	{
	    return super.toString();
	} else
	{
	    return toString();
	}
    }
    
    // ---------------------------------------------------------------
    // Returns all the Cards in the Dealer's hand, minus the first
    // ---------------------------------------------------------------
    private String showing()
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
    // Returns the value of the Dealer's hand, minus the first
    // Card
    // ---------------------------------------------------------------
    private int showingValue()
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
    // Returns the hand value as known to the user based on context.
    // ---------------------------------------------------------------
    public String valueToString()
    {
	int acedShowing = handValue() - cardInPos(0).getBlackjackValue(this);
	if (handValue() > 21)
	{
	    return Integer.toString(handValue());
	}
	
	if (showingValue() >= 21)
	{
	    return Integer.toString(acedShowing);
	}
	
	if (showingValue() > acedShowing)
	{
	    return Integer.toString(acedShowing) + " or " + Integer.toString(showingValue());
	}
	
	return Integer.toString(showingValue());
    }
}
