package it.edo.jbriscola.model;

/**
 * Defines a strategy for choosing a card from a player's hand.
 */
public interface AIStrategy {
    
    /**
     * Chooses a card from the given hand without removing it.
     *
     * @param h the hand to choose from
     * @return the chosen card
     */
    public abstract Card chooseCard(Hand h);
}
