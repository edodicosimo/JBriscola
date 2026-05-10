package it.edo.jbriscola.model;

public class EmptyDeckException extends Exception{
    public EmptyDeckException(String m,Throwable causa){
        super(m, causa);
    }
}