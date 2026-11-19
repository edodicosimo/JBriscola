package it.edo.jbriscola.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a player's hand in the Briscola card game.
 * Holds the list of cards currently owned by the player.
 */
public class Hand {

    /** The cards held in insertion order. */
    private final List<Card> cards;

    /**
     * Creates an empty hand.
     */
    public Hand() {
        this.cards = new ArrayList<>();
    }

    /**
     * Adds a card to the hand.
     *
     * @param card the card to add
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Removes a card from the hand.
     * Since {@link #showCards()} returns an unmodifiable view,
     * this method is the only way to remove cards from the hand.
     *
     * @param card the card to remove
     * @return {@code true} if the card was present and has been removed,
     *         {@code false} otherwise
     */
    public boolean removeCard(Card card) {
        return cards.remove(card);
    }

    /**
     * Checks whether the hand contains a given card.
     *
     * @param c the card to look for
     * @return {@code true} if the card is present, {@code false} otherwise
     */
    public boolean contains(Card c) {
        return cards.contains(c);
    }

    /**
     * Returns an unmodifiable view of the cards in the hand.
     * To remove cards use {@link #removeCard(Card)}.
     *
     * @return unmodifiable list of cards
     */
    public List<Card> showCards() {
        return Collections.unmodifiableList(cards);
    }

    /**
     * Returns the number of cards in the hand.
     *
     * @return number of cards
     */
    public int getSize() {
        return cards.size();
    }

}
