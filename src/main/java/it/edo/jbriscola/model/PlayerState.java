package it.edo.jbriscola.model;

/**
 * Represents the public data of a player at a given moment.
 *
 * @param nickname the player nickname
 * @param teamId the identifier of the player's team
 * @param handSize the number of cards in the player's hand
 */
public record PlayerState(String nickname, String teamId, int handSize) {}
