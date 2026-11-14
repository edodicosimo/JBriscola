package it.edo.jbriscola.model;

import java.util.List;

/**
 * Implements trick resolution and scoring according to Briscola rules.
 */
public class BriscolaRules implements GameRules {

    /**
     * Determines the winning card using the trump suit and rank strength.
     *
     * If no trump card is played, the strongest card of the first played suit wins.
     *
     * @param cards the nonempty list of cards in play order
     * @param trumpSuit the trump suit of the game
     * @return the winning card
     * @throws IndexOutOfBoundsException if the list is empty
     */
    @Override
    public Card solveTable(List<Card> cards, Suit trumpSuit){
        Card winner = cards.get(0);
        for (Card c : cards) {
            if (c.suit() == winner.suit()) {
                if (c.rank().getStrength() > winner.rank().getStrength()) {
                    winner = c;
                }
            } else if (c.suit() == trumpSuit) {
                winner = c;
            }
        }
        return winner;
    }
    
    /**
     * Sums the Briscola point values of the given cards.
     *
     * @param cards the cards to score
     * @return the total card score, or {@code 0} if the list is empty
     */
    @Override
    public int computeScore(List<Card> cards){
        int score = 0;
        for (Card card : cards) {
            score += card.points();
        }
        return score;
    }
    
}
