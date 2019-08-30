package dodger.cards;

public class Deck
{
    protected Card[] deck;
    protected int deckIndex = 0;
    protected int deckSize = 52;

    // ---------------------------------------------------------------
    // Default constructor; Generates a complete deck of 52 unique
    // Cards
    // ---------------------------------------------------------------
    public Deck()
    {
        deck = new Card[deckSize];
        int index = 0;
        for (int i = 0; i < 4; i++)
        {
            for (int j = 1; j <= 13; j++)
            {
                deck[index++] = new Card(i, j);
            }

        }
        Shuffle();
    }

    // ---------------------------------------------------------------
    // Constructor; Sets deck equal to the given card array
    // DOES NOT SHUFFLE
    // ---------------------------------------------------------------
    public Deck(Card[] cards)
    {
        deck = cards;
    }

    // ---------------------------------------------------------------
    // Shuffles the deck
    // ---------------------------------------------------------------
    public void Shuffle()
    {
        for (int i = 0; i < deckSize; i++)
        {
            int j = (int) (Math.random() * deckSize);
            Card tmp = deck[j];
            deck[j] = deck[i];
            deck[i] = tmp;
        }
        deckIndex = 0;
    }

    // ---------------------------------------------------------------
    // Outputs every Card in the deck in order as a String.
    // ---------------------------------------------------------------
    public String toString()
    {
        String d = "";
        for (int i = 0; i < deckSize; i++)
        {
            d = d + deck[i] + "\n";
        }
        return d;
    }

    // ---------------------------------------------------------------
    // Simulates returning a card to the top of the deck by
    // subtracting one from deckIndex
    // ---------------------------------------------------------------
    public void returnCard()
    {
        deckIndex--;
    }

    // ---------------------------------------------------------------
    // Returns the Card at deck[deckIndex] and increments deckIndex
    // to simulated drawing a card from the top of the deck.
    // ---------------------------------------------------------------
    public Card draw()
    {
        if (deckIndex > deck.length)
        {
            Shuffle();
            return draw();
        }
        return deck[deckIndex++];
    }

    // ---------------------------------------------------------------
    // Returns how many cards have been used so far
    // ---------------------------------------------------------------
    public int usedCards()
    {
        return deckIndex;
    }

    // ---------------------------------------------------------------
    // Returns the size of the deck
    // ---------------------------------------------------------------
    public int getDeckSize()
    {
        return deckSize;
    }
}
