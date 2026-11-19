package it.edo.jbriscola.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import it.edo.jbriscola.model.BriscolaRules;
import it.edo.jbriscola.model.GameState;
import it.edo.jbriscola.model.Phase;

class ControllerTest {
    @Test
    void startGameFillsMissingSeatsAndRegistersTheView() {
        Controller controller = new Controller();
        controller.startGame(new BriscolaRules(), 4,
            new String[]{"Alice"}, new String[]{"1"});

        GameState state = controller.view.getState();
        assertEquals(4, state.players().size());
        assertEquals(2, state.players().stream().filter(p -> p.teamId().equals("Team_1")).count());
        assertEquals(2, state.players().stream().filter(p -> p.teamId().equals("Team_2")).count());
        assertEquals(Phase.WAIT_HUMAN_PLAY, state.phase());
        assertFalse(controller.game.isReady());
        assertTrue(state.players().stream().allMatch(p -> p.handSize() == 3));
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
