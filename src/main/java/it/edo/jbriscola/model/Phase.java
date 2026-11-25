package it.edo.jbriscola.model;

/**
 * Defines the phases processed by the game update loop.
 */
public enum Phase {
    /** Prepares the deck, table and initial hands. */
    INITIALIZATION,
    /** Draws cards after a completed trick. */
    DEALING,
    /** Processes the current player's turn. */
    TURN,
    /** Resolves and collects the played cards. */
    COLLECTINGTRICK,
    /** Stops automatic advancement at the end of the game. */
    ENDGAME,
    /** Represents the terminal phase. */
    ENDED
}
