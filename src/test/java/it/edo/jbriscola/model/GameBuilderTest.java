package it.edo.jbriscola.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import it.edo.jbriscola.view.ConsoleView;

class GameBuilderTest {
    @Test
    void buildRejectsAnIncompletePlayerList() {
        GameBuilder builder = new GameBuilder().withNumberOfPlayers(4)
            .withRules(new BriscolaRules())
            .withPlayers(new String[]{"Alice"}, new String[]{"1"}, new HumanStrategy(new ConsoleView()));

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void explicitlySuppliedObserversStillReceiveUpdates() {
        List<Phase> phases = new ArrayList<>();
        Game game = new GameBuilder().withNumberOfPlayers(2)
            .withRules(new BriscolaRules())
            .withPlayers(new String[]{"Alice", "Bob"}, new String[]{"1", "2"}, new HumanStrategy(new ConsoleView()))
            .withObservers(List.of(g -> phases.add(g.getPhase())))
            .build();

        game.updateGame();

        assertEquals(List.of(Phase.TURN), phases);
    }
}
