package com.example;

public class Main
{
    public static void main(String args[])
    {
	Deck d = new Deck();
	System.out.println(d);
	for (int i = 0; i < 52; i++)
	{
	    System.out.println(d.draw());
	}
	d.Shuffle();
	System.out.println();
	System.out.println(d);
	
	Player p = new Player();
	p.hit(d);
	System.out.println(p);
	System.out.println(p.getHandValue());
	p.hit(d);
	System.out.println(p);
	System.out.println(p.getHandValue());
	p.hit(d);
	System.out.println(p);
	System.out.println(p.getHandValue());
	p.hit(d);
	System.out.println(p);
	System.out.println(p.getHandValue());
	p.hit(d);
	System.out.println(p);
	System.out.println(p.getHandValue());
    }
}
