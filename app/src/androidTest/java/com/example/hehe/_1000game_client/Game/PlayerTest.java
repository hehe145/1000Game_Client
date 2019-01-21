package com.example.hehe._1000game_client.Game;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void removeCard()
    {
        Player p = new Player("ada");

        p.addCards("AC");
        p.addCards("AD");
        p.addCards("AH");
        p.addCards("AS");
        p.addCards("KC");
        p.addCards("KD");
        p.addCards("KH");
        p.addCards("KS");
        p.addCards("QD");
        p.addCards("QH");

        p.removeCard("AH");

        StringBuilder text = new StringBuilder();

        for (int i = 0; i < p.getCardNumber();i++)
        {
            text.append(p.getCardCode( i ) ).append(" ");
        }

        assertEquals ( text.toString(), "AC AD AS KC KD KH KS QD QH ");

    }

}