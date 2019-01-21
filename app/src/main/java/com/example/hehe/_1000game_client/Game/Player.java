package com.example.hehe._1000game_client.Game;


import com.example.hehe._1000game_client.R;

import java.util.StringTokenizer;

public class Player {
    private Card[] cards;
    private String login ="";
    private int numberOfCards = 0;
    private int tourPoints = 0;
    private int points = 0;

    public Player() {
    }

    public Player(String login) {
        this.login = login;
        cards = new Card[10];
    }

    public Player(String login, int n)
    {
        this.login = login;
        cards = new Card[10];
        numberOfCards = n;
    }

    /**
     * Returns code of given card
     *
     * @param i number of card from player''s deck
     * @return cardCode
     */
    public String getCardCode(int i) {
        return cards[i].getCardCode();
    }

    /**
     * Add cards to a deck of player
     *
     * @param cardCode code of card to add
     */
    public void addCards(String cardCode)
    {
        StringTokenizer token = new StringTokenizer(cardCode, " ");

        while( token.hasMoreElements())
        {
            cards[numberOfCards++] = new Card( token.nextToken());
        }

    }

    /**
     * Removes card from player's deck
     *
     * @param cardCode code of card to remove
     */
    public void removeCard(String cardCode) {
        int a = 0;

        for (int i = 0; i < numberOfCards; i++) {
            if (cards[i].getCardCode().equals(cardCode)) {
                a = i;
                break;
            }
        }

        numberOfCards--;

        for (int i = a; i < numberOfCards; i++)
            cards[i] = cards[i + 1];

    }

    /**
     * Returns number of drawable image of card
     *
     * @param i number of player's card
     * @return number of drawable image
     */
    public int getCardImage(int i) {
        return cards[i].getImage();
    }

    /**
     * Returns player's login
     *
     * @return
     */
    public String getLogin() {
        return this.login;
    }

    /**
     * Adds points to player's account
     *
     * @param points
     */
    public void addPoints(String points) {
        this.points += Integer.parseInt(points);
    }

    /**
     * Returns player's points
     *
     * @return
     */
    public int getPoints() {
        return points;
    }


    /**
     * Returns player's tour points
     *
     * @return
     */

    public int getRoundPoints() {
        return this.tourPoints;
    }

    /**
     * Sets player's tour points to 0
     */
    public void resetTourPoints() {
        tourPoints = 0;
    }

    /**
     * Sets player's name
     *
     * @param name
     */
    public void setLogin(String name) {
        login = name;
    }

    /**
     * Sets number of player's cards
     *
     * @param i number of cards
     */
    public void setNumberOfCards(int i) {
        numberOfCards = i;
    }

    /**
     * Decreases number of player's card by 1;
     */
    public void removeOneCard() {
        numberOfCards--;
    }

    /**
     * Return number of cards that player has
     *
     * @return
     */
    public int getCardNumber() {
        return numberOfCards;
    }

    /**
     * Returns number of image
     *
     * @return
     */
    public int getBackCardImage() {
        int image;
        if (numberOfCards == 0)
            image = R.color.cardview_dark_background;
        else if (numberOfCards == 1)
            image = R.drawable.card_1;
        else if (numberOfCards == 2)
            image = R.drawable.card_2;
        else if (numberOfCards == 3)
            image = R.drawable.card_3;
        else if (numberOfCards == 4)
            image = R.drawable.card_4;
        else if (numberOfCards == 5)
            image = R.drawable.card_5;
        else if (numberOfCards == 6)
            image = R.drawable.card_6;
        else if (numberOfCards == 7)
            image = R.drawable.card_7;
        else if (numberOfCards == 8)
            image = R.drawable.card_8;
        else if (numberOfCards == 9)
            image = R.drawable.card_9;
        else
            image = R.drawable.card_10;

        return image;
    }

    public void addRoundPoints(String points)
    {
        this.tourPoints += Integer.parseInt( points );
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }
}

