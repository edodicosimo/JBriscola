package it.edo.jbriscola.controller;

import it.edo.jbriscola.model.Game;
import it.edo.jbriscola.model.GameRules;
import it.edo.jbriscola.model.Phase;

public class Controller {
    Game game;

    public Controller(){
    }

    public void startGame(GameRules rules, String...playerNames){
        this.game = new Game(rules, playerNames);
        while (game.getPhase() != Phase.ENDED && game.isReady()) { 
            game.updateGame();
        }
    }


}
