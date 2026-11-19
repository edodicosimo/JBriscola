package it.edo.jbriscola.model;

/**
 * Indicates whether the game can advance without waiting for input.
 */
public enum GameStatus {
    /** Allows the game to advance. */
    READY,
    /** Pauses automatic advancement. */
    NOTREADY
}
