package com.example.hehe._1000game_client.Game;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hehe._1000game_client.R;

import java.util.StringTokenizer;

import static com.example.hehe._1000game_client.Server.LoginActivity.serverReader;
import static com.example.hehe._1000game_client.Server.LoginActivity.serverWriter;

public class GameActivity extends AppCompatActivity
{

    private ImageView[] listOfButtons = new ImageView[10];
    private ImageView[] cardsOnTable = new ImageView[3];
    private ImageView[] cardsOfOpponents = new ImageView[2];
    private Button buttonSend;
    private EditText chatMessage;
    private TextView chatView;
    private TextView stakeView;
    private ImageView trumpSuit;
    private TextView[] playerNames = new TextView[3];
    private TextView[] roundPoints = new TextView[3];
    private TextView[] points = new TextView[3];
    private Player[] players;
    private int numberOfCards = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_game);

        Intent i=getIntent();
        String playerName=i.getStringExtra("playerName");

        String message = serverReader.getMessage();

        Toast.makeText( getApplicationContext(), message, Toast.LENGTH_LONG).show();

        initializeButtons();
        initializeImages();
        initializeTextViews();
        setListeners();

        players = new Player[3];

        players[2] = new Player(playerName);
        players[1] = new Player();
        players[0] = new Player();

        Class c = new Class(this);
        c.start();
    }

    public void start()
    {
        while ( ! Thread.currentThread().isInterrupted())
        {
            String message = null;
            message = serverReader.getMessage();

            if (message == null)
                cardsOnTable[1].setImageResource( R.drawable.spades_9);
            else
                cardsOnTable[1].setImageResource( R.drawable.spades_10);

//            if (  message.isEmpty())
//            {
                checkData( "CHAT asfsgaegfas");

            break;
        }
    }

    private void checkData(String message)
    {

        if ( message.startsWith("M "))
        {
            Toast.makeText( getApplicationContext(), message.substring(2), Toast.LENGTH_SHORT).show();
        }else
        if ( message.startsWith("GAMESTARTED"))
        {
            startGame();
        }else
        if ( message.startsWith("LOGOUT"))
        {
            clearTable();
            Toast.makeText( getApplicationContext(), message.substring(7), Toast.LENGTH_SHORT).show();
        }else
        if ( message.startsWith("BIDDING"))
        {
            //okienko ze stawkami
        }else
        if ( message.startsWith("BIGERBID"))
        {
            //okienko ze stawkami do zwiekszenia
        }else
        if ( message.startsWith("BIGGESTBID"))
        {
            StringTokenizer token = new StringTokenizer( message.substring(11));
            endOfBidding( token.nextToken());
        }else
        if ( message.startsWith("POINTS"))
        {
            StringTokenizer token = new StringTokenizer( message.substring(11));
            addPoints( token.nextToken(), token.nextToken());

        }else
        if ( message.startsWith("TOURPOINTS"))
        {
            StringTokenizer token = new StringTokenizer( message.substring(7));
            addToRoundPoints( token.nextToken(), token.nextToken());
        }else
        if ( message.startsWith("BID"))
        {
            setCurrentState(message.substring(4));
        }else
        if ( message.startsWith("YOUGET"))
        {
            players[2].addCard( message.substring(7));
            updateWindow();
        }else
        if ( message.startsWith("RETURN")) // potwierdzenie wyrzucenia
        {
            players[2].removeCard( message.substring(7));
            organizeCards();
        }else
        if ( message.startsWith("RETURNS")) // zmniejsza ilosc kart przeciwnikom
        {
            removeCard( message.substring(8));
        }else
        if ( message.startsWith("THROWEDUP")) // rzucanie 3 kart
        {
            StringTokenizer token = new StringTokenizer(message.substring(10));
            organize( token.nextToken(), token.nextToken());
        }else
        if ( message.startsWith("CHAT"))
        {
            chatView.setText(  message.substring(5));
        }else
        if ( message.startsWith("GAMEOVER"))
        {
            DialogFragment dialog = new EndGameDialog( message.substring(9));
            dialog.show( getSupportFragmentManager(), "EndGameTag");
            clearTable();
        }else
        if ( message.startsWith("PLAYERS"))
        {
            addPlayer( message.substring(8));
        }else
        if ( message.startsWith("REPORT"))
        {
            StringTokenizer token = new StringTokenizer(message.substring(7));
            report( token.nextToken(), token.nextToken() );
        }


    }
    private void report(String suit, String login)
    {
        if ( players[0].getLogin().equals( login))
        {
            players[0].addRoundPoints( getSuitValue(suit));
            roundPoints[0].setText( players[0].getRoundPoints());
        }else
        if ( players[1].getLogin().equals( login))
        {
            players[1].addRoundPoints( getSuitValue(suit));
            roundPoints[1].setText( players[1].getRoundPoints());
        }else
            players[2].addRoundPoints( getSuitValue( suit));
        roundPoints[2].setText( players[2].getRoundPoints());

        trumpSuit.setImageResource( getSuitImage( suit));
        Toast.makeText( getApplicationContext(), "Player " + login + " report suit " + suit + " and gets " + getSuitValue( suit) + " points.", Toast.LENGTH_SHORT).show();
    }

    private int getSuitImage(String suit)
    {
        switch ( suit)
        {
            case "Spades":
                return R.drawable.spades_suit;
            case "Clubs":
                return R.drawable.clubs_suit;
            case "Diamonds":
                return  R.drawable.diamonds_suit;
        }
        return R.drawable.hearts_suit;
    }

    private String getSuitValue(String suit)
    {
        switch ( suit)
        {
            case "Spades":
                return "40";
            case "Clubs":
                return "60";
            case "Diamonds":
                return  "80";
        }
        return "100";
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

    private void removeCardFromPlayer(String login, String cardCode)
    {
        if ( players[0].getLogin().equals( login))
        {
            if ( players[0].getCardNumber() > 0)
            {
                players[0].removeOneCard();
                cardsOfOpponents[0].setImageResource( players[0].getBackCardImmage());
            }else
                cardsOfOpponents[0].setImageResource( R.color.cardview_dark_background);
        }else
        if ( players[1].getLogin().equals( login))
        {
            if ( players[1].getCardNumber() > 0)
            {
                players[1].removeOneCard();
                cardsOfOpponents[1].setImageResource( players[1].getBackCardImmage());
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

    @SuppressLint("SetTextI18n")
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

    private void initializeTextViews()
    {
        stakeView = findViewById( R.id.stake);
        chatMessage = findViewById( R.id.chatEdit);
        chatView = findViewById( R.id.chatView);

        roundPoints[0] = findViewById( R.id.playerRoundPoints0);
        roundPoints[1] = findViewById( R.id.playerRoundPoints1);
        roundPoints[2] = findViewById( R.id.playerRoundPoints0);

        points[0] = findViewById( R.id.playerPoints0);
        points[1] = findViewById( R.id.playerPoints1);
        points[2] = findViewById( R.id.playerPoints2);

        playerNames[0] = findViewById( R.id.playerName0);
        playerNames[1] = findViewById( R.id.playerName1);
        playerNames[2] = findViewById( R.id.playerName2);

        playerNames[0].setText("");
        playerNames[1].setText("");
        playerNames[2].setText("");
    }

    private void initializeImages()
    {
        cardsOnTable[ 0] = findViewById( R.id.cardTable0);
        cardsOnTable[ 1] = findViewById( R.id.cardTable1);
        cardsOnTable[ 2] = findViewById( R.id.cardTable2);

        cardsOfOpponents[ 0] = findViewById( R.id.opponentsCards0);
        cardsOfOpponents[ 1] = findViewById( R.id.opponentsCards1);

        trumpSuit = findViewById( R.id.trumpSuit);
    }

    private void setListeners()
    {
        for (int i = 0; i < 10; i++)
        {
            final int a = i;
            listOfButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    throwCard(a);
                }
            });
        }

        buttonSend.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String text = chatMessage.getText().toString();

                if ( ! text.isEmpty())
                    serverWriter.sendMessage("CHAT " + text);
            }
        });
    }

    private void initializeButtons()
    {
        listOfButtons[ 0] = findViewById( R.id.cardButton0);
        listOfButtons[ 1] = findViewById( R.id.cardButton1);
        listOfButtons[ 2] = findViewById( R.id.cardButton2);
        listOfButtons[ 3] = findViewById( R.id.cardButton3);
        listOfButtons[ 4] = findViewById( R.id.cardButton4);
        listOfButtons[ 5] = findViewById( R.id.cardButton5);
        listOfButtons[ 6] = findViewById( R.id.cardButton6);
        listOfButtons[ 7] = findViewById( R.id.cardButton7);
        listOfButtons[ 8] = findViewById( R.id.cardButton8);
        listOfButtons[ 9] = findViewById( R.id.cardButton9);

        buttonSend = findViewById( R.id.chatButton);


        for (int j = 0; j < 10; j++)
            listOfButtons[ j].setEnabled(false);
    }



    @Override
    public void onBackPressed()
    {
        serverWriter.sendMessage("EXIT");
        String message = serverReader.getMessage();
        AlertDialog.Builder builder = new AlertDialog.Builder( this);
        builder.setMessage( message ).setNegativeButton("No", dialogClickListener)
                .setPositiveButton("Yes", dialogClickListener).setCancelable(false).show();

    }

    private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
            {
                goBack();
                break;
            }


            case DialogInterface.BUTTON_NEGATIVE:
            {
                serverWriter.sendMessage("NO");
                Toast.makeText( getApplicationContext(), serverReader.getMessage(), Toast.LENGTH_LONG).show();
                break;
            }

        }
    }
    };

    private void goBack()
    {
        super.onBackPressed();
    }

}
