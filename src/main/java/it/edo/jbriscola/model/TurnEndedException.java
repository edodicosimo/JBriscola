package it.edo.jbriscola.model;

public class TurnEndedException extends  Exception{
    public TurnEndedException(String m,Throwable causa){
        super(m, causa);
    }
}
