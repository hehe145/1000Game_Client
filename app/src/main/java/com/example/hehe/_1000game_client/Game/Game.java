package com.example.hehe._1000game_client.Game;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hehe._1000game_client.R;

import java.util.StringTokenizer;

import static com.example.hehe._1000game_client.Server.LoginActivity.serverReader;

public class Game extends Thread
{
    private ImageView[] listOfButtons;
    private ImageView[] cardsOnTable;
    private ImageView[] cardsOfOpponents;
    private Context context;
    private Player[] players;

    private int numberOfCards = 0;

    public Game(ImageView[] listOfButtons, ImageView[] cardsOnTable, ImageView[] cardsOfOpponents, Context context, String playerName)
    {
        super();
        this.listOfButtons = listOfButtons;
        this.cardsOnTable = cardsOnTable;
        this.cardsOfOpponents = cardsOfOpponents;
        this.context = context;

        players = new Player[3];

        players[2] = new Player(playerName);
        players[1] = new Player();
        players[0] = new Player();
    }

    private void clearCards()
    {
        for (int i = 0; i < 10; i++)
            listOfButtons[i].setImageResource(R.color.cardview_dark_background);

        for (int i = 0; i < 3; i++)
            cardsOnTable[i].setImageResource(R.color.cardview_dark_background);

        for (int i = 0; i < 2; i++)
            cardsOfOpponents[i].setImageResource(R.color.cardview_dark_background);
    }

    public void run()
    {
            StringBuilder stringBuilder = new StringBuilder("");

            while (true)
            {
                stringBuilder.append( serverReader.getMessage());

                if ( stringBuilder.length() > 0)
                {
                    checkData( stringBuilder.toString());
                }

                stringBuilder = new StringBuilder("");
            }




    }

    private void checkData(String s)
    {

        if ( s.startsWith("M "))
        {
            Toast.makeText( context, s.substring(2), Toast.LENGTH_SHORT);
        }else
        if ( s.startsWith("GAMESTARTED"))
        {
            startGame();
        }else
        if ( s.startsWith("LOGOUT"))
        {
            //logout
            
            reset();
        }else
        if ( s.startsWith("BIDDING"))
        {
            //okienko ze stawkami
        }else
        if ( s.startsWith("BIGERBID"))
        {
            //okienko ze stawkami do zwiekszenia
        }else
        if ( s.startsWith("BIGGESTBID"))
        {
            //karty ze stoøu set icon background
            StringTokenizer token = new StringTokenizer( s.substring(11));
            endOfBidding( token.nextToken());
        }else
        if ( s.startsWith("POINTS"))
        {
            StringTokenizer token = new StringTokenizer( s.substring(7));
            addToRoundPoints( token.nextToken(), token.nextToken());
        }else
        if ( s.startsWith("TOURPOINTS"))
        {
            StringTokenizer token = new StringTokenizer( s.substring(11));
            addPoints( token.nextToken(), token.nextToken());
        }else
        if ( s.startsWith("BID"))
        {
            setCurrentState(s.substring(4));
        }else
        if ( s.startsWith("YOUGET"))
        {
            players[2].addCard( s.substring(7));
            updateWindow();
        }else
        if ( s.startsWith("RETURN")) // potwierdzenie wyrzucenia
        {
            players[2].removeCard( s.substring(7));
            organizeCards();
        }else
        if ( s.startsWith("RETURNS")) // zmniejsza ilosc kart przeciwnikom
        {
            removeCard( s.substring(8));
        }else
        if ( s.startsWith("THROWEDUP")) // rzucanie 3 kart
        {
            StringTokenizer token = new StringTokenizer(s.substring(10));
            organize( token.nextToken(), token.nextToken());
        }else
        if ( s.startsWith("CZAT"))
        {
            //edycja pola tekstowego
        }else
        if ( s.startsWith("GAMEOVER"))
        {
            //Message box o wygranej
        }else
        if ( s.startsWith("PLAYERS"))
        {
            addPlayer( s.substring(8));
        }
        if ( s.startsWith("REPORT"))
        {
            StringTokenizer token = new StringTokenizer(s.substring(7));
            report( token.nextToken(), token.nextToken() );
        }


    }
    private void report(String s, String s1)
    {

    }

    private void addPlayer(String login)
    {
        if ( ! players[2].getLogin().equals( login))
        {
            if ( players[0].getLogin().equals( ""))
                players[0] = new Player( login);
            else
                players[1] = new Player( login);
        }

    }

    private void organize(String login, String cardCode)
    {
        removeCardFromPlayer(login, cardCode);

        Card card = new Card( cardCode);

        cardsOnTable[ numberOfCards++].setImageResource( card.getImage());

        if ( numberOfCards == 3)
        {
            long start = System.currentTimeMillis();
            long end = start + 2000;

            while (System.currentTimeMillis() < end){}

            cardsOnTable[0].setImageResource( R.color.cardview_dark_background);
            cardsOnTable[1].setImageResource( R.color.cardview_dark_background);
            cardsOnTable[2].setImageResource( R.color.cardview_dark_background);

            numberOfCards =0;
        }
    }

    private void removeCardFromPlayer(String login, String cardCode)
    {
        if ( players[0].getLogin().equals( login))
        {
            if ( players[0].getCardNumber() > 0)
            {
                //TODO
                //cardsOfOpponents[0].setImageResource();
                players[0].removeOneCard();
            }else
                cardsOfOpponents[0].setImageResource( R.color.cardview_dark_background);
        }else
        if ( players[1].getLogin().equals( login))
        {
            if ( players[10].getCardNumber() > 0)
            {
                //TODO
                //cardsOfOpponents[1].setImageResource();
                players[1].removeOneCard();
            }else
                cardsOfOpponents[1].setImageResource( R.color.cardview_dark_background);
        }else
        {
            players[2].removeCard( cardCode);
            organizeCards();
        }
    }

    private void removeCard(String login)
    {

    }

    private void organizeCards()
    {

    }

    private void endOfBidding(String s) {
    }

    private void setCurrentState(String substring) {
    }

    private void addToRoundPoints(String s, String s1) {
    }

    private void addPoints(String s, String s1) {
    }

    private void updateWindow()
    {
        int i = players[2].getCardNumber() - 1;
        listOfButtons[ i].setImageResource( players[2].getCardImage( i));
        listOfButtons[ i].setEnabled( true);
    }

    private void reset()
    {

    }

    private void startGame()
    {
        cardsOfOpponents[0].setImageResource(null);
        cardsOfOpponents[1].setImageResource(null);

        //TextView login
        //TextView login

        //TextView stawka

        //TextView punktyrundy
        //TextView punktyrundy
        //TextView punktyrundy

        //TextView punkty
        //TextView punkty
        //TextView punkty

        //Karty do wygrania

        players[0].resetTourPoints();
        players[1].resetTourPoints();
        players[2].resetTourPoints();

        //TextView kolor atutowy
    }

}
