package dodger.blackjack;

import java.util.Scanner;

import dodger.blackjack.players.Dealer;
import dodger.blackjack.players.Player;
import dodger.blackjack.players.Split;
import dodger.cards.Deck;

public class Game
{
    Deck d = new Deck();
    Player p;
    Split split;
    Dealer ai = new Dealer();
    String input = "";
    Scanner scan = new Scanner(System.in);

    // ---------------------------------------------------------------
    // Constructor
    // Starts the game and plays until the user runs out of money.
    // ---------------------------------------------------------------
    public Game() throws InterruptedException
    {
	long startingAmt = 0;
	do
	{
	    System.out.println("How much money do you start with?");
	    startingAmt = getNumericInput();

	    if (startingAmt <= 0)
	    {
		System.err.println("That number is too small! Minimum is $1.");
		Thread.sleep(2500);
	    }

	} while (startingAmt == 0);

	p = new Player(startingAmt);
	split = new Split(p);

	do
	{
	    playHand();
	    if (p.money > 0)
	    {
		System.out.println("Play another hand? (y/n)");
		input = getInput();
		System.out.println();
	    } else
	    {
		input = "n";
		Thread.sleep(2000);
		System.out.println();
	    }
	} while (input.substring(0, 1).equalsIgnoreCase("y"));

	System.out.println("Thanks for playing!");
	System.out.println("You finished with $" + p.money + "!");
    }

    // ---------------------------------------------------------------
    // Plays a hand of blackjack
    // ---------------------------------------------------------------
    private void playHand() throws InterruptedException
    {
	reset();

	do
	{
	    System.out.println("You have $" + p.money + ".");
	    System.out.println("How much do you bet? (Whole dollar amounts only)");
	    p.increaseBet(getNumericInput());

	    if (p.getBet() <= 0)
	    {
		System.err.println("That bet is too small! Minimum bet is $1.");
		Thread.sleep(2500);
	    }

	} while (p.getBet() <= 0);

	potStatus();

	// Populating the player's hands
	p.hit(d);
	ai.hit(d);
	p.hit(d);
	ai.hit(d);

	// Detecting if the player wins by Blackjack
	if (p.handValue() == 21 && ai.handValue() != 21)
	{
	    gameStatus();
	    System.out.print("You got a blackjack! ");
	    pVictory();
	    return;
	} else if (p.handValue() == 21 && ai.handValue() == 21)
	{
	    gameStatus();
	    System.out.println("You both got blackjacks!");
	    draw();
	    return;
	}

	// Loop that allows the player to actually play the game.
	// Plays the player's first hand.
	do
	{
	    gameStatus();

	    // Can the player split? If so, give them the option to.
	    // Otherwise play as normal.
	    if (p.cardsInHand() == 2)
	    {
		if (p.cardInPos(0).getBlackjackValue(p) == p.cardInPos(1).getBlackjackValue(p)
			&& split.cardsInHand() == 0)
		{
		    System.out.print("Hit, Stand, double down, or Split? (Note: Splitting will double your bet)");

		} else
		{
		    System.out.print("Hit, Stand, or Double down?");
		}
	    } else
	    {
		System.out.print("Hit or Stand?");
	    }

	    if (split.cardsInHand() > 0)
	    {
		System.out.println(" (First hand)");
	    } else
	    {
		System.out.println();
	    }

	    input = getInput();

	    System.out.println();

	    doAction(input, p);

	    if (p.handValue() > 21)
	    {
		if (split.cardsInHand() == 0)
		{
		    gameStatus();
		    System.out.println("You busted!");
		    aiVictory();
		    return;
		} else
		{
		    gameStatus();
		    System.out.println("Your first hand busted!");
		    System.out.println();
		    Thread.sleep(2500);
		}
	    }
	    // Detects what their input starts with instead of the whole word as a
	    // form of resilience against typos.
	} while ((input.substring(0, 1).equalsIgnoreCase("h") || input.substring(0, 2).equalsIgnoreCase("sp"))
		&& p.handValue() < 21);

	// Plays the second hand if the player decided to split. Does the same thing
	// as the first loop, minus giving the option to split.
	if (split.cardsInHand() > 0)
	{
	    do
	    {
		gameStatus();

		if (split.cardsInHand() == 2)
		{
		    System.out.println("Hit, Stand, or Double down? (Second hand)");
		} else
		{
		    System.out.println("Hit or Stand?");
		}

		input = getInput();

		System.out.println();

		doAction(input, split);

		if (split.handValue() > 21)
		{
		    if (p.handValue() > 21)
		    {
			gameStatus();
			System.out.println("Both your hands busted!");
			aiVictory();
			return;
		    }

		    System.out.println("Your second hand busted!");
		    Thread.sleep(2500);
		}
	    } while ((input.substring(0, 1).equalsIgnoreCase("h") || input.substring(0, 1).equalsIgnoreCase("sp"))
		    && split.handValue() < 21);
	}

	// Dealer's turn
	// The Dealer is actually SUPER complex in its decision making
	// The Dealer will hit as long as its hand's value is 17 or lower, or if it's too
	// low to beat and/or tie the player.
	// The Dealer can not split.
	while (((ai.handValue() < p.handValue() || (ai.handValue() < split.handValue() && !split.isBusted())) || ai.handValue() <= 17)
		&& ai.handValue() != 21)
	{
	    gameStatus();

	    Thread.sleep(2500);

	    System.out.println();

	    ai.hit(d);

	    if (ai.handValue() > 21)
	    {
		gameStatus();
		System.out.println("The Dealer busted!");
		pVictory();
		return;
	    }
	}

	if (split.cardsInHand() > 0)
	{
	    System.out.println("Your first hand: " + p + ". Value: " + p.handValue());
	    System.out.println("Your second hand: " + split + ". Value: " + split.handValue());
	} else
	{
	    System.out.println("Your hand: " + p + ". Value: " + p.handValue());
	}
	System.out.println("The Dealer's hand: " + ai.toString(true) + ". Value: " + ai.handValue());

	// Checking to see who wins.
	// Defaults to Dealer winning if the player doesn't win and it's not a draw.
	// (In other words any wonky bullshit defaults to an Dealer victory)
	if ((p.handValue() > ai.handValue() && p.handValue() <= 21)
		|| (split.handValue() > ai.handValue() && split.handValue() < 21))
	{
	    pVictory();
	    return;
	}

	if (p.handValue() == ai.handValue() || split.handValue() == ai.handValue())
	{
	    if (ai.handValue() == 21 && ai.cardsInHand() == 2)
	    {
		System.out.print("The Dealer had a blackjack! ");
		aiVictory();
		return;
	    }
	    draw();
	    return;
	} else
	{
	    aiVictory();
	    return;
	}
    }

