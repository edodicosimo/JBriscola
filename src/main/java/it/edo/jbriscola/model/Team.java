package it.edo.jbriscola.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a team, its registered players and the cards won in tricks.
 */
public class Team {
    /** The identifier used for team equality. */
    private final String ID;
    /** The cards collected by the team. */
    private List<Card> wonCards;
    /** The players registered with this team instance. */
    private List<Player> playersList;

    /**
     * Creates a team with no registered players or collected cards.
     *
     * @param id the team identifier
     */
    public Team(String id){
        this.ID = id;
        this.wonCards = new ArrayList<>();
        this.playersList = new ArrayList<>();
    }

    /**
     * Returns the team identifier.
     *
     * @return the team identifier
     */
    public String getId() {
        return ID;
    }

    /**
     * Returns an unmodifiable view of the collected cards.
     *
     * @return the cards won by the team
     */
    public List<Card> getWonCards() {
        return Collections.unmodifiableList(wonCards);
    }

    /**
     * Adds cards to the team's collected pile.
     *
     * @param cards the cards won in a trick
     */
    public void addCardsToPile(List<Card> cards){
        wonCards.addAll(cards);
    }

    /**
     * Registers a player assigned to this exact team instance.
     *
     * @param p the player to register
     * @throws IllegalArgumentException if the player belongs to a different team instance
     */
    public void addPlayer(Player p){
        if (p.getTeam() == this){
            playersList.add(p);
        } else {
            throw new IllegalArgumentException("Il team del player non corrisponde a questo team");
        }
    }

    /**
     * Returns the number of registered players.
     *
     * @return the registered player count
     */
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

    /**
     * Returns a text representation of the team.
     *
     * @return the team label followed by its identifier
     */
    @Override 
    public String toString(){
        return "Team: " + ID;
    }
}
