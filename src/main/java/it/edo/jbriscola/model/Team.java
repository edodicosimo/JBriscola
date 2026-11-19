package it.edo.jbriscola.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Team {
    private final String ID;
    private List<Card> wonCards;
    private List<Player> playersList;

    public Team(String id){
        this.ID = id;
        this.wonCards = new ArrayList<>();
        this.playersList = new ArrayList<>();
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

    public void addPlayer(Player p){
        if (p.getTeam() == this){
            playersList.add(p);
        } else {
            throw new IllegalArgumentException("Il team del player non corrisponde a questo team");
        }
    }

    public int size(){
        return this.playersList.size();
    }

    /**
     * Compares teams by their identifiers.
     *
     * @param obj the object to compare with this team
     * @return {@code true} if the object is a team of the same class with the same identifier
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Team other = (Team) obj;
        return Objects.equals(ID, other.ID);
    }

    /**
     * Returns a hash code based on the team identifier.
     *
     * @return the hash code of the team identifier
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(ID);
    }

    @Override 
    public String toString(){
        return "Team: " + ID;
    }
}
