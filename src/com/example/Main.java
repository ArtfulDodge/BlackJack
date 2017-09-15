package com.example;

import java.util.*;

public class Main
{
    public static void main(String args[])
    {
	Deck d = new Deck();
	Player p = new Player();
	Player ai = new Player();
	String action = "hit";
	Scanner scan = new Scanner(System.in);
	
	p.hit(d);
	p.hit(d);
	
	ai.hit(d);
	ai.hit(d);
	
	while(action.equalsIgnoreCase("hit") && p.handValue() <= 21 && p.cardsInHand() < 5)
	{
	    System.out.println("Your hand: " + p + ", value: " + p.handValue());
	    System.out.println("Your opponent's hand: " + ai.showing() + ", value: " + ai.showingValue());
	    System.out.println("Hit or Stand?");
	    action = scan.nextLine();
	    
	    if (action.equalsIgnoreCase("hit"))
	    {
		p.hit(d);
	    }
	    
	    if (p.handValue() > 21)
	    {
		System.out.println("Your hand: " + p + ", value: " + p.handValue());
		System.out.println("You busted!");
	    }
	}
	
	scan.close();
    }
}
