package it.edo.jbriscola.model;

import java.util.HashMap;
import java.util.Map;

public class Table {
    private Map<Player, Card> currentCards;
    
    public Table(){
        this.currentCards = new HashMap<>();
    }

    public void addCard(Player player, Card card){
        currentCards.put(player, card);
    }
}
