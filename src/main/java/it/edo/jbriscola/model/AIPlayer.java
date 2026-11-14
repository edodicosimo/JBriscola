package it.edo.jbriscola.model;

/**
 * Represents a computer-controlled player in a Briscola game.
 *
 * Cards are chosen using an {@link AIStrategy}.
 */
public class AIPlayer extends Player{

    /** The strategy assigned to the player. */
    private final AIStrategy strategy;

    /**
     * Creates a computer-controlled player with the given strategy.
     *
     * @param nick the player nickname
     * @param t the team the player belongs to
     * @param s the strategy assigned to the player
     */
    public AIPlayer(String nick, Team t, AIStrategy s) {
        super(nick, t);
        this.strategy = s;
    }
    

    /**
     * Chooses a card using the player's assigned strategy and removes it from the hand.
     *
     * @return the played card
     * @throws IllegalArgumentException if the chosen card is not in the player's hand
     */
    public Card playCard(){
        Card c = strategy.chooseCard(this.hand);
        return playCard(c);
    }
    
}
