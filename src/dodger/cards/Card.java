package dodger.cards;

import dodger.blackjack.players.Player;

public class Card
{
    private int value;
    private int suitNum;
    private String suit;
    private String faceValue;

    private final String HEARTS = "hearts";
    private final String DIAMONDS = "diamonds";
    private final String SPADES = "spades";
    private final String CLUBS = "clubs";

    // ---------------------------------------------------------------
    // Default constructor; Creates a Card with a random suit
    // and value.
    // ---------------------------------------------------------------
    public Card()
    {
        suitNum = (int) (Math.random() * 4);
        value = 1 + (int) (Math.random() * 13);
        Decode();
    }

    // ---------------------------------------------------------------
    // Overloaded constructor; Creates a Card with the desired suit
    // and value.
    // ---------------------------------------------------------------
    public Card(int desiredSuit, int desiredValue)
    {
        suitNum = desiredSuit;
        value = desiredValue;
        Decode();
    }

    // ---------------------------------------------------------------
    // "Decodes" the integers into meaningful Strings.
    // ---------------------------------------------------------------
    private void Decode()
    {
        if (suitNum == 0)
        {
            suit = HEARTS;
        }

        if (suitNum == 1)
        {
            suit = DIAMONDS;
        }

        if (suitNum == 2)
        {
            suit = SPADES;
        }

        if (suitNum == 3)
        {
            suit = CLUBS;
        }

        if (value == 11)
        {
            faceValue = "jack";
        } else if (value == 12)
        {
            faceValue = "queen";
        } else if (value == 13)
        {
            faceValue = "king";
        } else if (value == 1)
        {
            faceValue = "ace";
        } else
        {
            faceValue = Integer.toString(value);
        }
    }

    // ---------------------------------------------------------------
    // Returns the suit of the Card as a string
    // ---------------------------------------------------------------
    public String getSuit()
    {
        return suit;
    }

    // ---------------------------------------------------------------
    // Returns the value of the Card as an integer
    // ---------------------------------------------------------------
    public int getValue()
    {
        return value;
    }

    // ---------------------------------------------------------------
    // Returns the value of the Card as it would be in most games as
    // an integer.
    // ---------------------------------------------------------------
    public int getGameValue()
    {
        if (value == 1)
        {
            return 11;
        }
        return value;
    }

    // ---------------------------------------------------------------
    // Returns the value of the card according to the rules of
    // Blackjack
    // ---------------------------------------------------------------
    public int getBlackjackValue(Player p)
    {
        if (value > 10)
        {
            return 10;
        }

        if (value == 1)
        {
            if (p.handValue() + 10 <= 21)
            {
                return 11;
            }
        }

        return value;
    }

    // ---------------------------------------------------------------
    // Returns the face value (faceValue) of the Card
    // as a string.
    // ---------------------------------------------------------------
    public String getFaceValue()
    {
        return faceValue;
    }

    // ---------------------------------------------------------------
    // Returns the faceValue and suit of the Card in the
    // form of "faceValue of suit"
    // ---------------------------------------------------------------
    public String toString()
    {
        return faceValue + " of " + suit;
    }
}