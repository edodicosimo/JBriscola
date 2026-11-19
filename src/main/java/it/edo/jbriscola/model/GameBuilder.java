package it.edo.jbriscola.model;

import java.util.ArrayList;
import java.util.List;

public class GameBuilder {
    /** The number of players created for the game. */
    private int numberOfPlayers;
    /** The players in circular turn order, starting with the trick leader. */
    private int playersPerTeam;
    private List<Player> playerList = new ArrayList<>();
    /** The two teams to which players are assigned alternately. */
    private Team team1 = new Team("Team_1");
    private Team team2 = new Team("Team_2");
    /** The deck from which players draw cards. */
    /** The revealed card used to determine the trump suit. */
    private GameRules rules;
    /** The observers reserved for game event notifications. */
    private List<GameObserver> observers = new ArrayList<>();

    //////////////////////////////////////////////
    
    
    public GameBuilder withRules(GameRules rules){
        this.rules = rules;
        return this;
    }
    
    public GameBuilder withObservers(List<GameObserver> observers){
        this.observers = new ArrayList<>();
        this.observers.addAll(observers);
        return this;
    }

    public GameBuilder withNumberOfPlayers(int n){
        this.numberOfPlayers = n;
        this.playersPerTeam = n/2;
        return this;
    }

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

    public Game build(){
        if (playerList.size() != numberOfPlayers) {
            throw new IllegalStateException("Il numero di giocatori non corrisponde a quello richiesto");
        }
        return new Game(numberOfPlayers, playerList, team1, team2, rules, observers);
    }
} 


