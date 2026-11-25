package it.edo.jbriscola.model;

import it.edo.jbriscola.view.View;

public class HumanStrategy implements PlayerStrategy{

    private final View view;

    public HumanStrategy(View v){
        this.view = v;
    }

    @Override 
    public Card chooseCard(Hand h){
        return view.askPlayerForCard(h);
    }
}
