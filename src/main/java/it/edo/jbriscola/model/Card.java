package it.edo.jbriscola.model;

public record Card(Rank rank, Suit suit){
    @Override
    public String toString(){
        return rank + "di" + suit;
    }

    public int points(){
        return rank.getPoints();
    }
}