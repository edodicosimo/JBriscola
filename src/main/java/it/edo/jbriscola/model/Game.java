package it.edo.jbriscola.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Manages the players, cards, scores and phase transitions of a Briscola game.
 *
 * Trick resolution and scoring are delegated to {@link GameRules}.
 */
public class Game {
    /** The number of players created for the game. */
    private int numberOfPlayers;
    /** The players in circular turn order, starting with the trick leader. */
    private List<Player> playerList;
    /** The two teams to which players are assigned alternately. */
    private List<Team> teams;
    /** The deck from which players draw cards. */
    private Deck deck;
    /** The cards played in the current trick and their players. */
    private Table table;
    /** The revealed card used to determine the trump suit. */
    private Card briscolaCard;
    /** The suit that takes precedence when resolving a trick. */
    private Suit trumpSuit;
    /** The player selected to take the next turn. */
    private Player currentPlayer;
    /** The phase processed by the next game update. */
    private Phase currentPhase;
    /** Whether the game can advance without waiting for input. */
    private GameStatus currentStatus;
    /** The rules used to resolve tricks and compute scores. */
    private GameRules rules;
    /** The observers reserved for game event notifications. */
    private List<GameObserver> observers;

    /**
     * Creates human players assigned alternately to two teams and prepares initialization.
     *
     * @param rules the rules used for trick resolution and scoring
     * @param playerNames the player nicknames in seating order
     */
    public Game(GameRules rules,String...playerNames){
        this.currentStatus = GameStatus.NOTREADY;
        this.rules = rules;
        this.numberOfPlayers = playerNames.length;
        this.teams = List.of(
            new Team("Team_1"),
            new Team("Team_2")
        );
        this.playerList = new ArrayList<>(numberOfPlayers);
        int i = 0;
        for (String name : playerNames) {
            this.playerList.add(
                new HumanPlayer(name, this.teams.get(i % 2))
            );
            i++;
        }
        this.currentPhase = Phase.INITIALIZATION;
        this.currentStatus = GameStatus.READY;
    }


    /**
     * Creates a shuffled deck and an empty table, deals initial hands and reveals the trump card.
     *
     * Existing player hands and team piles are not cleared.
     *
     * @throws IllegalStateException if the deck runs out during setup
     */
    public void initializeGame(){
        this.deck = new Deck(true);
        this.table = new Table();
        dealInitialCards();
        revealBriscola();
    }

    /**
     * Sets the dealing phase and draws one card for each player in turn order.
     *
     * Cards already dealt remain in the players' hands if the deck runs out.
     *
     * @throws EmptyDeckException if the deck runs out before all players receive a card
     */
    public void dealCards() throws EmptyDeckException{
        this.currentPhase = Phase.DEALING;
        for (Player p : playerList) {
                p.receiveCard(deck.drawCard());    
        } 
    }

    /**
     * Deals three cards to each player in three rounds.
     *
     * @throws IllegalStateException if the deck runs out during the initial deal
     */
    public void dealInitialCards(){
        try {
            for (int i = 0; i < 3; i++) {
                    this.dealCards();
                }
        } catch (EmptyDeckException e) {
            throw new IllegalStateException(
                "Carte insufficienti per la distribuzione iniziale", e
            );
        }
    }

    /**
     * Draws and stores the trump card, sets the trump suit and returns the card to the deck.
     *
     * @throws IllegalStateException if the deck is empty
     */
    public void revealBriscola(){
        try {
            this.briscolaCard = deck.drawCard();
            this.trumpSuit = briscolaCard.suit();
            this.deck.add(briscolaCard);
        } catch (EmptyDeckException e) {
            throw new IllegalStateException(
                "Carte insufficienti per pescare la briscola",e
            );
        }
    }

    /**
     * Places a player's card on the table without removing it from the hand.
     *
     * @param p the player associated with the card
     * @param c the card to place on the table
     */
    public void playCard(Player p, Card c){
        this.table.addCard(p, c);
    }

    /**
     * Sets the phase to process on the next update.
     *
     * @param p the new game phase
     */
    public void changePhase(Phase p){
        this.currentPhase = p;
    }

    /**
     * Awards the played cards to the winning team, makes the winner lead and clears the table.
     *
     * The table must contain a completed trick.
     */
    public void solveTable(){
        Card winnerCard = rules.solveTable(table.getAllCards(), trumpSuit);
        Player winnerPlayer = table.getPlayerByCard(winnerCard);
        Team winnerTeam = winnerPlayer.getTeam();
        winnerTeam.addCardsToPile(table.getAllCards());
        updatePlayerOrder(winnerPlayer);
        table.clear();
    }
    
