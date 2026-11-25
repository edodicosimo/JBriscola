package it.edo.jbriscola.model;

import it.edo.jbriscola.view.View;

/**
 * Delegates human card selection to a {@link View}.
 */
public class HumanStrategy implements PlayerStrategy{

    /** The view used to request a human card selection. */
    private final View view;

    /**
     * Creates a strategy that requests card selections through the given view.
     *
     * @param v the view used for human input
     */
    public HumanStrategy(View v){
        this.view = v;
    }

    /**
     * Asks the view to select a card without removing it from the hand.
     *
     * @param h the hand to choose from
     * @return the card selected through the view
     */
    @Override 
    public Card chooseCard(Hand h){
        return view.askPlayerForCard(h);
    }
}
