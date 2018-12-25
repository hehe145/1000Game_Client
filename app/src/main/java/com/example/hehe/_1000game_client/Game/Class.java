package com.example.hehe._1000game_client.Game;

public class Class extends Thread
{
    private GameActivity gameActivity;
    public Class (GameActivity gameActivity)
    {
        this.gameActivity = gameActivity;
    }

    public void run()
    {
        gameActivity.start();
    }
}
