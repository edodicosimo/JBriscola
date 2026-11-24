package it.edo.jbriscola.model;

/**
 * Receives notifications after game state changes.
 */
public interface GameObserver {
    /**
     * Receives a notification from a game.
     *
     * @param g the game whose state is available for inspection
     */
    public abstract void update(Game g);
}
