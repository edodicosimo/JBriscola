package it.edo.jbriscola.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an immutable snapshot of the public game data.
 *
 * @param phase the current game phase
 * @param status whether the game is ready to advance
 * @param currentPlayerName the current player's nickname, or {@code null} before selection
 * @param briscolaCard the revealed trump card, or {@code null} before initialization
 * @param trumpSuit the trump suit, or {@code null} before initialization
 * @param remainingCards the number of cards remaining in the deck
 * @param tableCards the played cards associated with their players in play order
 * @param players the public player data in turn order
 * @param teamScores the team scores in team order
 */
public record GameState(
    Phase phase,
    GameStatus status,
    String currentPlayerName,
    Card briscolaCard,
    Suit trumpSuit,
    int remainingCards,
    Map<Card, PlayerState> tableCards,
    List<PlayerState> players,
    List<Integer> teamScores
) {
    /**
     * Creates a snapshot with independent, unmodifiable collections.
     */
    public GameState {
        tableCards = Collections.unmodifiableMap(new LinkedHashMap<>(tableCards));
        players = List.copyOf(players);
        teamScores = List.copyOf(teamScores);
    }
}
