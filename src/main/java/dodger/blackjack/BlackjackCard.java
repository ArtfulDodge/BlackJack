package dodger.blackjack;

import dodger.blackjack.players.Player;
import dodger.cards.Card;

public class BlackjackCard extends Card
{
    // ---------------------------------------------------------------
    // Both constructors will just call the appropriate Card
    // constructor
    // ---------------------------------------------------------------
    public BlackjackCard()
    {
        super();
    }

    public BlackjackCard(int desiredSuit, int desiredValue)
    {
        super(desiredSuit, desiredValue);
    }

    public BlackjackCard(Card c)
    {
        this(c.getSuitNum(), c.getValue());
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
}
