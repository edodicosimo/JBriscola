package it.edo.jbriscola.model;

import java.util.List;

/**
 * Defines the rules for determining trick winners and card scores.
 */
public interface GameRules {
    /**
     * Determines the winning card of a trick.
     *
     * @param cards the nonempty list of cards in play order
     * @param trumpSuit the trump suit of the game
     * @return the winning card
     */
    public abstract Card solveTable(List<Card> cards, Suit trumpSuit);
    
    /**
     * Computes the total score of the given cards.
     *
     * @param cards the cards to score
     * @return the total card score, or {@code 0} if the list is empty
     */
    public abstract int computeScore(List<Card> cards);

}
