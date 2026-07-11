package it.edo.jbriscola.model;

public class AIPlayer extends Player{

    private final AIStrategy strategy;

    public AIPlayer(String nick, Team t, AIStrategy s) {
        super(nick, t);
        this.strategy = s;
    }

    
}
