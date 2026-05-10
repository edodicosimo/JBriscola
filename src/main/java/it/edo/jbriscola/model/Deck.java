package it.edo.jbriscola.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

// A deck is an ordered list of cards.
public class Deck {
    private final List<Card> cards;

    public Deck(boolean shuffled) {
        this.cards = new ArrayList<>();
        initializeDeck();
        if (shuffled) {
            shuffle();
        }
    }

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

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public boolean isEmpty(){
        if (cards.size() != 0){
            return false;
        }
        else{
            return true;
        }
    }

    public Card drawCard() throws EmptyDeckException {
        try{
            return cards.removeLast();
        } catch (NoSuchElementException e){
            throw new EmptyDeckException("Il mazzo è vuoto!",e);
        }
    }

    public int getSize(){
        return cards.size();
    }
}
