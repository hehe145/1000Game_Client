package com.example.hehe._1000game_client.Game;

import android.widget.ImageView;

import com.example.hehe._1000game_client.R;

public class Game extends Thread
{
    private ImageView[] listOfButtons;
    private ImageView[] cardsOnTable;
    private ImageView[] cardsOfOponents;
    private String login;
    private Card[] cards;
    private int numberOfCards = 0;
    private int roundPoints = 0;
    private int allPoints = 0;

    public Game( ImageView[] table, ImageView[] cardsOnTable, ImageView[] cardsOfOponents)
    {
        super();
        listOfButtons = table;
        this.cardsOnTable = cardsOnTable;
        this.cardsOfOponents = cardsOfOponents;

    }

    private void clearCards()
    {
        for (int i = 0; i < 10; i++)
            listOfButtons[i].setImageResource(R.color.cardview_dark_background);

        for (int i = 0; i < 3; i++)
            cardsOnTable[i].setImageResource(R.color.cardview_dark_background);

        for (int i = 0; i < 2; i++)
            cardsOfOponents[i].setImageResource(R.color.cardview_dark_background);
    }

}
