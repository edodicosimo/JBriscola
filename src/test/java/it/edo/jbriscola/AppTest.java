package it.edo.jbriscola;

// con import static possiamo importare membri statici di una classe
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.edo.jbriscola.model.Card;
import it.edo.jbriscola.model.Rank;
import it.edo.jbriscola.model.Suit;

import org.junit.jupiter.api.Test;

class AppTest{
    @Test
    void projectRuns(){
        assertTrue(true);
    }

    @Test
    void printSomeCards(){
        Card assoDiDenari = new Card(Rank.ASSO, Suit.DENARI);
        Card treDiBastoni = new Card(Rank.TRE, Suit.BASTONI);
        Card reDiCoppe = new Card(Rank.RE, Suit.COPPE);
        Card setteDiSpade = new Card(Rank.SETTE, Suit.SPADE);

        System.out.println(assoDiDenari);
        System.out.println(treDiBastoni);
        System.out.println(reDiCoppe);
        System.out.println(setteDiSpade);
    }
}




