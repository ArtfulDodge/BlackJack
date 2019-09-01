package dodger.blackjack;

import java.util.Scanner;

import dodger.blackjack.players.Dealer;
import dodger.blackjack.players.Player;
import dodger.blackjack.players.Split;

public class Game
{
    BlackjackDeck d = new BlackjackDeck(52*4);
    Player p;
    Split split;
    Dealer ai = new Dealer();
    String input;
    Scanner scan = new Scanner(System.in);

    // ---------------------------------------------------------------
    // Constructor
    // Starts the game and plays until the user runs out of money.
    // ---------------------------------------------------------------
    public Game() throws InterruptedException
    {
        long startingAmt = 1000;
        /*do
        {
            System.out.println("How much money do you start with?");
            startingAmt = getNumericInput();

            if (startingAmt <= 0)
            {
                System.err.println("That number is too small! Minimum is $1.");
                Thread.sleep(2500);
            }

        } while (startingAmt == 0);*/

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

        // Detecting if anyone wins by Blackjack
        if (p.handValue() == 21 && ai.handValue() != 21)
        {
            gameStatusFinal();
            System.out.print("You got a blackjack! ");
            pVictory();
            return;
        } else if (p.handValue() == 21 && ai.handValue() == 21)
        {
            gameStatusFinal();
            System.out.println("You both got blackjacks! ");
            draw();
            return;
        } else if (ai.handValue() == 21 && p.handValue() != 21)
        {
            gameStatusFinal();
            System.out.print("The Dealer got a blackjack! ");
            aiVictory();
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
                        && split.cardsInHand() == 0 && p.getBet() <= p.money)
                {
                    System.out.print("Hit, Stand, double down, or Split? (Note: Splitting will double your bet)");

                } else if (p.getBet() <= p.money)
                {
                    System.out.print("Hit, Stand, or Double down?");
                }
                else
                {
                    System.out.print("Hit or Stand?");
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

            if (p.isBusted())
            {
                if (split.cardsInHand() == 0)
                {
                    gameStatusFinal();
                    System.out.println("You busted!");
                    aiVictory();
                    return;
                } else
                {
                    gameStatus();
                    p.resetBet();
                    System.out.println("Your first hand busted!");
                    System.out.println();
                    Thread.sleep(2500);
                }
            }
            // Detects what their input starts with instead of the whole word as a
            // form of resilience against typos.
        } while ((input.substring(0, 1).equalsIgnoreCase("h") || input.substring(0, 2).equalsIgnoreCase("sp"))
                && !p.isBusted());

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

                if (split.isBusted())
                {
                    if (p.isBusted())
                    {
                        gameStatusFinal();
                        System.out.println("Both your hands busted!");
                        aiVictory();
                        return;
                    }
                    gameStatus();
                    split.resetBet();
                    System.out.println("Your second hand busted!");
                    Thread.sleep(2500);
                }
            } while ((input.substring(0, 1).equalsIgnoreCase("h") || input.substring(0, 1).equalsIgnoreCase("sp"))
                    && !split.isBusted());
        }

        // Dealer's turn
        // The Dealer is actually SUPER complex in its decision making
        // The Dealer will hit as long as its hand's value is 17 or lower
        // The Dealer can not split.
        while (ai.handValue() < 17)
        {
            gameStatusFinal();

            Thread.sleep(2500);

            System.out.println();

            ai.hit(d);
        }

        gameStatusFinal();

        // Checking to see who wins.
        // Defaults to Dealer winning if the player doesn't win and it's not a draw.
        // (In other words any wonky bullshit defaults to an Dealer victory)
        if (ai.isBusted())
        {
            System.out.println("The Dealer busted!");
            pVictory();
            return;
        }

        if ((p.handValue() > ai.handValue() && !p.isBusted())
                || (split.handValue() > ai.handValue() && !split.isBusted()))
        {
            if (p.handValue() < ai.handValue())
                p.resetBet();
            if (split.handValue() < ai.handValue())
                split.resetBet();

            pVictory();
            return;
        }

        if (p.handValue() == ai.handValue() || split.handValue() == ai.handValue())
        {
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
        if (d.usedCards() >= d.getDeckSize()/2)
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
        System.out.println("The Dealer wins the hand.");
        ai.money += 2*pot();
        return;
    }

    private void pVictory()
    {
        System.out.println("You win the hand!");
        p.money += 2*pot();
        return;
    }

    private void draw()
    {
        System.out.println("It's a push!");
        p.money += p.getBet() + split.getBet();
        return;
    }

    // ---------------------------------------------------------------
    // Outputs the status of the pot.
    // ---------------------------------------------------------------
    private void potStatus()
    {
        System.out.println("Your bet is now $" + pot());

        ai.increaseBet(p.getBet() + split.getBet() - ai.getBet());
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
            System.out.println("Showing: " + ai.valueToString());
        }
    }

    // ---------------------------------------------------------------
    // Returns the final values of each Player's hand
    // ---------------------------------------------------------------
    private void gameStatusFinal()
    {
        if (split.cardsInHand() > 0)
        {
            System.out.println("Your first hand: " + p + ". Value: " + p.handValue());
            System.out.println("Your second hand: " + split + ". Value: " + split.handValue());
        } else
        {
            System.out.println("Your hand: " + p + ". Value: " + p.handValue());
        }
        System.out.println("The Dealer's hand: " + ai.toString(true) + ". Value: " + ai.handValue());
    }

    // ---------------------------------------------------------------
    // Reads the user's input and does the desired action
    // ---------------------------------------------------------------
    private void doAction(String i, Player pl) throws InterruptedException
    {
        if (i.substring(0, 1).equalsIgnoreCase("d") && p.getBet() <= p.money && pl.cardsInHand() == 2)
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
        return p.getBet() + split.getBet();
    }
}
