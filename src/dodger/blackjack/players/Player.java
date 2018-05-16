package dodger.blackjack.players;

import java.util.*;

import dodger.cards.Card;
import dodger.cards.Deck;

public class Player
{
    protected ArrayList<Card> hand = new ArrayList<Card>();
    protected long bet = 0;
    public long money;

    // ---------------------------------------------------------------
    // Constructor
    // Requires you to set the amount of money the player starts with
    // ---------------------------------------------------------------
    public Player(long m)
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
    @SuppressWarnings("Duplicates")
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
    // Increases the Player's bet by the given amount
    // ---------------------------------------------------------------
    public void increaseBet(long amount)
    {
        if (money < amount)
        {
            System.out.println("That bet is too large! Betting max amount instead.");
            bet += money;
            money = 0;
        } else
        {
            bet += amount;
            money -= amount;
        }
    }

    // ---------------------------------------------------------------
    // Doubles the Player's bet
    // ---------------------------------------------------------------
    public void doubleBet()
    {
        increaseBet(bet);
    }

    // ---------------------------------------------------------------
    // Resets the Player's bet to 0
    // ---------------------------------------------------------------
    public void resetBet()
    {
        bet = 0;
    }

    // ---------------------------------------------------------------
    // Returns the value of bet
    // ---------------------------------------------------------------
    public long getBet()
    {
        return bet;
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
