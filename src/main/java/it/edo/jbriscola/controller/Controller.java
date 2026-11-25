package it.edo.jbriscola.controller;

import it.edo.jbriscola.model.Game;
import it.edo.jbriscola.model.GameBuilder;
import it.edo.jbriscola.model.GameRules;
import it.edo.jbriscola.model.HumanStrategy;
import it.edo.jbriscola.model.Phase;
import it.edo.jbriscola.model.RandomStrategy;
import it.edo.jbriscola.view.ConsoleView;
import it.edo.jbriscola.view.View;

public class Controller {
    Game game;
    View view;

    
    public Controller(){
        this.view = new ConsoleView();
    }

    public void startGame(GameRules rules, int n, String[] players, String[] teams){
        var b = new GameBuilder();
        var hs = new HumanStrategy(view);
        this.game = b.withNumberOfPlayers(n).withRules(rules)
            .withPlayers(players, teams, hs).fillPlayers(new RandomStrategy()).build();
        game.addObserver(view);
        game.notifyObservers();
        while (game.getPhase() != Phase.ENDED && game.isReady()) { 
            game.updateGame();
        }
    }
}
