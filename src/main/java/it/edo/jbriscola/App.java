package it.edo.jbriscola;

import it.edo.jbriscola.controller.Controller;
import it.edo.jbriscola.model.BriscolaRules;

/**
 * Provides the console entry point for JBriscola.
 */
public class App {
    /**
     * Starts a four-player Briscola game with one human and three random bots.
     *
     * @param args the command-line arguments, currently unused
     */
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.startGame(
            new BriscolaRules(),
            4,
            new String[]{"Edoardo"},
            new String[]{"1"}
        );
    }
}