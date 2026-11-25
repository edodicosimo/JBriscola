package it.edo.jbriscola.controller;

import it.edo.jbriscola.model.Game;
import it.edo.jbriscola.model.GameBuilder;
import it.edo.jbriscola.model.GameRules;
import it.edo.jbriscola.model.HumanStrategy;
import it.edo.jbriscola.model.Phase;
import it.edo.jbriscola.model.RandomStrategy;
import it.edo.jbriscola.view.ConsoleView;
import it.edo.jbriscola.view.View;

/**
 * Builds games, connects the console view and drives the game update loop.
 */
public class Controller {
    /** The game managed by this controller. */
    Game game;
    /** The view used for state notifications and human card selections. */
    View view;

    
    /**
     * Creates a controller with a console view for input and output.
     */
    public Controller(){
        this.view = new ConsoleView();
    }

    /**
     * Builds a game, registers the view and advances play while the game is ready.
     *
     * Human selections are read synchronously through the view. Unfilled team
     * slots are assigned to players using a random strategy.
     *
     * @param rules the rules used to resolve tricks and compute scores
     * @param n the total number of players
     * @param players the human player nicknames
     * @param teams the corresponding team selectors, either {@code "1"} or {@code "2"}
     * @throws IllegalArgumentException if nickname and team array lengths differ or a selector is invalid
     * @throws IllegalStateException if the built player count differs from the requested total or console input ends
     */
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
