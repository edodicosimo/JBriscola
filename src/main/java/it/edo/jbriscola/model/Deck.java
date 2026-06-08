package it.edo.jbriscola.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Represents an ordered collection of cards in a Briscola deck.
 *
 * A full deck contains all combinations of {@link Rank} and {@link Suit}.
 */
public class Deck {
    private final List<Card> cards;

    /**
     * Creates a full Briscola deck, optionally shuffled.
     *
     * @param shuffled whether to shuffle the deck after initialization
     */
    public Deck(boolean shuffled) {
        this.cards = new ArrayList<>();
        initializeDeck();
        if (shuffled) {
            shuffle();
        }
    }

    /**
     * Creates a full, shuffled Briscola deck.
     */
    public Deck() {
        this(true);
    }

    private void initializeDeck() {
        for (var suit : Suit.values()) {
            for (var rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    /**
     * Shuffles the deck in place.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Returns whether the deck has no cards remaining.
     *
     * @return {@code true} if the deck is empty
     */
    public boolean isEmpty(){
        return cards.isEmpty();
    }

    /**
     * Removes and returns the top card from the deck.
     *
     * @return the drawn {@link Card}
     * @throws EmptyDeckException if the deck is empty
     */
    public Card drawCard() throws EmptyDeckException {
        try{
            return cards.removeLast();
        } catch (NoSuchElementException e){
            throw new EmptyDeckException("Il mazzo è vuoto!",e);
        }
    }

    /**
     * Returns the number of cards remaining in the deck.
     *
     * @return the remaining card count
     */
    public int getSize(){
        return cards.size();
    }
}
