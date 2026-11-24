package it.edo.jbriscola.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import it.edo.jbriscola.model.BriscolaRules;
import it.edo.jbriscola.model.Game;
import it.edo.jbriscola.model.GameBuilder;
import it.edo.jbriscola.model.GameState;
import it.edo.jbriscola.model.Phase;
import it.edo.jbriscola.model.RandomStrategy;

class ConsoleViewTest {
    @Test
    void fourBotsPlayToTheEndWithConsoleUpdates() {
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            Game game = new GameBuilder()
                .withNumberOfPlayers(4)
                .withRules(new BriscolaRules())
                .fillPlayers(new RandomStrategy())
                .build();
            ConsoleView view = new ConsoleView();
            game.addObserver(view);
            game.notifyObservers();

            int updates = 0;
            while (game.getPhase() != Phase.ENDED && game.isReady() && updates < 200) {
                game.updateGame();
                assertEquals(game.getState(), view.getState());
                updates++;
            }

            GameState state = view.getState();
            assertTrue(updates < 200, "La partita deve terminare entro 200 aggiornamenti");
            assertEquals(Phase.ENDGAME, state.phase());
            assertFalse(game.isReady());
            assertEquals(4, state.players().size());
            assertTrue(state.players().stream().allMatch(p -> p.handSize() == 0));
            assertTrue(state.tableCards().isEmpty());
            assertEquals(0, state.remainingCards());
            assertEquals(120, state.teamScores().stream().mapToInt(Integer::intValue).sum());
        });
    }
}
