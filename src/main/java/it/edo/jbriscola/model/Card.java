package it.edo.jbriscola.model;

/**
 * Represents a single card in a Briscola deck.
 *
 * A card is defined by its {@link Rank} and {@link Suit}.
 *
 * @param rank the rank of the card
 * @param suit the suit of the card
 */
public record Card(Rank rank, Suit suit){
    /**
     * Returns a text representation of this card.
     *
     * @return the rank followed by the suit
     */
    @Override
    public String toString(){
        return rank + "di" + suit;
    }

    /**
     * Returns the number of points awarded by this card.
     *
     * @return the Briscola point value of the card rank
     */
    public int points(){
        return rank.getPoints();
    }
}
