package it.edo.jbriscola.model;

/**
 * Represents the ranks of cards in a Briscola deck.
 *
 * Each rank stores both its point value and its trick-taking strength.
 */
public enum Rank{
    /** The asso rank. */
    ASSO(11,10),
    /** The tre rank. */
    TRE(10,9),
    /** The re rank. */
    RE(4,8),
    /** The cavallo rank. */
    CAVALLO(3,7),
    /** The fante rank. */
    FANTE(2,6),
    /** The sette rank. */
    SETTE(0,5),
    /** The sei rank. */
    SEI(0,4),
    /** The cinque rank. */
    CINQUE(0,3),
    /** The quattro rank. */
    QUATTRO(0,2), 
    /** The due rank. */
    DUE(0,1);

    /** The points awarded when the card is collected. */
    private final int points;
    /** The relative strength used to resolve tricks. */
    private final int strength;

    /**
     * Creates a rank with its scoring value and trick strength.
     *
     * @param points the Briscola point value
     * @param strength the relative trick-taking strength
     */
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
