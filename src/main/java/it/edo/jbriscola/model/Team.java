package it.edo.jbriscola.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Team {
    private final String ID;
    private List<Card> wonCards;

    public Team(String id){
        this.ID = id;
        this.wonCards = new ArrayList<>();
    }

    public String getId() {
        return ID;
    }

    public List<Card> getWonCards() {
        return Collections.unmodifiableList(wonCards);
    }

    public void addCardsToPile(List<Card> cards){
        wonCards.addAll(cards);
    }

    @Override 
    public String toString(){
        return "Team: " + ID;
    }
}
