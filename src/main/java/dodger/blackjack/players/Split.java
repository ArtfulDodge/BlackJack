package dodger.blackjack.players;

public class Split extends Player
{
    Player play;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------
    public Split(Player p)
    {
        super(0);
        play = p;
    }

    // ---------------------------------------------------------------
    // Increases the Split's bet by the given amount
    // ---------------------------------------------------------------
    public void increaseBet(long amount)
    {
        if (money < amount)
        {
            System.out.println("That bet is too large! Betting max amount instead.");
            bet += play.money;
            play.money = 0;
        } else
        {
            bet += amount;
            play.money -= amount;
        }
    }

    // ---------------------------------------------------------------
    // Returns true if the Split is busted
    // ---------------------------------------------------------------
    public boolean isBusted()
    {
        return handValue() > 21;
    }
}
