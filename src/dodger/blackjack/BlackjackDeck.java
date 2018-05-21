package dodger.blackjack;

import dodger.cards.Deck;

public class BlackjackDeck extends Deck
{
    // ---------------------------------------------------------------
    // Default constructor; Generates a complete deck of 52 unique
    // Cards
    // ---------------------------------------------------------------
    public BlackjackDeck()
    {
        deck = new BlackjackCard[52];
        int index = 0;
        for(int i = 0; i < 4; i++)
        {
            for(int j = 1; j <= 13; j++)
            {
                deck[index++] = new BlackjackCard(i, j);
            }
        }
        Shuffle();
    }

    // ---------------------------------------------------------------
    // Calls the Deck draw method and converts the output to a
    // BlackjackCard
    // ---------------------------------------------------------------
    public BlackjackCard draw()
    {
        return new BlackjackCard(super.draw());
    }
}
