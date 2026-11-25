package it.edo.jbriscola.view;

import it.edo.jbriscola.model.Card;
import it.edo.jbriscola.model.GameObserver;
import it.edo.jbriscola.model.Hand;

/**
 * Receives game snapshots for presentation.
 */
public interface  View extends GameObserver{
    /**
     * Requests a card selection without removing the card from the hand.
     *
     * @param hand the hand to choose from
     * @return the selected card
     */
    public abstract Card askPlayerForCard(Hand hand);
}
