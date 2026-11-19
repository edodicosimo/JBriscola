package it.edo.jbriscola.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds a game with two shared teams, human players and optional AI players.
 */
public class GameBuilder {
    /** The number of players created for the game. */
    private int numberOfPlayers;
    /** The target number of players per team. */
    private int playersPerTeam;
    /** The players in insertion order. */
    private List<Player> playerList = new ArrayList<>();
    /** The first shared team. */
    private Team team1 = new Team("Team_1");
    /** The second shared team. */
    private Team team2 = new Team("Team_2");
    /** The rules used to resolve tricks and compute scores. */
    private GameRules rules;
    /** The observers reserved for game event notifications. */
    private List<GameObserver> observers = new ArrayList<>();

    //////////////////////////////////////////////
    
    
    /**
     * Sets the rules used for trick resolution and scoring.
     *
     * @param rules the game rules
     * @return this builder
     */
    public GameBuilder withRules(GameRules rules){
        this.rules = rules;
        return this;
    }
    
    /**
     * Copies the observers to register with the game.
     *
     * @param observers the observers to register
     * @return this builder
     */
    public GameBuilder withObservers(List<GameObserver> observers){
        this.observers = new ArrayList<>();
        this.observers.addAll(observers);
        return this;
    }

    /**
     * Sets the total player count and the target count per team.
     *
     * The target per team is half the total, using integer division.
     *
     * @param n the requested number of players
     * @return this builder
     */
    public GameBuilder withNumberOfPlayers(int n){
        this.numberOfPlayers = n;
        this.playersPerTeam = n/2;
        return this;
    }

    /**
     * Creates and registers a human player with one of the shared teams.
     *
     * @param name the player nickname
     * @param team the team selector, either {@code "1"} or {@code "2"}
     * @throws IllegalArgumentException if the team selector is null or invalid
     */
    public void addHumanPlayer(String name, String team) {
        if (team == null) {
            throw new IllegalArgumentException("Il team non può essere null");
        }
        Team selectedTeam = switch (team) { 
            case "1" -> team1;
            case "2" -> team2;
            default -> throw new IllegalArgumentException("Il team deve essere 1 o 2");
        };
        Player p = new HumanPlayer(name, selectedTeam);
        selectedTeam.addPlayer(p);
        this.playerList.add(p);
    }

    /**
     * Adds human players in the order supplied.
     *
     * @param playernames the player nicknames
     * @param teams the corresponding team selectors, either {@code "1"} or {@code "2"}
     * @return this builder
     * @throws IllegalArgumentException if array lengths differ or a team selector is invalid
     */
    public GameBuilder withPlayers(String[] playernames, String[] teams){
        if (playernames.length != teams.length) {
            throw new IllegalArgumentException("Numero di giocatori ≠ numero di teams");
        }
        var n = teams.length;
        for (int i = 0; i < n; i++){
            this.addHumanPlayer(playernames[i], teams[i]);      
        }
        return this;
    }

    /**
     * Appends AI players to fill the target count of each team.
     *
     * The first team is filled before the second; existing players are retained.
     *
     * @param s the strategy shared by the added AI players
     * @return this builder
     * @throws ArrayIndexOutOfBoundsException if more than four AI players are needed
     */
    public GameBuilder fillPlayers(AIStrategy s){
        String[] names = {"Giovanni","Luca","Marco", "Matteo"};
        int missingt1Players = playersPerTeam - team1.size();
        int missingt2Players = playersPerTeam - team2.size();
        int i = 0;
        while (missingt1Players > 0){
            String name = names[i];
            Player p = new AIPlayer(name, team1, s);
            team1.addPlayer(p);
            this.playerList.add(p);
            i ++;
            missingt1Players --;
        }
        while (missingt2Players > 0){
            String name = names[i];
            Player p = new AIPlayer(name, team2, s);
            team2.addPlayer(p);
            this.playerList.add(p);
            i ++;
            missingt2Players --;
        }
        return this;
    }

    /**
     * Creates a game using the configured players, shared teams, rules and observers.
     *
     * The player and observer lists are shared with the created game.
     *
     * @return the configured game
     * @throws IllegalStateException if the player count differs from the requested total
     */
    public Game build(){
        if (playerList.size() != numberOfPlayers) {
            throw new IllegalStateException("Il numero di giocatori non corrisponde a quello richiesto");
        }
        return new Game(numberOfPlayers, playerList, team1, team2, rules, observers);
    }
} 


