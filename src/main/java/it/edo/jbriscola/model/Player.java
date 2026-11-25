package it.edo.jbriscola.model;

/**
 * Represents a player in a Briscola game.
 *
 * A player is identified by a nickname and belongs to a {@link Team}.
 */
public class Player {
    private final PlayerStrategy strategy;
    /** The player nickname. */
    private final String NICKNAME;
    /** The team currently assigned to the player. */
    private Team team;
    /** The cards currently held by the player. */
    protected final Hand hand;

    /**
     * Creates a player with an empty hand.
     *
     * @param nickname the nickname of the player
     * @param team the team the player belongs to
     */
    public Player(String nickname, Team team, PlayerStrategy s){
        this.hand = new Hand();
        this.team = team;
        this.NICKNAME = nickname;
        this.strategy = s;
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
     * Chooses a card using the player's assigned strategy and removes it from the hand.
     *
     * @return the played card
     * @throws IllegalArgumentException if the chosen card is not in the player's hand
     */
    public Card playCard(){
        Card c = strategy.chooseCard(this.hand);
        return playCard(c);
    }
    
    /**
     * Returns the player's nickname.
     *
     * @return the player nickname
     */
    public String getNickname() {
        return this.NICKNAME;
    }

    /**
     * Returns the player's hand.
     *
     * @return the player hand
     */
    public Hand getHand() {
        return this.hand;
    }

    /**
     * Returns the player's team.
     *
     * @return the team the player belongs to
     */
    public Team getTeam() {
        return this.team;
    }

    /**
     * Sets the team the player belongs to.
     *
     * @param team the player's new team
     */
    public void setTeam(Team team) {
        this.team = team;
    }
}
