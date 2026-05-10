package it.edo.jbriscola.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class DeckTest {
    @Test
    void drawShuffledDeckUntilEmptyDeckException() {
        Deck deck = new Deck(true);
        Set<Card> drawnCards = new HashSet<>();

        try {
            while (true) {
                Card card = deck.drawCard();

                assertNotNull(card);
                assertTrue(drawnCards.add(card));
            }
        } catch (EmptyDeckException e) {
            assertEquals(Rank.values().length * Suit.values().length, drawnCards.size());
            assertTrue(deck.isEmpty());
        }
    }
}
