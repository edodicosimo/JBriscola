package it.edo.jbriscola.view;

import it.edo.jbriscola.model.Game;
import it.edo.jbriscola.model.GameObserver;
import it.edo.jbriscola.model.GameState;

/**
 * Receives game snapshots for presentation.
 */
public class View implements GameObserver {
    /** The most recent game snapshot received by the view. */
    private GameState state;

    /**
     * Updates the view after a game notification.
     *
     * @param game the game that changed
     */
    @Override
    public void update(Game game) {
        this.state = game.getState(); 
    }



    /**
     * Returns the most recent snapshot received by the view.
     *
     * @return the latest snapshot, or {@code null} before the first update
     */
    public GameState getState() {
        return state;
    }
}
