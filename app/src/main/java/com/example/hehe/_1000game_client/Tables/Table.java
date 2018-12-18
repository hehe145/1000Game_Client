package com.example.hehe._1000game_client.Tables;

public class Table
{
    private String tableName;
    private String playersNames;

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    private int numberOfPlayers;

    public String getTableName() {
        return tableName;
    }

    public String getPlayersNames() {
        return playersNames;
    }

    public Table (String name, String players, int numberOfPlayers)
    {
        tableName = name;
        playersNames = players;
        this.numberOfPlayers = numberOfPlayers;
    }
}
