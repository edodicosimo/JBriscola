package it.edo.jbriscola.model;

/**
 * Represents a player in a Briscola game.
 *
 * A player is identified by a nickname and belongs to a {@link Team}.
 */
public abstract class Player {
    private final String NICKNAME;
    private final Team team;
    protected final Hand hand;

    /**
     * Creates a player with an empty hand.
     *
     * @param nickname the nickname of the player
     * @param team the team the player belongs to
     */
    public Player(String nickname, Team team){
        this.hand = new Hand();
        this.team = team;
        this.NICKNAME = nickname;
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card the card to receive
     */
    public void receiveCard(Card card){
        hand.addCard(card);
    }

    /**
     * Plays a card from the player's hand.
     *
     * @param card the card to play
     * @return the played card
     * @throws IllegalArgumentException if the card is not in the player's hand
     */
    public Card playCard(Card card){
        if (!hand.removeCard(card)){
            throw new IllegalArgumentException("La carta non è in mano al giocatore");
        }
        return card;
    }


    /**
     * Returns the player's nickname.
     *
     * @return the player nickname
     */
    public String getNickname() {
        return NICKNAME;
    }

    /**
     * Returns the player's hand.
     *
     * @return the player hand
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Returns the player's team.
     *
     * @return the team the player belongs to
     */
    public Team getTeam() {
        return team;
    }
}
