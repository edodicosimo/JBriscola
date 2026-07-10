package it.edo.jbriscola.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Table {
    private Map<Player, Card> currentCards;
    
    public Table(){
        this.currentCards = new LinkedHashMap<>();
    }

    public void addCard(Player player, Card card){
        currentCards.put(player, card);
    }

    public Card getFirstCard(){
        return currentCards.values().iterator().next();
    }

    public Player getFirstPlayer(){
        return currentCards.keySet().iterator().next();
    }

    public List<Card> getAllCards(){
        return new ArrayList<>(currentCards.values());
    }
}
