package it.edo.jbriscola.model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class HandTest {
    @Test
    void newHandIsEmpty() {
        Hand hand = new Hand();

        assertEquals(0, hand.getSize());
        assertTrue(hand.showCards().isEmpty());
    }

    @Test
    void addCardIncreasesSizeAndIsContained() {
        Hand hand = new Hand();
        Card card = new Card(Rank.ASSO, Suit.DENARI);

        hand.addCard(card);

        assertEquals(1, hand.getSize());
        assertTrue(hand.contains(card));
    }

    @Test
    void removeCardReturnsTrueAndRemovesWhenPresent() {
        Hand hand = new Hand();
        Card card = new Card(Rank.RE, Suit.SPADE);
        hand.addCard(card);

        boolean removed = hand.removeCard(card);

        assertTrue(removed);
        assertFalse(hand.contains(card));
        assertEquals(0, hand.getSize());
    }

    @Test
    void removeCardReturnsFalseWhenNotPresent() {
        Hand hand = new Hand();
        Card card = new Card(Rank.SETTE, Suit.COPPE);

        boolean removed = hand.removeCard(card);

        assertFalse(removed);
    }

    @Test
    void containsReturnsFalseForCardNotInHand() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.CAVALLO, Suit.BASTONI));

        assertFalse(hand.contains(new Card(Rank.FANTE, Suit.BASTONI)));
    }

    @Test
    void showCardsReflectsInsertionOrder() {
        Hand hand = new Hand();
        Card first = new Card(Rank.ASSO, Suit.DENARI);
        Card second = new Card(Rank.TRE, Suit.SPADE);

        hand.addCard(first);
        hand.addCard(second);

        assertEquals(List.of(first, second), hand.showCards());
    }

    @Test
    void showCardsIsUnmodifiable() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.SEI, Suit.COPPE));
        List<Card> cards = hand.showCards();

        assertThrows(UnsupportedOperationException.class,
                () -> cards.add(new Card(Rank.CINQUE, Suit.DENARI)));
    }
}
