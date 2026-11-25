package it.edo.jbriscola.view;

import java.util.Scanner;

import it.edo.jbriscola.model.Game;
import it.edo.jbriscola.model.GameState;
import it.edo.jbriscola.model.Card;
import it.edo.jbriscola.model.Hand;

public class ConsoleView implements View{
    private GameState state;
    private final Scanner scanner;

    public ConsoleView(){
        this.scanner = new Scanner(System.in);
    }
    
    @Override
    public void update(Game game) {
        this.state = game.getState(); 
        printState();
    }
    
    public GameState getState() {
        return state;
    }

    /**
     * Displays the hand and requests a card by its one-based position.
     *
     * Invalid input is rejected until a valid selection is entered.
     * The selected card remains in the hand.
     *
     * @param hand the nonempty hand to choose from
     * @return the selected card
     * @throws IllegalArgumentException if the hand is empty
     * @throws IllegalStateException if console input ends before a valid selection
     */
    @Override
    public Card askPlayerForCard(Hand hand){
        var cards = hand.showCards();
        if (cards.isEmpty()) {
            throw new IllegalArgumentException("La mano è vuota");
        }

        System.out.println("Scegli una carta dalla tua mano:");
        for (int i = 0; i < cards.size(); i++) {
            System.out.printf("  %d) %s%n", i + 1, cards.get(i));
        }

        while (true) {
            System.out.printf("Inserisci un numero da 1 a %d: ", cards.size());
            if (!scanner.hasNextLine()) {
                throw new IllegalStateException("Input terminato prima della scelta della carta");
            }
            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= cards.size()) {
                    return cards.get(choice - 1);
                }
            } catch (NumberFormatException e) {
                // Read another line after a nonnumeric selection.
            }
            System.out.println("Scelta non valida. Riprova.");
        }
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
