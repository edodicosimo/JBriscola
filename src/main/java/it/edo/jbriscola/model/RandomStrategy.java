package it.edo.jbriscola.model;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Implements a strategy that chooses a random card from a player's hand.
 */
public class RandomStrategy implements AIStrategy {
    /**
     * Chooses a random card from the given hand without removing it.
     *
     * @param h the nonempty hand to choose from
     * @return the randomly chosen card
     * @throws IllegalArgumentException if the hand is empty
     */
    @Override 
    public Card chooseCard(Hand h){
        int index = ThreadLocalRandom.current().nextInt(h.getSize());
        return h.showCards().get(index);
    }
}