    /**
     * Rotates the turn order so the given player leads and becomes the current player.
     *
     * If the player is not in the game, the order remains unchanged.
     *
     * @param p the player who leads the next trick
     */
    public void updatePlayerOrder(Player p){
        int winnerIndex = playerList.indexOf(p);

        if (winnerIndex == -1) {
            return;
        }

        Collections.rotate(playerList, -winnerIndex);
        this.currentPlayer = p;
    }
    /**
     * Selects the next player in the current turn order.
     *
     * @throws TurnEndedException if there is no player after the current player
     */
    public void updateCurrentPlayer() throws TurnEndedException{
        int newidx = playerList.indexOf(currentPlayer) + 1; 
        try {
            currentPlayer = playerList.get(newidx);
        } catch (IndexOutOfBoundsException e) {
            throw new TurnEndedException("Turno finito",e);
        }
    }

    /**
     * Returns whether the game is ready to advance.
     *
     * @return {@code true} if the current status is {@link GameStatus#READY}
     */
    public boolean isReady(){
        return currentStatus == GameStatus.READY;
    }

    /**
     * Returns the current game phase.
     *
     * @return the current phase
     */
    public Phase getPhase(){
        return currentPhase;
    }
    
    /**
     * Asks an AI player to choose and remove a card from its hand.
     *
     * @param p the AI player whose card is requested
     * @return the chosen and removed card
     * @throws IllegalArgumentException if the player is not an AI player or its strategy cannot supply a valid card
     */
    public Card askPlayerForCard(Player p) {
        if (p instanceof AIPlayer aiPlayer) {
            return aiPlayer.playCard();
        }
        throw new IllegalArgumentException("Il giocatore deve essere un AIPlayer");
    }

    /**
     * Computes the score of a team's collected cards using the game rules.
     *
     * @param t the team to score
     * @return the team's total score
     */
    public int computeScore(Team t){
        return rules.computeScore(t.getWonCards());
    }

    /**
     * Returns the first team with the highest strictly positive score.
     *
     * Ties are resolved in favor of the first team in team order.
     *
     * @return the highest-scoring team, or {@code null} if no team has a positive score
     */
    public Team computeWinningTeam(){
        int best = 0;
        Team winner = null;
        for (Team t : teams) {
            int score = computeScore(t);
            if (score > best){
                winner = t;
                best = score;
            }
        }
        return winner;
    }

    /**
     * Checks whether all players have empty hands.
     *
     * The deck and table are not checked.
     *
     * @return {@code true} if every player's hand is empty
     */
    public boolean isGameOver() {
        boolean ended = true;
        for (Player p : playerList) {
            ended = ended && p.getHand().getSize() == 0;
        }
        return ended;
    }

    /**
     * Processes one step of the current phase and updates the game status.
     *
     * Human turns wait for external input. AI turns play a card and advance the
     * turn order. Completed tricks are collected before dealing again.
     * An empty deck ends the game when all hands are empty; otherwise play continues.
     * The endgame phase marks the game as not ready, while the ended phase does nothing.
     */
    public void updateGame() {
        switch (currentPhase) {
            case INITIALIZATION -> {
                initializeGame();
                currentPlayer = playerList.get(0);
                currentPhase = Phase.TURN;
                currentStatus = GameStatus.READY;
            }

            case TURN -> {
                currentStatus = GameStatus.NOTREADY;
                if (currentPlayer instanceof HumanPlayer) {
                    currentPhase = Phase.WAIT_HUMAN_PLAY;
                } else {
                    Card selectedCard = askPlayerForCard(currentPlayer);
                    playCard(currentPlayer, selectedCard);
                    try {
                        updateCurrentPlayer();
                    } catch (TurnEndedException e) {
                        currentPhase = Phase.COLLECTINGTRICK;
                    }
                    currentStatus = GameStatus.READY;
                }
            }

            case WAIT_HUMAN_PLAY -> {
                // The controller submits the chosen card separately.
                currentStatus = GameStatus.NOTREADY;
            }

            case COLLECTINGTRICK -> {
                currentStatus = GameStatus.NOTREADY;
                this.solveTable();
                currentPhase = Phase.DEALING;
                currentStatus = GameStatus.READY;
            }

            case DEALING -> {
                currentStatus = GameStatus.NOTREADY;
                try {
                    dealCards();
                } catch (EmptyDeckException e) {
                    if (isGameOver()){
                        currentPhase = Phase.ENDGAME;
                        currentStatus = GameStatus.READY;
                        return;
                    }
                }
                currentPhase = Phase.TURN;
                currentStatus = GameStatus.READY;
            }

            case ENDGAME -> {

                currentStatus = GameStatus.NOTREADY;
            }

            case ENDED ->{
            }
        }
    }






}
