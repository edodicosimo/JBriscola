package it.edo.jbriscola.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the cards played in the current trick.
 *
 * Each card is associated with its {@link Player}, preserving play order.
 */
public class Table {
    /** The played cards associated with their players in play order. */
    private Map<Player, Card> currentCards;
    
    /**
     * Creates an empty table.
     */
    public Table(){
        this.currentCards = new LinkedHashMap<>();
    }

    /**
     * Associates a card with a player, replacing any card already played by that player.
     *
     * @param player the player who played the card
     * @param card the played card
     */
    public void addCard(Player player, Card card){
        currentCards.put(player, card);
    }

    /**
     * Returns the first card played in the current trick.
     *
     * @return the first played card
     * @throws java.util.NoSuchElementException if the table is empty
     */
    public Card getFirstCard(){
        return currentCards.values().iterator().next();
    }

    /**
     * Returns the player who played first in the current trick.
     *
     * @return the first player
     * @throws java.util.NoSuchElementException if the table is empty
     */
    public Player getFirstPlayer(){
        return currentCards.keySet().iterator().next();
    }

    /**
     * Returns the number of cards on the table.
     *
     * @return the played card count
     */
    public int howManyCards(){
        return currentCards.size();
    }

    /**
     * Returns a copy of the cards on the table in play order.
     *
     * @return the played cards in play order
     */
    public List<Card> getAllCards(){
        return new ArrayList<>(currentCards.values());
    }

    /**
     * Removes all cards from the table.
     */
    public void clear(){
        currentCards.clear();
    }

    /**
     * Returns the player associated with the given card.
     *
     * @param card the card to look up
     * @return the player who played the card
     * @throws IllegalArgumentException if the card is not on the table
     */
    public Player getPlayerByCard(Card card) {
        for (var entry : currentCards.entrySet()) {
            if (entry.getValue().equals(card)) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("La carta non è sul tavolo");
    }

    
}
