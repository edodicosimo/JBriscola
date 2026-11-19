package it.edo.jbriscola.model;

/**
 * Signals an attempt to draw a card from an empty deck.
 */
public class EmptyDeckException extends Exception{
    /**
     * Creates an exception with a message and its underlying cause.
     *
     * @param m the detail message
     * @param causa the underlying cause
     */
    public EmptyDeckException(String m,Throwable causa){
        super(m, causa);
    }
}