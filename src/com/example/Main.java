package com.example;

public class Main
{
    public static void main(String args[])
    {
	Deck d = new Deck();
	Player p = new Player();
	Player ai = new Player();
	
	p.hit(d);
	p.hit(d);
	
	ai.hit(d);
	ai.hit(d);
	
	System.out.println(p);
	System.out.println(p.showing());
	System.out.println();
	System.out.println(ai);
	System.out.println(ai.showing());
    }
}
