package it.edo.jbriscola.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.parallel.ResourceLock;

import it.edo.jbriscola.view.ConsoleView;

import org.junit.jupiter.api.Test;

import it.edo.jbriscola.model.BriscolaRules;
import it.edo.jbriscola.model.GameState;
import it.edo.jbriscola.model.Phase;

@ResourceLock("SYSTEM_IN")
class ControllerTest {
    @Test
    void oneHumanAndThreeBotsFinishWithFirstCardSelections() {
        playWithFirstCardSelections(4);
    }

    @Test
    void oneHumanAndOneBotFinishWithFirstCardSelections() {
        playWithFirstCardSelections(2);
    }

    private void playWithFirstCardSelections(int playerCount) {
        int expectedSelections = 40 / playerCount;
        ByteArrayInputStream input = new ByteArrayInputStream(
            "1\n".repeat(expectedSelections).getBytes(StandardCharsets.UTF_8));
        InputStream originalInput = System.in;
        try {
            System.setIn(input);
            assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
                Controller controller = new Controller();
                controller.startGame(new BriscolaRules(), playerCount,
                    new String[]{"Alice"}, new String[]{"1"});

                GameState state = controller.game.getState();
                assertEquals(Phase.ENDGAME, state.phase());
                assertFalse(controller.game.isReady());
                assertEquals(playerCount, state.players().size());
                assertEquals(playerCount / 2,
                    state.players().stream().filter(p -> p.teamId().equals("Team_1")).count());
                assertEquals(playerCount / 2,
                    state.players().stream().filter(p -> p.teamId().equals("Team_2")).count());
                assertTrue(state.players().stream().allMatch(p -> p.handSize() == 0));
                assertTrue(state.tableCards().isEmpty());
                assertEquals(0, state.remainingCards());
                assertEquals(120, state.teamScores().stream().mapToInt(Integer::intValue).sum());
                assertEquals(state, ((ConsoleView) controller.view).getState());
                assertEquals(0, input.available(), "Tutte le scelte umane devono essere consumate");
            });
        } finally {
            System.setIn(originalInput);
        }
    }

    @Test
    void botOnlyGamesFinishAndScoresUseTheSharedTeams() {
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            for (int count : new int[]{2, 4}) {
                Controller controller = new Controller();
                controller.startGame(new BriscolaRules(), count, new String[0], new String[0]);

                GameState state = controller.game.getState();
                assertEquals(count, state.players().size());
                assertEquals(Phase.ENDGAME, state.phase());
                assertTrue(controller.game.isGameOver());
                assertEquals(120, state.teamScores().stream().mapToInt(Integer::intValue).sum());
                assertEquals(state.teamScores().stream().mapToInt(Integer::intValue).max().orElseThrow(),
                    controller.game.computeScore(controller.game.computeWinningTeam()));
            }
        });
    }
}
