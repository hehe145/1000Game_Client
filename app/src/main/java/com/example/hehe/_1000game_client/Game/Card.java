package com.example.hehe._1000game_client.Game;


import com.example.hehe._1000game_client.R;

public class Card
{
    private String cardCode;
    private int image;

    public Card(String cardCode)
    {
        this.cardCode = cardCode;

        if ( cardCode.equals("9H"))
            image = R.drawable.hearts_9;
        else if ( cardCode.equals("9C"))
            image = R.drawable.clubs_9;
        else if ( cardCode.equals("9D"))
            image = R.drawable.diamonds_9;
        else if ( cardCode.equals("9S"))
            image = R.drawable.spades_9;

        else if ( cardCode.equals("10H"))
            image = R.drawable.hearts_10;
        else if ( cardCode.equals("10C"))
            image = R.drawable.clubs_10;
        else if ( cardCode.equals("10D"))
            image = R.drawable.diamonds_10;
        else if ( cardCode.equals("10S"))
            image = R.drawable.spades_10;

        else if ( cardCode.equals("JH"))
            image = R.drawable.hearts_j;
        else if ( cardCode.equals("JC"))
            image = R.drawable.clubs_j;
        else if ( cardCode.equals("JD"))
            image = R.drawable.diamonds_j;
        else if ( cardCode.equals("JS"))
            image = R.drawable.spades_j;

        else if ( cardCode.equals("QH"))
            image = R.drawable.hearts_q;
        else if ( cardCode.equals("QC"))
            image = R.drawable.clubs_q;
        else if ( cardCode.equals("QD"))
            image = R.drawable.diamonds_q;
        else if ( cardCode.equals("QS"))
            image = R.drawable.spades_q;

        else if ( cardCode.equals("KH"))
            image = R.drawable.hearts_k;
        else if ( cardCode.equals("KC"))
            image = R.drawable.clubs_k;
        else if ( cardCode.equals("KD"))
            image = R.drawable.diamonds_k;
        else if ( cardCode.equals("KS"))
            image = R.drawable.spades_k;

        else if ( cardCode.equals("AH"))
            image = R.drawable.hearts_a;
        else if ( cardCode.equals("AC"))
            image = R.drawable.clubs_a;
        else if ( cardCode.equals("AD"))
            image = R.drawable.diamonds_a;
        else if ( cardCode.equals("AS"))
            image = R.drawable.spades_a;

    }

    /**
     * Returns number of drawable image of card
     * @return
     */
    public int getImage()
    {
        return image;
    }

    /**
     * Returns code of card
     * @return
     */
    public String getCardCode()
    {
        return this.cardCode;
    }
}
