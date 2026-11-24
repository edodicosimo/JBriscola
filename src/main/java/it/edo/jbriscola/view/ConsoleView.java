package it.edo.jbriscola.view;

import it.edo.jbriscola.model.Game;
import it.edo.jbriscola.model.GameObserver;
import it.edo.jbriscola.model.GameState;

public class ConsoleView implements GameObserver{
    private GameState state;
    
    @Override
    public void update(Game game) {
        this.state = game.getState(); 
        printState();
    }
    
    public GameState getState() {
        return state;
    }

    /**
     * Prints the latest game snapshot to the console.
     *
     * If no snapshot has been received, prints a message indicating that the state is unavailable.
     */
    public void printState() {
        if (state == null) {
            System.out.println("Stato della partita non ancora disponibile.");
            return;
        }

        System.out.println("\n=== Stato della partita ===");
        System.out.println("Fase: " + state.phase());
        System.out.println("Stato: " + state.status());
        System.out.println("Giocatore corrente: "
            + (state.currentPlayerName() == null ? "Non ancora assegnato" : state.currentPlayerName()));
        System.out.println("Carta di briscola: "
            + (state.briscolaCard() == null ? "Non ancora rivelata" : state.briscolaCard()));
        System.out.println("Seme di briscola: "
            + (state.trumpSuit() == null ? "Non ancora assegnato" : state.trumpSuit()));
        System.out.println("Carte nel mazzo: " + state.remainingCards());

        System.out.println("Giocatori in ordine di turno:");
        for (var player : state.players()) {
            System.out.printf("  %s (%s): %d carte in mano%n",
                player.nickname(), player.teamId(), player.handSize());
        }

        System.out.println("Carte sul tavolo:");
        if (state.tableCards().isEmpty()) {
            System.out.println("  Nessuna carta.");
        } else {
            for (var entry : state.tableCards().entrySet()) {
                System.out.printf("  %s (%s): %s%n", entry.getValue().nickname(),
                    entry.getValue().teamId(), entry.getKey());
            }
        }

        System.out.println("Punteggi:");
        for (int i = 0; i < state.teamScores().size(); i++) {
            System.out.printf("  Team_%d: %d punti%n", i + 1, state.teamScores().get(i));
        }
    }
}
