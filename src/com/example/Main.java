package com.example;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main
{
    public static void main(String args[]) throws InterruptedException
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
	
	while(action.startsWith("h") && p.showingValue() < 21 && p.cardsInHand() < 5)
	{
	    System.out.println("Your hand: " + p + ", value: " + p.handValue());
	    System.out.println("Your opponent's hand: " + ai.showing() + ", value: " + ai.showingValue());
	    System.out.println("Hit or Stand?");
	    action = scan.nextLine();
	    
	    if (action.startsWith("h"))
	    {
		p.hit(d);
	    }
	    
	    if (p.handValue() > 21)
	    {
		System.out.println("Your hand: " + p + ", value: " + p.handValue());
		System.out.println("You busted!");
		aiVictory();
		scan.close();
		return;
	    }
	}
	
	while((ai.handValue() < p.showingValue() || ai.handValue() < 18) && ai.cardsInHand() < 5)
	{
	    System.out.println("Your hand: " + p + ", value: " + p.handValue());
	    System.out.println("Your opponent's hand: " + ai.showing() + ", value: " + ai.showingValue());
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
	
	System.out.println("Your hand: " + p + ", value: " + p.handValue());
	System.out.println("Your opponent's hand: " + ai + ", value: " + ai.handValue());
	if (p.handValue() > ai.handValue())
	{
	    pVictory();
	    scan.close();
	    return;
	}
	if (p.handValue() < ai.handValue())
	{
	    aiVictory();
	    scan.close();
	    return;
	}
	if (p.handValue() == ai.handValue())
	{
	    System.out.println("It's a draw!");
	    scan.close();
	    return;
	}
	    
	scan.close();
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
