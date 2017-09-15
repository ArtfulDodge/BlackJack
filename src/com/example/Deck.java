package com.example;

public class Deck
{
    private Card[] deck;

    private int deckIndex;
    
    //---------------------------------------------------------------
    //  Default constructor; Generates a complete deck of 52 unique
    //  Cards
    //---------------------------------------------------------------
    public Deck()
    {
	deck = new Card[52];
	int index = 0;
	for (int i = 0; i < 4; i++)
	{
	    for (int j = 1; j < 14; j++)
	    {
		deck[index] = new Card(i, j);
		index++;
	    }

	}
	Shuffle();
    }

    //---------------------------------------------------------------
    //  Shuffles the deck
    //---------------------------------------------------------------
    public void Shuffle()
    {
	for(int i = 0; i < 52; i++)
	{
	   int j = (int)(Math.random() * 52);
	   Card tmp = deck[j];
	   deck[j] = deck[i];
	   deck[i] = tmp;
	}
	deckIndex = 0;
    }
    
    //---------------------------------------------------------------
    //  Outputs every Card in the deck in order as a String.
    //---------------------------------------------------------------
    public String toString()
    {
	String d = "";
	for (int i = 0; i < 52; i++)
	{
	    d = d + deck[i] + "\n";
	}
	return d;
    }
    
    //----------------------------------------------------------------
    //  Returns the Card at deck[deckIndex] and increments deckIndex 
    //  to simulated drawing a card from the top of the deck.
    //----------------------------------------------------------------
    public Card draw()
    {
	deckIndex++;
	if (deckIndex > 52)
	{
	    Shuffle();
	    return draw();
	}
	return deck[deckIndex - 1];
    }
}
