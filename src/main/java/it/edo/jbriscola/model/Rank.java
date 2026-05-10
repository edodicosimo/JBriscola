package it.edo.jbriscola.model;

/**
 * Represents the ranks of cards in a Briscola deck.
 *
 * Each rank stores both its point value and its trick-taking strength.
 */
public enum Rank{
    ASSO(11,10),
    TRE(10,9),
    RE(4,8),
    CAVALLO(3,7),
    FANTE(2,6),
    SETTE(0,5),
    SEI(0,4),
    CINQUE(0,3),
    QUATTRO(0,2), 
    DUE(0,1);

    private final int points;
    private final int strength;

    Rank(int points, int strength){
        this.points = points;
        this.strength = strength;
    }

    /**
     * Returns the number of points awarded by this rank.
     *
     * @return the Briscola point value
     */
    public int getPoints() {
        return points;
    }

    /**
     * Returns the relative strength of this rank when comparing cards.
     *
     * @return the trick-taking strength
     */
    public int getStrength() {
        return strength;
    }
}
