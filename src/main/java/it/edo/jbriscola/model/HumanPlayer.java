package it.edo.jbriscola.model;

/**
 * Represents a player whose card choices are supplied externally.
 */
public class HumanPlayer extends Player{
    /**
     * Creates a human player with an empty hand.
     *
     * @param nick the player nickname
     * @param t the team the player belongs to
     */
    public HumanPlayer(String nick, Team t){
        super(nick, t);
    }
}