    // ---------------------------------------------------------------
    // Resets the game for the next hand
    // ---------------------------------------------------------------
    private void reset()
    {
	if (d.usedCards() >= 26)
	{
	    d.Shuffle();
	    System.out.println("The deck has been shuffled!");
	}
	p.clearHand();
	split.clearHand();
	ai.clearHand();
	p.resetBet();
	split.resetBet();
	ai.resetBet();
    }

    // ---------------------------------------------------------------
    // aiVictory(), pVictory(), and draw() just print out the
    // proper things for each scenario
    // ---------------------------------------------------------------
    private void aiVictory()
    {
	System.out.println("The Dealer wins the pot!");
	ai.money += pot();
	return;
    }

    private void pVictory()
    {
	System.out.println("You win the pot!");
	p.money += pot();
	return;
    }

    private void draw()
    {
	System.out.println("It's a draw!");
	p.money += p.getBet() + split.getBet();
	return;
    }

    // ---------------------------------------------------------------
    // Outputs the status of the pot.
    // ---------------------------------------------------------------
    private void potStatus() throws InterruptedException
    {
	System.out.println("The pot is now $" + pot());

	Thread.sleep(1250);

	ai.increaseBet(p.getBet() + split.getBet() - ai.getBet());
	System.out.println();

	System.out.println("The Dealer matched your bet!");
	System.out.println("The pot is now $" + pot());

	System.out.println();
    }

    // ---------------------------------------------------------------
    // Returns the cards in each Player's hands and the values of
    // those hands as they are known to the user.
    // ---------------------------------------------------------------
    private void gameStatus()
    {
	if (split.cardsInHand() > 0)
	{
	    System.out.println("Your first hand: " + p + ". Value: " + p.handValue());
	    System.out.println("Your second hand: " + split + ". Value: " + split.handValue());
	} else
	{
	    System.out.println("Your hand: " + p + ". Value: " + p.handValue());
	}

	System.out.print("The Dealer's hand: " + ai + ". ");
	if (ai.handValue() > 21)
	{
	    System.out.println("Value: " + ai.valueToString());
	} else
	{
	    System.out.println("Known value: " + ai.valueToString());
	}
    }

    // ---------------------------------------------------------------
    // Reads the user's input and does the desired action
    // ---------------------------------------------------------------
    private void doAction(String i, Player pl) throws InterruptedException
    {
	if (i.substring(0, 1).equalsIgnoreCase("d") && pl.cardsInHand() == 2)
	{
	    i = "stand";
	    pl.doubleBet();
	    potStatus();
	    pl.hit(d);
	}

	if (i.substring(0, 1).equalsIgnoreCase("h"))
	{
	    pl.hit(d);
	}

	if (input.substring(0, 2).equalsIgnoreCase("sp")
		&& (p.cardInPos(0).getBlackjackValue(pl) == pl.cardInPos(1).getBlackjackValue(pl)
			&& split.cardsInHand() == 0))
	{
	    split.addCard(p.cardInPos(1));
	    pl.removeCard();
	    pl.hit(d);
	    split.hit(d);
	    pl.doubleBet();
	    potStatus();
	}
    }

    // ---------------------------------------------------------------
    // Gets user input as a long
    // ---------------------------------------------------------------
    private long getNumericInput()
    {
	long result = 0;
	boolean isLong = false;
	do
	{
	    input = getInput();
	    System.out.println();
	    try
	    {
		if (input.contains("."))
		{
		    result = Long.parseLong(input.substring(0, input.indexOf(".")).replaceAll("[\\D]", ""));
		} else
		{
		    result = Long.parseLong(input.replaceAll("[\\D]", ""));
		}

		isLong = true;
	    } catch (NumberFormatException e)
	    {
		System.err.println("I can only read numbers! Please try again:");
		isLong = false;
	    }
	} while (!isLong);

	return result;
    }

    // ---------------------------------------------------------------
    // Gets user input as a String
    // ---------------------------------------------------------------
    private String getInput()
    {
	String result = "";
	do
	{
	    result = scan.nextLine();
	} while (result.equals(""));

	while (result.length() < 3)
	{
	    result += " ";
	}
	
	return result;
    }
    
    // ---------------------------------------------------------------
    // Returns the current pot
    // ---------------------------------------------------------------
    private long pot()
    {
	return ai.getBet() + p.getBet() + split.getBet();
    }
}
