package com.example.hehe._1000game_client.Game;

public class Player
{
    private Card[] cards;
    private String login;
    private int numberOfCards = 0;
    private int tourPoints = 0;
    private int points = 0;

    public Player()
    {}

    public Player(String login)
    {
        this.login = login;
        cards = new Card[10];
    }

    /**
     * Returns code of given card
     * @param i number of card from player''s deck
     * @return cardCode
     */
    public String getCardCode(int i )
    {
        return cards[i].getCardCode();
    }

    /**
     * Adds card to a deck of player
     * @param cardCode code of card to add
     */
    public void addCard(String cardCode)
    {
        cards[ numberOfCards++] = new Card( cardCode);
    }

    /**
     * Removes card from player's deck
     * @param cardCode code of card to remove
     */
    public void removeCard(String cardCode)
    {
        int a = 0;

        for (int i =0; i < numberOfCards; i++)
        {
            if ( cards[ i].getCardCode().equals( cardCode))
            {
                a = i;
                break;
            }
        }
        numberOfCards--;

        for (int i = a; i < numberOfCards; i++)
            cards[ i] = cards[ i + 1];

    }

    /**
     * Returns number of drawable image of card
     * @param i number of player's card
     * @return number of drawable image
     */
    public int getCardImage( int i)
    {
        return cards[ i].getImage();
    }

    /**
     * Returns player's login
     * @return
     */
    public String getLogin()
    {return this.login;}

    /**
     * Adds points to player's account
     * @param points
     */
    public void addPoints( String points)
    {
        this.points += Integer.parseInt( points);
    }

    /**
     * Returns player's points
     * @return
     */
    public int getPoints()
    {
        return points;
    }

    /**
     * Adds points to player's tourPoints
     * @param points
     */
    public void addTourPoints(String points)
    {
        this.tourPoints += Integer.parseInt( points);
    }

    /**
     * Returns player's tour points
     * @return
     */
    public int getTourPoints()
    {return this.tourPoints;}

    /**
     * Sets player's tour points to 0
     */
    public void resetTourPoints()
    {
        tourPoints = 0;
    }

    /**
     * Sets player's name
     * @param name
     */
    public void setLogin(String name)
    {
        login = name;
    }

    /**
     * Sets number of player's cards
     * @param i number of cards
     */
    public void setNumberOfCards( int i)
    {
        numberOfCards = i;
    }

    /**
     * Decreases number of player's card by 1;
     */
    public void removeOneCard()
    {
        numberOfCards--;
    }

    public int getCardNumber()
    {
        return numberOfCards;
    }
}