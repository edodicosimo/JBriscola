package it.edo.jbriscola.model;

/**
 * Signals that no player remains after the current player in the trick.
 */
public class TurnEndedException extends  Exception{
    /**
     * Creates an exception with a message and its underlying cause.
     *
     * @param m the detail message
     * @param causa the underlying cause
     */
    public TurnEndedException(String m,Throwable causa){
        super(m, causa);
    }
}
