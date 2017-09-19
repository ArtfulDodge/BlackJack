package com.example;

import java.util.*;
import java.util.concurrent.TimeUnit;

//TODO: Splitting clarifications
public class Main
{
    public static void main(String args[]) throws InterruptedException
    {
	// Creating needed objects and variables
	Deck d = new Deck();
	Player p = new Player();
	Player split = new Player();
	Player ai = new Player();
	String action = "hit";
	Scanner scan = new Scanner(System.in);

	// Populating the player's hands
	p.hit(d);
	ai.hit(d);

	p.hit(d);
	ai.hit(d);

	// Loop that allows the player to actually play the game
	// Detects if the action they want starts with an h because of typos
	while ((action.startsWith("h") || action.startsWith("sp")) && p.handValue() < 21)
	{
	    System.out.println("Your hand: " + p + ", value: " + p.handValue());
	    System.out.println("Your opponent's hand: " + ai.showing() + ", value: " + ai.showingValue());

	    if (p.cardsInHand() == 2 && p.cardInPos(0).getFaceValue().equals(p.cardInPos(1).getFaceValue()))
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
		System.out.println("Hit or Stand?");
		action = scan.nextLine();
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
		    System.out.println("Your hand: " + p + ", value: " + p.handValue());
		    System.out.println("You busted!");
		    aiVictory();
		    scan.close();
		    return;
		} else
		{
		    System.out.println("Your hand: " + p + ", value: " + p.handValue());
		    System.out.println("Your first hand busted!");
		}
	    }
	}

	if (split.cardsInHand() > 0)
	{
	    System.out.println("Your first hand: " + p + ", value: " + p.handValue());
	    System.out.println();
	    
	    action = "hit";
	    
	    while ((action.startsWith("h") || action.startsWith("sp")) && split.handValue() < 21)
	    {
		System.out.println("Your second hand: " + split + ", value: " + split.handValue());
		System.out.println("Your opponent's hand: " + ai.showing() + ", value: " + ai.showingValue());

		System.out.println("Hit or Stand?");
		action = scan.nextLine();

		System.out.println();

		if (action.startsWith("h"))
		{
		    split.hit(d);
		}

		if (split.handValue() > 21 && p.handValue() > 21)
		{
		    System.out.println("Your first hand: " + p + ", value: " + p.handValue());
		    System.out.println("Your second hand: " + split + ", value: " + split.handValue());
		    System.out.println("Both your hands busted!");
		    aiVictory();
		    scan.close();
		    return;
		}
	    }
	}

	while ((ai.handValue() <= p.handValue() || ai.handValue() < split.handValue()) || ai.handValue() < 18)
	{
	    if (split.cardsInHand() > 0)
	    {
		System.out.println("Your first hand: " + p + ", value: " + p.handValue());
		System.out.println("Your second hand: " + split + ", value: " + split.handValue());
		System.out.println("Your opponent's hand: " + ai.showing() + ", value: " + ai.showingValue());
	    } else
	    {
		System.out.println("Your hand: " + p + ", value: " + p.handValue());
		System.out.println("Your opponent's hand: " + ai.showing() + ", value: " + ai.showingValue());
	    }
	    TimeUnit.SECONDS.sleep(2);

	    System.out.println();

	    ai.hit(d);

	    if (ai.handValue() > 21)
	    {
		System.out.println("Your opponent's hand: " + ai + ", value: " + ai.handValue());
		System.out.println("Your opponent busted!");
		pVictory();
		scan.close();
		return;
	    }
	}
	if (split.cardsInHand() > 0)
	{
	    System.out.println("Your first hand: " + p + ", value: " + p.handValue());
	    System.out.println("Your second hand: " + split + ", value: " + split.handValue());
	} else
	{
	    System.out.println("Your hand: " + p + ", value: " + p.handValue());
	}

	System.out.println("Your opponent's hand: " + ai + ", value: " + ai.handValue());
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

    private static void aiVictory()
    {
	System.out.println("Your opponent wins!");
    }

    private static void pVictory()
    {
	System.out.println("You win!");
    }
}
