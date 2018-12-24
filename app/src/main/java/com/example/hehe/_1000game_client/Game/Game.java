package com.example.hehe._1000game_client.Game;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hehe._1000game_client.R;

import java.util.StringTokenizer;

import static com.example.hehe._1000game_client.Server.LoginActivity.serverReader;
import static com.example.hehe._1000game_client.Server.LoginActivity.serverWriter;

public class Game extends Thread
{
    private ImageView[] listOfButtons;
    private ImageView[] cardsOnTable;
    private ImageView[] cardsOfOpponents;
    private ImageView trumpSuit;
    private TextView chatView;
    private TextView stakeView;
    private TextView[] playerNames;
    private TextView[] roundPoints;
    private TextView[] points;
    private Context context;
    private Player[] players;

    private int numberOfCards = 0;

    public Game(ImageView[] listOfButtons, ImageView[] cardsOnTable, ImageView[] cardsOfOpponents,
                Context context, String playerName, TextView chatView, TextView[] playerNames, TextView[] roundPoints, TextView[] points, ImageView trumpSuit, TextView stake)
    {
        super();
        this.listOfButtons = listOfButtons;
        this.cardsOnTable = cardsOnTable;
        this.cardsOfOpponents = cardsOfOpponents;
        this.context = context;
        this.chatView = chatView;
        this.playerNames = playerNames;
        this.roundPoints = roundPoints;
        this.points = points;
        this.trumpSuit = trumpSuit;
        this.stakeView = stake;

        players = new Player[3];

        players[2] = new Player(playerName);
        players[1] = new Player();
        players[0] = new Player();
    }

    private void clearTable()
    {
        listOfButtons[0].setImageResource(R.color.cardview_dark_background);
        listOfButtons[1].setImageResource(R.color.cardview_dark_background);
        listOfButtons[2].setImageResource(R.color.cardview_dark_background);
        listOfButtons[3].setImageResource(R.color.cardview_dark_background);
        listOfButtons[4].setImageResource(R.color.cardview_dark_background);
        listOfButtons[5].setImageResource(R.color.cardview_dark_background);
        listOfButtons[6].setImageResource(R.color.cardview_dark_background);
        listOfButtons[7].setImageResource(R.color.cardview_dark_background);
        listOfButtons[8].setImageResource(R.color.cardview_dark_background);
        listOfButtons[9].setImageResource(R.color.cardview_dark_background);

        cardsOnTable[0].setImageResource(R.color.cardview_dark_background);
        cardsOnTable[1].setImageResource(R.color.cardview_dark_background);
        cardsOnTable[2].setImageResource(R.color.cardview_dark_background);

        cardsOfOpponents[0].setImageResource(R.color.cardview_dark_background);
        cardsOfOpponents[1].setImageResource(R.color.cardview_dark_background);

        trumpSuit.setImageResource( R.color.cardview_dark_background);

        chatView.setText("");

        stakeView.setText("");

        playerNames[0].setText("");
        playerNames[1].setText("");
        playerNames[2].setText("");

        roundPoints[0].setText(0);
        roundPoints[1].setText(0);
        roundPoints[2].setText(0);

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
            clearTable();
            Toast.makeText( context, "You leave the table", Toast.LENGTH_SHORT);

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
            StringTokenizer token = new StringTokenizer( s.substring(11));
            endOfBidding( token.nextToken());
        }else
        if ( s.startsWith("POINTS"))
        {
            StringTokenizer token = new StringTokenizer( s.substring(11));
            addPoints( token.nextToken(), token.nextToken());

        }else
        if ( s.startsWith("TOURPOINTS"))
        {
            StringTokenizer token = new StringTokenizer( s.substring(7));
            addToRoundPoints( token.nextToken(), token.nextToken());
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
            chatView.setText( s.substring(5));
        }else
        if ( s.startsWith("GAMEOVER"))
        {

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
        if ( players[0].getLogin().equals(login))
        {
            players[0].removeOneCard();
            cardsOfOpponents[0].setImageResource( players[0].getBackCardImmage());
        }else
        {
            players[1].removeOneCard();
            cardsOfOpponents[1].setImageResource( players[1].getBackCardImmage());
        }

    }

    private void organizeCards()
    {
        for ( int i = 0; i < 10; i ++)
        {
            cardsOnTable[i].setImageResource( R.color.cardview_dark_background);
            cardsOnTable[i].setEnabled(false);
        }

        for (int i = 0; i < players[2].getCardNumber(); i++)
        {
            cardsOnTable[i].setImageResource( players[2].getCardImage( i));
            cardsOnTable[i].setEnabled(true);
        }
    }

    private void endOfBidding(String login)
    {
        if ( players[0].getLogin().equals( login))
        {
            players[0].setNumberOfCards(10);
            players[1].setNumberOfCards(7);
        }else
        if ( players[1].getLogin().equals( login))
        {
            players[0].setNumberOfCards(7);
            players[1].setNumberOfCards(10);
        }else
        {
            players[0].setNumberOfCards(7);
            players[1].setNumberOfCards(7);
        }

        cardsOfOpponents[0].setImageResource( players[0].getBackCardImmage());
        cardsOfOpponents[1].setImageResource( players[1].getBackCardImmage());
    }

    private void setCurrentState(String stake)
    {
        stakeView.setText( stake);
    }

    private void addToRoundPoints(String login, String points)
    {
        if ( players[0].getLogin().equals(login))
        {
            players[0].addRoundPoints( points);
            roundPoints[0].setText( players[0].getRoundPoints());
        }else
        if ( players[1].getLogin().equals(login))
        {
            players[1].addRoundPoints( points);
            roundPoints[1].setText( players[1].getRoundPoints());
        }else
        {
            players[2].addRoundPoints( points);
            roundPoints[2].setText( players[2].getRoundPoints());
        }
    }

    private void addPoints(String login, String points)
    {
        if ( players[0].getLogin().equals( login))
        {
            players[0].addPoints( points);
            roundPoints[0].setText( players[0].getPoints());
        }else
        if ( players[1].getLogin().equals( login))
        {
            players[1].addPoints( points);
            roundPoints[1].setText( players[1].getPoints());
        }else
        {
            players[2].addPoints( points);
            roundPoints[2].setText( players[2].getPoints());
        }
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
        cardsOfOpponents[0].setImageResource( R.drawable.card_7);
        cardsOfOpponents[1].setImageResource( R.drawable.card_7);

        playerNames[0].setText( players[0].getLogin());
        playerNames[1].setText( players[1].getLogin());

        stakeView.setText("100");

        points[0].setText("0");
        points[1].setText("0");
        points[2].setText("0");

        roundPoints[0].setText("0");
        roundPoints[1].setText("0");
        roundPoints[2].setText("0");

        players[0].resetTourPoints();
        players[1].resetTourPoints();
        players[2].resetTourPoints();

        trumpSuit.setImageResource( R.color.cardview_dark_background);

        cardsOnTable[0].setImageResource( R.color.cardview_dark_background);
        cardsOnTable[1].setImageResource( R.color.cardview_dark_background);
        cardsOnTable[2].setImageResource( R.color.cardview_dark_background);
    }

    public void throwCard(int a)
    {
        serverWriter.sendMessage("THROW " + players[2].getCardCode( a));
    }
}
