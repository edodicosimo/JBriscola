package it.edo.jbriscola.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import it.edo.jbriscola.view.View;

class GameStateTest {
    private Game createGame() {
        return new GameBuilder().withNumberOfPlayers(2)
            .withRules(new BriscolaRules())
            .withPlayers(new String[]{"Alice", "Bob"}, new String[]{"1", "2"})
            .build();
    }

    @Test
    void snapshotIsAvailableBeforeInitialization() {
        Game game = createGame();

        GameState state = game.getState();

        assertEquals(Phase.INITIALIZATION, state.phase());
        assertEquals(GameStatus.READY, state.status());
        assertNull(state.currentPlayerName());
        assertNull(state.briscolaCard());
        assertEquals(0, state.remainingCards());
        assertEquals(Map.of(), state.tableCards());
        assertEquals(List.of(0, 0), state.teamScores());
        assertEquals(List.of(
            new PlayerState("Alice", "Team_1", 0),
            new PlayerState("Bob", "Team_2", 0)
        ), state.players());
    }

    @Test
    void viewReceivesCompletedTransitionsAndOldSnapshotsStayUnchanged()
            throws EmptyDeckException {
        Game game = createGame();
        View view = new View();
        game.addObserver(view);

        game.updateGame();
        GameState initialized = view.getState();
        assertEquals(Phase.TURN, initialized.phase());
        assertEquals("Alice", initialized.currentPlayerName());
        assertEquals(3, initialized.players().get(0).handSize());
        assertEquals(34, initialized.remainingCards());
        assertEquals(Map.of(), initialized.tableCards());
        assertEquals(initialized.briscolaCard().suit(), initialized.trumpSuit());

        game.dealCards();
        assertEquals(3, initialized.players().get(0).handSize());
        assertEquals(4, game.getState().players().get(0).handSize());
        game.changePhase(Phase.TURN);
        game.updateGame();
        assertEquals(Phase.WAIT_HUMAN_PLAY, view.getState().phase());
        assertEquals(GameStatus.NOTREADY, view.getState().status());
    }

    @Test
    void snapshotCopiesCollectionsAndPreservesCardPlayerPlayOrder() {
        Card first = new Card(Rank.RE, Suit.COPPE);
        Card second = new Card(Rank.ASSO, Suit.DENARI);
        PlayerState alice = new PlayerState("Alice", "Team_1", 2);
        PlayerState bob = new PlayerState("Bob", "Team_2", 2);
        Map<Card, PlayerState> played = new LinkedHashMap<>();
        played.put(first, bob);
        played.put(second, alice);
        List<PlayerState> players = new ArrayList<>(List.of(alice, bob));
        List<Integer> scores = new ArrayList<>(List.of(0, 0));
        GameState state = new GameState(Phase.TURN, GameStatus.READY,
            "Alice", null, null, 20, played, players, scores);

        played.clear();
        players.clear();
        scores.set(0, 99);

        assertEquals(List.of(first, second), new ArrayList<>(state.tableCards().keySet()));
        assertEquals(bob, state.tableCards().get(first));
        assertEquals(alice, state.tableCards().get(second));
        assertEquals(List.of(alice, bob), state.players());
        assertEquals(List.of(0, 0), state.teamScores());
        assertThrows(UnsupportedOperationException.class, () -> state.tableCards().clear());
        assertThrows(UnsupportedOperationException.class, () -> state.players().clear());
        assertThrows(UnsupportedOperationException.class, () -> state.teamScores().clear());
    }
}
