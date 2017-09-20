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
    static String action = "hit";
    static Scanner scan = new Scanner(System.in);

    public static void main(String args[]) throws InterruptedException
    {
	// Populating the player's hands
	p.hit(d);
	ai.hit(d);
	p.hit(d);
	ai.hit(d);

	// Loop that allows the player to actually play the game
	// Detects what their desired action starts with instead of the whole word as a
	// form of resilience against typos.
	while ((action.startsWith("h") || action.startsWith("sp")) && p.handValue() < 21)
	{
	    gameStatus();

	    // Can the player split? If so, give them the option to. Otherwise play as normal.
	    if (p.cardsInHand() == 2 && p.cardInPos(0).getFaceValue().equals(p.cardInPos(1).getFaceValue()))
					// Look at this fucking abomination of a boolean expression.
					// It's 2x longer than this sentence.
	    {
		System.out.println("Hit, Stand, or Split?");
		action = scan.nextLine();

		if (action.equalsIgnoreCase("split"))
		{
		    split.addCard(p.cardInPos(1));
		    p.removeCard();
		    p.hit(d);
		    split.hit(d);
		}
	    } else
	    {
		if (split.cardsInHand() > 0)
		{
		    System.out.println("Hit or Stand? (First hand)");
		    action = scan.nextLine();
		} else
		{
		    System.out.println("Hit or Stand?");
		    action = scan.nextLine();
		}
	    }

	    System.out.println();

	    if (action.startsWith("h"))
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
		    scan.close();
		    return;
		} else
		{
		    gameStatus();
		    System.out.println("Your first hand busted!");
		    TimeUnit.SECONDS.sleep(2);
		}
	    }
	}

	// Makes it a little less jarring if you get exactly 21.
	// Before would just cut to next player/hand's turn with no time
	// to process what just happened.
	if (p.handValue() == 21)
	{
	    gameStatus();
	    TimeUnit.SECONDS.sleep(2);
	}

	// Plays the second hand if the player decided to split
	if (split.cardsInHand() > 0)
	{

	    action = "hit";

	    while ((action.startsWith("h") || action.startsWith("sp")) && split.handValue() < 21)
	    {
		gameStatus();

		System.out.println("Hit or Stand? (Second hand)");
		action = scan.nextLine();

		System.out.println();

		if (action.startsWith("h"))
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
			scan.close();
			return;
		    }

		    System.out.println("Your second hand busted!");
		    TimeUnit.SECONDS.sleep(2);
		}
	    }
	}

	// AI's turn
	// The AI is actually SUPER complex in its decision making (note: this is sarcasm)
	// The AI will hit as long as its hand's value is 17 or lower, or if it's too low to beat the player.
	// The AI can not split.
	while ((ai.handValue() <= p.handValue() || ai.handValue() < split.handValue()) || ai.handValue() <= 17)
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
		scan.close();
		return;
	    }
	}

	gameStatus();

	// Checking to see who wins. 
	// Defaults to AI winning if the player doesn't win and it's not a draw. 
	// (In other words any wonky bullshit defaults to an AI victory)
	if (p.handValue() > ai.handValue() || split.handValue() > ai.handValue())
	{
	    pVictory();
	    scan.close();
	    return;
	}
	if (p.handValue() == ai.handValue())
	{
	    System.out.println("It's a draw!");
	    scan.close();
	    return;
	} else
	{
	    aiVictory();
	    scan.close();
	    return;
	}

    }

    // aiVictory() and pVictory() exist simply because I was too lazy to
    // type out the print statements every time something would 
    // cause one of them to win.
    private static void aiVictory()
    {
	System.out.println("Your opponent wins!");
    }

    private static void pVictory()
    {
	System.out.println("You win!");
    }

    // gameStatus() exists for readability reasons.
    // imagine if everywhere you saw gameStatus() now you instead saw what was inside
    // gameStatus(). It's awful, isn't it?
    private static void gameStatus()
    {
	if (split.cardsInHand() > 0)
	{
	    if (ai.handValue() > 21)
	    {
		System.out.println("Your first hand: " + p + ", value: " + p.handValue());
		System.out.println("Your second hand: " + split + ", value: " + split.handValue());
		System.out.println("Your opponent's hand: " + ai + ", value: " + ai.handValue());
	    } else
	    {
		System.out.println("Your first hand: " + p + ", value: " + p.handValue());
		System.out.println("Your second hand: " + split + ", value: " + split.handValue());
		System.out.println("Your opponent's hand: " + ai.showing() + ", value: " + ai.showingValue());
	    }
	} else
	{
	    if (ai.handValue() > 21)
	    {
		System.out.println("Your hand: " + p + ", value: " + p.handValue());
		System.out.println("Your opponent's hand: " + ai + ", value: " + ai.handValue());
	    } else
	    {
		System.out.println("Your hand: " + p + ", value: " + p.handValue());
		System.out.println("Your opponent's hand: " + ai.showing() + ", value: " + ai.showingValue());
	    }
	}
    }
}
