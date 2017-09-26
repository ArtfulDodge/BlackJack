package com.example;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main
{
    // Creating needed objects and variables
    static Deck d = new Deck();
    static Player p = new Player();
    static Player split = new Player();
    static Player ai = new Player();
    static String input = "yes";
    static Scanner scan = new Scanner(System.in);
    static int money = 500;

    public static void main(String args[]) throws InterruptedException
    {
	while (input.substring(0, 1).equalsIgnoreCase("y"))
	{
	    input = "hit";
	    playHand();
	    if (money > 0)
	    {
		System.out.println("Play another hand? (y/n)");
		input = scan.nextLine();
		System.out.println();
	    } else
	    {
		input = "n";
		TimeUnit.SECONDS.sleep(2);
		System.out.println();
	    }
	}

	System.out.println("Thanks for playing!");
	System.out.println("You finished with $" + money + "!");
	scan.close();
    }

    //---------------------------------------------------------------
    //  Plays a hand of blackjack
    //---------------------------------------------------------------
    private static void playHand() throws InterruptedException
    {
        reset();
        while (p.bet <= 0)
        {
            System.out.println("You have $" + money + ".");
            System.out.println("How much do you bet? (whole dollar amounts only)");
            input = scan.nextLine();
    
            try
            {
        	if (input.contains("."))
        	    p.bet = Integer.parseInt(input.substring(0, input.indexOf(".")).replaceAll("[\\D]", ""));
        	else
        	    p.bet = Integer.parseInt(input.replaceAll("[\\D]", ""));
        	
            } catch (NumberFormatException e)
            {
        	System.out.println("I can only read numbers! Please try again: ");
        	System.out.println();
            }
    
            if (p.bet <= 0)
            {
        	System.out.println("That bet is too small! Minimum bet is $1.");
        	System.out.println();
        	TimeUnit.SECONDS.sleep(2);
            }
        }
    
        if (p.bet > money)
        {
            System.out.println("That bet is too large! Betting max amount instead.");
            TimeUnit.SECONDS.sleep(2);
            p.bet = money;
        }
    
        money -= p.bet;
    
        potStatus();
    
        // Populating the player's hands
        p.hit(d);
        ai.hit(d);
        p.hit(d);
        ai.hit(d);
    
        if (p.handValue() == 21 && ai.handValue() != 21)
        {
            gameStatus();
            System.out.print("You got a blackjack! ");
            pVictory();
            return;
        } else if (p.handValue() == 21 && ai.handValue() == 21)
        {
            draw();
            return;
        }
    
        input = "hit";
    
        // Loop that allows the player to actually play the game
        // Detects what their input starts with instead of the whole word as a
        // form of resilience against typos.
        while ((input.substring(0, 1).equalsIgnoreCase("h") || input.substring(0, 2).equalsIgnoreCase("sp"))
        	&& p.handValue() < 21)
        {
            gameStatus();
    
            // Can the player split? If so, give them the option to. Otherwise play as
            // normal.
            if (p.cardsInHand() == 2)
            {
        	if (p.cardInPos(0).getBlackjackValue(p) == p.cardInPos(1).getBlackjackValue(p) && split.cardsInHand() == 0)
        	{
        	    System.out.println("Hit, Stand, double down, or Split? (Note: Splitting will double your bet)");
        	    input = scan.nextLine();
    
        	    if (input.substring(0, 2).equalsIgnoreCase("sp"))
        	    {
        		split.addCard(p.cardInPos(1));
        		p.removeCard();
        		p.hit(d);
        		split.hit(d);
        		split.bet = p.bet;
        		money -= split.bet;
        		potStatus();
        	    }
        	} else
        	{
        	    if (split.cardsInHand() > 0)
        	    {
        		System.out.println("Hit, Stand, or Double down? (First hand)");
        		input = scan.nextLine();
        	    } else
        	    {
        		System.out.println("Hit, Stand, or Double down?");
        		input = scan.nextLine();
        	    }
        	}
            } else
            {
        	if (split.cardsInHand() > 0)
        	{
        	    System.out.println("Hit or Stand? (First hand)");
        	    input = scan.nextLine();
        	} else
        	{
        	    System.out.println("Hit or Stand?");
        	    input = scan.nextLine();
        	}
            }
            System.out.println();
    
            if (input.substring(0, 1).equalsIgnoreCase("d"))
            {
        	input = "stand";
        	doubleBet(p);
        	potStatus();
        	p.hit(d);
            }
    
            if (input.substring(0, 1).equalsIgnoreCase("h"))
            {
        	p.hit(d);
            }
    
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
        	    TimeUnit.SECONDS.sleep(2);
        	}
            }
        }
    
        // Plays the second hand if the player decided to split
        if (split.cardsInHand() > 0)
        {
    
            input = "hit";
    
            while ((input.substring(0, 1).equalsIgnoreCase("h") || input.substring(0, 1).equalsIgnoreCase("sp"))
        	    && split.handValue() < 21)
            {
        	gameStatus();
    
        	System.out.println("Hit, Stand, or Double down? (Second hand)");
        	input = scan.nextLine();
    
        	System.out.println();
        	
        	 if (input.substring(0, 1).equalsIgnoreCase("d"))
        	    {
        		input = "stand";
        		doubleBet(split);
        		potStatus();
        		split.hit(d);
        	    }
    
        	if (input.substring(0, 1).equalsIgnoreCase("h"))
        	{
        	    split.hit(d);
        	}
    
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
        	    TimeUnit.SECONDS.sleep(2);
        	}
            }
        }
    
        // AI's turn
        // The AI is actually SUPER complex in its decision making (note: this is
        // sarcasm)
        // The AI will hit as long as its hand's value is 17 or lower, or if it's too
        // low to beat the player.
        // The AI can not split.
        while (((ai.handValue() < p.handValue() || ai.handValue() < split.handValue()) || ai.handValue() <= 17)
        	&& ai.handValue() != 21)
        {
            gameStatus();
    
            TimeUnit.SECONDS.sleep(2);
    
            System.out.println();
    
            ai.hit(d);
    
            if (ai.handValue() > 21)
            {
        	gameStatus();
        	System.out.println("Your opponent busted!");
        	pVictory();
        	return;
            }
        }
    
        if (split.cardsInHand() > 0)
        {
            System.out.println("Your first hand: " + p + ". Value: " + p.handValue());
            System.out.println("Your second hand: " + split + ". Value: " + split.handValue());
            System.out.println("Your opponent's hand: " + ai + ". Value: " + ai.handValue());
        } else
        {
            System.out.println("Your hand: " + p + ". Value: " + p.handValue());
            System.out.println("Your opponent's hand: " + ai + ". Value: " + ai.handValue());
        }
    
        // Checking to see who wins.
        // Defaults to AI winning if the player doesn't win and it's not a draw.
        // (In other words any wonky bullshit defaults to an AI victory)
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
        	System.out.print("Your opponent had a blackjack! ");
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
    private static void reset()
    {
        if (d.usedCards() >= 26)
        {
            d.Shuffle();
            System.out.println("The deck has been shuffled!");
        }
        p = new Player();
        split = new Player();
        ai = new Player();
    }

    // aiVictory(), pVictory(), and draw() just print out the 
    // proper things for each scenario and do the necessary
    // behind the scenes things.
    private static void aiVictory()
    {
	System.out.println("Your opponent wins the pot!");
	p.bet = 0;
	split.bet = 0;
	ai.bet = 0;
	return;
    }

    private static void pVictory()
    {
	System.out.println("You win the pot!");
	money += ai.bet + p.bet + split.bet;
	p.bet = 0;
	split.bet = 0;
	ai.bet = 0;
	return;
    }

    private static void draw()
    {
	System.out.println("It's a draw!");
	money += p.bet + split.bet;
	p.bet = 0;
	split.bet = 0;
	ai.bet = 0;
	return;
    }

    // ---------------------------------------------------------------
    // Outputs the status of the pot.
    // ---------------------------------------------------------------
    private static void potStatus() throws InterruptedException
    {
        System.out.println();
        System.out.println("The pot is now $" + (p.bet + split.bet + ai.bet));
    
        TimeUnit.SECONDS.sleep(1);
        
        ai.bet = p.bet + split.bet;
        System.out.println();
    
        System.out.println("The AI matched your bet!");
        System.out.println("The pot is now $" + (p.bet + split.bet + ai.bet));
    
        System.out.println();
    }

    // gameStatus() exists for readability reasons.
    // Imagine if everywhere you saw gameStatus() now you instead saw what was
    // inside gameStatus(). It's awful, isn't it?
    private static void gameStatus()
    {
	int acedShowing = ai.handValue() - ai.cardInPos(0).getBlackjackValue(ai);

	if (split.cardsInHand() > 0)
	{
	    if (ai.handValue() > 21)
	    {
		System.out.println("Your first hand: " + p + ". Value: " + p.handValue());
		System.out.println("Your second hand: " + split + ". Value: " + split.handValue());
		System.out.println("Your opponent's hand: " + ai + ". Value: " + ai.handValue());
	    } else if (ai.showingValue() == acedShowing)
	    {
		System.out.println("Your first hand: " + p + ". Value: " + p.handValue());
		System.out.println("Your second hand: " + split + ". Value: " + split.handValue());
		System.out.println("Your opponent's hand: " + ai.showing() + ". Showing: " + ai.showingValue());
	    } else if (ai.showingValue() >= 21)
	    {
		System.out.println("Your first hand: " + p + ". Value: " + p.handValue());
		System.out.println("Your second hand: " + split + ". Value: " + split.handValue());
		System.out.println("Your opponent's hand: " + ai.showing() + ". Showing: " + acedShowing);
	    } else
	    {
		System.out.println("Your first hand: " + p + ". Value: " + p.handValue());
		System.out.println("Your second hand: " + split + ". Value: " + split.handValue());
		System.out.println("Your opponent's hand: " + ai.showing() + ". Showing: " + acedShowing + " or "
			+ ai.showingValue());
	    }
	} else
	{
	    if (ai.handValue() > 21)
	    {
		System.out.println("Your hand: " + p + ". Value: " + p.handValue());
		System.out.println("Your opponent's hand: " + ai + ". Value: " + ai.handValue());
	    } else if (ai.showingValue() == acedShowing)
	    {
		System.out.println("Your hand: " + p + ". Value: " + p.handValue());
		System.out.println("Your opponent's hand: " + ai.showing() + ". Showing: " + ai.showingValue());
	    } else if (ai.showingValue() >= 21)
	    {
		System.out.println("Your hand: " + p + ". Value: " + p.handValue());
		System.out.println("Your opponent's hand: " + ai.showing() + ". Showing: " + acedShowing);
	    } else
	    {
		System.out.println("Your hand: " + p + ". Value: " + p.handValue());
		System.out.println("Your opponent's hand: " + ai.showing() + ". Showing: " + acedShowing + " or "
			+ ai.showingValue());
	    }
	}
    }

    // ---------------------------------------------------------------
    // Doubles the player's bet
    // ---------------------------------------------------------------
    private static void doubleBet(Player play)
    {
	if (play.bet > money)
	{
	    System.out.println("Not enough money to double bet! Betting max amount insted!");
	    play.bet += money;
	    money = 0;
	} else
	{
	    money -= play.bet;
	    play.bet *= 2;
	}
    }
}
