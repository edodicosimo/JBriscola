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
    /** The cards remaining in the deck, drawn from the end. */
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

    /**
     * Adds all rank and suit combinations to the deck.
     */
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
    
    /**
     * Appends a card at the end from which cards are drawn.
     *
     * @param c the card to append
     * @return {@code true} if the card is added
     */
    public boolean add(Card c){
        return cards.add(c);
    }

    /**
     * Requests insertion of a card at the given position in the deck.
     *
     * The current implementation calls itself recursively and does not insert the card.
     *
     * @param index the insertion position, where zero is drawn last
     * @param c the card to insert
     * @return {@code true} if the card is added
     * @throws IndexOutOfBoundsException if the index is outside the insertion range
     */
    public void add(int index, Card c){
        this.cards.add(index,c);
    }
}
