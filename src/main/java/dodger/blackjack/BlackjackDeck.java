package dodger.blackjack;

import dodger.cards.Deck;

public class BlackjackDeck extends Deck
{

    // ---------------------------------------------------------------
    // Default constructor; Generates a deck made up of 52 unique
    // cards
    // ---------------------------------------------------------------
    public BlackjackDeck()
    {
        deck = new BlackjackCard[deckSize];
        int index = 0;
        for(int i = 0; i < 4; i++) {
            for (int j = 1; j <= 13; j++) {
                deck[index++] = new BlackjackCard(i, j);
                if (index % 52 == 0 && index != 52*4)
                {
                    i = 0;
                    j = 0;
                }

            }
        }

        Shuffle();
    }

    // ---------------------------------------------------------------
    // Generates a deck with a number of cards equal to size
    // ---------------------------------------------------------------
    public BlackjackDeck(int size)
    {
        deckSize = size;
        deck = new BlackjackCard[deckSize];
        int index = 0;
        for(int i = 0; i < 4; i++) {
            for (int j = 1; j <= 13; j++) {
                deck[index++] = new BlackjackCard(i, j);
                if (index % 52 == 0 && index != 52*4)
                {
                    i = 0;
                    j = 0;
                }

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
