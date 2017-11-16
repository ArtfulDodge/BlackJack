package com.example;

import java.util.Scanner;

public class Main
{
    public static void main(String args[]) throws InterruptedException
    {
	Scanner scan = new Scanner(System.in);
	String input = "";
	@SuppressWarnings("unused")
	Game g;
	
	do
	{
	    g = new Game();
	    System.out.println("Play again? (y/n)");
	    input = scan.nextLine();
	    System.out.println();

	} while (input.substring(0, 1).equalsIgnoreCase("y"));
	scan.close();
    }
}