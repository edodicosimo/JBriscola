package it.edo.jbriscola.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Manages the players, cards, scores and phase transitions of a Briscola game.
 *
 * Trick resolution and scoring are delegated to {@link GameRules}.
 */
public class Game {
    /** The number of players created for the game. */
    private final int numberOfPlayers;
    /** The players in circular turn order, starting with the trick leader. */
    private List<Player> playerList;
    /** The first shared team. */
    private final Team team1;
    /** The second shared team. */
    private final Team team2;
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
    private final GameRules rules;
    /** The observers reserved for game event notifications. */
    private final List<GameObserver> observers;

    /**
     * Creates a game ready for initialization from the supplied players, teams and rules.
     *
     * Player and observer lists are retained by reference. The deck and table
     * are created when the initialization phase is processed.
     *
     * @param n the total number of players
     * @param pList the players in turn order
     * @param team1 the first team shared by its players
     * @param team2 the second team shared by its players
     * @param rules the rules used for trick resolution and scoring
     * @param obs the registered game observers
     */
    public Game(Integer n, List<Player> pList, Team team1, Team team2, GameRules rules, List<GameObserver> obs){
        this.currentStatus = GameStatus.NOTREADY;
        this.numberOfPlayers = n;
        this.playerList = pList;
        this.team1 = team1;
        this.team2 = team2;
        this.rules = rules;
        this.observers = obs;
        this.currentPhase = Phase.INITIALIZATION;
        this.currentStatus = GameStatus.READY;
    }

    /**
     * Registers an observer of game updates.
     *
     * @param o the observer to register
     * @return {@code true} if the observer is added
     */
    public boolean addObserver(GameObserver o){
        return observers.add(o);
    }

    /**
     * Notifies registered observers of the current game state.
     */
    public void notifyObservers(){
        for (GameObserver o : observers) {
            o.update(this);
        }
    }

    /**
     * Returns an independent snapshot of the public game data.
     *
     * The snapshot is available before initialization and preserves table play order.
     * Player hands are represented only by their card counts.
     *
     * @return the current game snapshot
     */
    public GameState getState() {
        List<PlayerState> players = new ArrayList<>();
        Map<Player, PlayerState> playerStates = new LinkedHashMap<>();
        for (Player p : playerList) {
            PlayerState state = new PlayerState(
                p.getNickname(), p.getTeam().getId(), p.getHand().getSize()
            );
            players.add(state);
            playerStates.put(p, state);
        }

        Map<Card, PlayerState> tableCards = new LinkedHashMap<>();
        if (table != null) {
            for (Card card : table.getAllCards()) {
                tableCards.put(card, playerStates.get(table.getPlayerByCard(card)));
            }
        }

        List<Integer> scores = new ArrayList<>();
        for (Team t : List.of(team1, team2)) {
            scores.add(computeScore(t));
        }

        return new GameState(
            currentPhase, currentStatus,
            currentPlayer == null ? null : currentPlayer.getNickname(),
            briscolaCard, trumpSuit, deck == null ? 0 : deck.getSize(),
            tableCards, players, scores
        );
    }

    /**
     * Creates a shuffled deck and an empty table, arranges players if needed,
     * deals initial hands and reveals the trump card.
     *
     * Existing player hands and team piles are not cleared.
     *
     * @throws IllegalStateException if the deck runs out during setup
     */
    public void initializeGame(){
        this.deck = new Deck(true);
        this.table = new Table();
        this.deck.shuffle();
        if (!playersAreMixed()){
            mixPlayers();
        }
        dealInitialCards();
        revealBriscola();
    }

    /**
     * Checks whether consecutive players in the list belong to different teams.
     *
     * The last player is not compared with the first.
     *
     * @return {@code true} if no adjacent pair belongs to the same team
     */
    public boolean playersAreMixed() {
        for (int i = 0; i < playerList.size() - 1; i++) {
            if (playerList.get(i).getTeam().equals(playerList.get(i + 1).getTeam())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Arranges four players by alternating the two teams, starting with the first team.
     *
     * Each team must supply at least two players. The relative order of the first
     * two players within each team is preserved.
     *
     * @throws NullPointerException if either team has no players in the current list
     * @throws IndexOutOfBoundsException if either team has fewer than two players
     */
    public void mixPlayers() {
        var byTeam = playerList.stream()
            .collect(Collectors.groupingBy(Player::getTeam));

        var t1 = byTeam.get(team1);
        var t2 = byTeam.get(team2);

        this.playerList = new ArrayList<>(List.of(
            t1.get(0),
            t2.get(0),
            t1.get(1),
            t2.get(1)
        ));
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
     * Draws and stores the trump card, sets the trump suit and reinserts the card
     * at the bottom of the deck so it is drawn last.
     *
     * @throws IllegalStateException if the deck is empty
     */
    public void revealBriscola(){
        try {
            this.briscolaCard = deck.drawCard();
            this.trumpSuit = briscolaCard.suit();
            this.deck.add(0, briscolaCard);
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
     * Asks a player to choose and remove a card using its assigned strategy.
     *
     * @param p the player whose card is requested
     * @return the chosen and removed card
     * @throws IllegalArgumentException if the selected card is not in the player's hand
     */
    public Card askPlayerForCard(Player p) {
            return p.playCard();
    }

    /**
     * Computes the score of a team's collected cards using the game rules.
     *
     * @param t the team to score
     * @return the team's total score
     */
    public int computeScore(Team t){
        return rules.computeScore(t.getPile());
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
        for (Team t : List.of(team1, team2)) {
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
     * Turns request a card through the current player's strategy and advance
     * the turn order. Human strategies may block while waiting for input.
     * Completed tricks are collected before dealing again.
     * An empty deck ends the game when all hands are empty; otherwise play continues.
     * The endgame phase marks the game as not ready, while the ended phase does nothing.
     * Observers are notified after the phase has been processed successfully.
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
                    Card selectedCard = askPlayerForCard(currentPlayer);
                    playCard(currentPlayer, selectedCard);
                    try {
                        updateCurrentPlayer();
                    } catch (TurnEndedException e) {
                        currentPhase = Phase.COLLECTINGTRICK;
                    }
                    currentStatus = GameStatus.READY;
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
                        break;
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
        notifyObservers();
    }

}
