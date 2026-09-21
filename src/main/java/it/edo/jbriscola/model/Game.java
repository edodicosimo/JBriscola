package it.edo.jbriscola.model;

import java.util.List;

public class Game {
    int numberOfPlayers;
    Player[] playerList; 
    List<Team> teams;
    Deck deck;
    Table table;
    Card briscolaCard;  //la briscola che sta in tavola
    Suit trumpSuit;
    Player currentPlayer;
    Phase currentPhase;
    List<GameObserver> observers;

    public Game(String...playerNames){
        this.numberOfPlayers = playerNames.length;
        this.teams = List.of(
            new Team("Team_1"),
            new Team("Team_2")
        );
        this.playerList = new Player[numberOfPlayers];
        int i = 0;
        for (String name : playerNames) {
            this.playerList[i] = new HumanPlayer(name, this.teams.get(i % 2));
            i++;
        }
    }


    public void initializeGame(){
        this.deck = new Deck(true);
        this.table = new Table();
    }

    public void dealInitialCards(){
        for (Player p : playerList){
            p.receiveCard(deck.drawCard());
        }
    }






}
