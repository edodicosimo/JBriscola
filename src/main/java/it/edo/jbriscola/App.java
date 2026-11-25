package it.edo.jbriscola;

import it.edo.jbriscola.controller.Controller;
import it.edo.jbriscola.model.BriscolaRules;

public class App {
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