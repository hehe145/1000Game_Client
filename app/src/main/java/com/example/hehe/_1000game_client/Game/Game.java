package com.example.hehe._1000game_client.Game;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
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

public class Game extends Thread
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
    private GameActivity gameActivity;
    private int numberOfCards;

    public Game(GameActivity gameActivity, String playerName)
    {
        this.gameActivity = gameActivity;

        initializeButtons();
        initializeImages();
        initializeTextViews();
        setListeners();


        players = new Player[3];

        players[2] = new Player(playerName);
        players[1] = new Player();
        players[0] = new Player();
    }


    /**
     * Runs thread
     */
    public void run()
    {
        String message;
        while ( ! Thread.currentThread().isInterrupted())
        {
            message = serverReader.getMessage();


            if ( ! message.isEmpty() )
            {
                checkData( message);
            }

        }
    }

    /**
     * Checks data and launch correct method
     * @param message message from server
     */
    private void checkData(String message)
    {
        if ( message.startsWith("M "))
        {
            messageShow(message.substring(2));
        }else
        if ( message.startsWith("GAMESTARTED "))
        {
            startGame();
        }else
        if ( message.startsWith("LOGOUT "))
        {
            clearTable();
            messageShow( message.substring(7));
        }else
        if ( message.startsWith("BIDDING "))
        {
            bid( message.substring(8));
        }else
        if ( message.startsWith("BIGERBID "))
        {
            raise( message.substring(9));
        }else
        if ( message.startsWith("BIGGESTBID "))
        {
            StringTokenizer token = new StringTokenizer( message.substring(11));
            endOfBidding( token.nextToken());
        }else
        if ( message.startsWith("POINTS "))
        {
            StringTokenizer token = new StringTokenizer( message.substring(11));
            addPoints( token.nextToken(), token.nextToken());

        }else
        if ( message.startsWith("TOURPOINTS "))
        {
            StringTokenizer token = new StringTokenizer( message.substring(11));
            addToRoundPoints( token.nextToken(), token.nextToken());
        }else
        if ( message.startsWith("BID "))
        {
            setCurrentState(message.substring(4));
        }else
        if ( message.startsWith("YOURECIVE "))
        {
            players[2].addCards( message.substring(9));
            createImages();
        }else
        if ( message.startsWith("RECIVEONE "))
        {
            players[2].addCards( message.substring(9));
            updateWindow();
        }else
        if ( message.startsWith("PLAYERS "))
        {
            addPlayers( message.substring(7));
        }else
        if ( message.startsWith("YOURETURN "))
        {
            players[2].removeCard( message.substring(7));
            organizeCards();
        }else
        if ( message.startsWith("RETURNS "))
        {
            removeCard( message.substring(8));
        }else
        if ( message.startsWith("THROWEDUP "))
        {
            StringTokenizer token = new StringTokenizer(message.substring(10));
            throwsCard( token.nextToken(), token.nextToken());
        }else
        if ( message.startsWith("CHAT "))
        {

            chat(  message.substring(5));
        }else
        if ( message.startsWith("GAMESTARTS "))
        {
            cardsView();
        }else
        if ( message.startsWith("ENDOFTOUR "))
        {
            nextTour();
        }
        if ( message.startsWith("GAMEOVER "))
        {
            DialogFragment dialog = new EndGameDialog( message.substring(9));
            dialog.show( gameActivity.getSupportFragmentManager(), "EndGameTag");
            clearTable();
        }else
        if ( message.startsWith("REPORT "))
        {
            StringTokenizer token = new StringTokenizer(message.substring(7));
            report( token.nextToken(), token.nextToken() );
        }


    }

    private void nextTour() {
        gameActivity.runOnUiThread(new Runnable()
        {
            public void run()
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

                cardsOfOpponents[0].setImageResource(R.drawable.card_7);
                cardsOfOpponents[1].setImageResource(R.drawable.card_7);

                trumpSuit.setImageResource( R.color.cardview_dark_background);

                stakeView.setText("100");

                roundPoints[0].setText("0");
                roundPoints[1].setText("0");
                roundPoints[2].setText("0");
            }
        });
        players[0].setNumberOfCards(7);
        players[1].setNumberOfCards(7);
    }

    /**
     * Updates images of number of cards that every player has
     */
    private void cardsView()
    {
        players[0].setNumberOfCards(8);
        players[1].setNumberOfCards(8);

        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cardsOfOpponents[ 0 ].setImageResource( players[ 0 ].getBackCardImage() );
                cardsOfOpponents[ 1 ].setImageResource( players[ 1 ].getBackCardImage() );
            }
        });
    }

    /**
     * Changes images of cards in imageViews and enables those which player can throw
     */
    private void createImages()
    {
        gameActivity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                int numberOfCards = players[ 2].getNumberOfCards();
                for (int i = 0; i < numberOfCards; i++)
                {
                    listOfButtons[ i ].setImageResource( players[ 2 ].getCardImage( i ));
                    listOfButtons[ i ].setEnabled( true );
                }

                for (int i = numberOfCards; i < 10; i++)
                {
                    listOfButtons[ i ].setImageResource( R.color.cardview_dark_background );
                    listOfButtons[ i ].setEnabled( true );
                }
            }
        });

    }

    /**
     * Shows Bidder dialog which enables player to increase the stake.
     * When player wins bidding he can increase number of points.
     *
     * @param message value of current stake
     */
    private void raise(String message)
    {
        int value = Integer.parseInt( message);

        FragmentTransaction ft = gameActivity.getSupportFragmentManager().beginTransaction();
        Bidder bidder = new Bidder();

        Bundle args = new Bundle();
        args.putInt( "stake", ( value+10 ) );
        args.putString( "name", "You won bidding! You can raise stake or play." );
        args.putString( "firstBtn", "PLAY" );
        args.putString( "secondBtn", "RAISE" );
        args.putInt( "bool", 0 );

        bidder.setArguments(args);
        bidder.show(ft, "dialog");
    }

    /**
     *  Shows Bidder dialog which enables player to increase the stake.
     * @param message value of current stake
     */
    private void bid(String message)
    {
        int value = Integer.parseInt( message);

        FragmentTransaction ft = gameActivity.getSupportFragmentManager().beginTransaction();
        Bidder bidder = new Bidder();

        Bundle args = new Bundle();
        args.putInt("stake", (value+10));
        args.putString("name", "Select your stake or fold.");
        args.putString("firstBtn", "FOLD");
        args.putString("secondBtn", "BID");
        args.putInt("bool", 1);

        bidder.setArguments(args);
        bidder.show(ft, "dialog");

    }

    /**
     * Shows message in Toast dialog
     * @param message
     */
    private void messageShow(final String message)
    {
        gameActivity.runOnUiThread(new Runnable()
        {
            public void run() {
                Toast.makeText( gameActivity, message, Toast.LENGTH_LONG ).show();
            }
        });
    }

    /**
     * Shows message in chatView
     * @param message message to all players
     */
    public void chat( final String message)
    {

        gameActivity.runOnUiThread(new Runnable()
        {
            public void run() {
                chatView.setText(  message + "\n" + chatView.getText().toString() );
            }
        });
    }

    /**
     * Increase number of points player gets after reporting suit
     * @param suit
     * @param login
     */
    private void report(final String suit, final String login)
    {
        gameActivity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                if (players[0].getLogin().equals(login)) {
                    players[0].addRoundPoints(getSuitValue(suit));
                    roundPoints[0].setText( Integer.toString( players[0].getRoundPoints() ));
                } else if (players[1].getLogin().equals(login)) {
                    players[1].addRoundPoints(getSuitValue(suit));
                    roundPoints[1].setText( Integer.toString( players[1].getRoundPoints() ));
                } else
                {
                    players[2].addRoundPoints(getSuitValue(suit));
                    roundPoints[2].setText( Integer.toString( players[2].getRoundPoints() ));
                }


                trumpSuit.setImageResource(getSuitImage(suit));
            }
        });

        messageShow("Player " + login + " report suit " + suit + " and gets " + getSuitValue( suit) + " points.");
    }

    /**
     * Returns number of image of suit
     * @param suit suit of card
     * @return number of drawable object
     */
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

    /**
     * Returns value of suit
     * @param suit
     * @return points of suit
     */
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

    /**
     * Adds players to array of players
     * @param logins logins of players in String separated with empty char
     */
    private void addPlayers(String logins) {
        StringTokenizer token = new StringTokenizer(logins, " ");

        while (token.hasMoreElements()) {
            String login = token.nextToken();

            if (!players[2].getLogin().equals(login)) {
                if (players[0].getLogin().equals(""))
                    players[0] = new Player(login, 7);

                else {
                    players[1] = new Player(login, 7);
                    break;
                }
            }
        }
        updatePlayerNames();
    }

    /**
     * Updates textViews after player joined to table
     */
    private void updatePlayerNames() {
        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(int i =0; i<2; i++)
                {
                    playerNames[i].setText( players[i].getLogin());
                }
            }
        });

    }

    /**
     * Updates view when one of the player throws card
     * @param login login of
     * @param cardCode
     */
    private void throwsCard(String login,final String cardCode)
    {

        removeCardFromPlayer(login, cardCode);

        gameActivity.runOnUiThread(new Runnable() {
            public void run() {
                Card card = new Card(cardCode);

                cardsOnTable[ numberOfCards ].setImageResource(card.getImage());
            }
        });

        numberOfCards++;

        if (numberOfCards == 3)
        {
            nextRound();
        }
    }

    /**
     * Waits 3 seconds and clears table.
     * After every player throws a card starts new round.
     */
    private void nextRound() {
        long start = System.currentTimeMillis();
        long end = start + 3000;

        while (System.currentTimeMillis() < end) {} //timer 3 sec

        gameActivity.runOnUiThread(new Runnable() {
            public void run() {
               cardsOnTable[0].setImageResource(R.color.cardview_dark_background);
               cardsOnTable[1].setImageResource(R.color.cardview_dark_background);
               cardsOnTable[2].setImageResource(R.color.cardview_dark_background);
            }
        });

        numberOfCards = 0;
    }

    /**
     *
     */
    private void clearTable()
    {
        gameActivity.runOnUiThread(new Runnable()
        {
            public void run()
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

                roundPoints[0].setText("0");
                roundPoints[1].setText("0");
                roundPoints[2].setText("0");
            }
        });


    }

    private void removeCardFromPlayer( final String login, final String cardCode)
    {
        gameActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (players[0].getLogin().equals(login)) {
                    if (players[0].getCardNumber() > 0) {
                        players[0].removeOneCard();
                        cardsOfOpponents[0].setImageResource(players[0].getBackCardImage());
                    } else
                        cardsOfOpponents[0].setImageResource(R.color.cardview_dark_background);
                } else if (players[1].getLogin().equals(login)) {
                    if (players[1].getCardNumber() > 0) {
                        players[1].removeOneCard();
                        cardsOfOpponents[1].setImageResource(players[1].getBackCardImage());
                    } else
                        cardsOfOpponents[1].setImageResource(R.color.cardview_dark_background);
                } else {
                    players[2].removeCard(cardCode);
                    organizeCards();
                }
            }
        });
    }

    private void removeCard( final String login)
    {
        gameActivity.runOnUiThread( new Runnable() {
            public void run() {
                if (players[0].getLogin().equals(login))
                {
                    players[0].removeOneCard();
                    cardsOfOpponents[0].setImageResource( players[0].getBackCardImage());
                } else {
                    players[1].removeOneCard();
                    cardsOfOpponents[1].setImageResource( players[1].getBackCardImage());
                }
            }
        });

    }

    private void organizeCards()
    {
        gameActivity.runOnUiThread(new Runnable() {
            public void run() {
                int numberOfCards = players[2].getNumberOfCards();
                listOfButtons[ numberOfCards ].setImageResource(R.color.cardview_dark_background);
                listOfButtons[ numberOfCards ].setEnabled(false);

                for (int i = 0; i < numberOfCards; i++) {
                    listOfButtons[i].setImageResource(players[2].getCardImage(i));
                }

                for (int i = numberOfCards; i < 10; i++)
                {
                    listOfButtons[i].setImageResource( R.color.cardview_dark_background );
                    listOfButtons[i].setEnabled( false );
                }
            }
        });

    }

    private void endOfBidding(final String login)
    {
        gameActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (players[0].getLogin().equals(login)) {
                    players[0].setNumberOfCards(10);
                    players[1].setNumberOfCards(7);
                } else if (players[1].getLogin().equals(login)) {
                    players[0].setNumberOfCards(7);
                    players[1].setNumberOfCards(10);
                } else {
                    players[0].setNumberOfCards(7);
                    players[1].setNumberOfCards(7);
                }

                cardsOfOpponents[0].setImageResource(players[0].getBackCardImage());
                cardsOfOpponents[1].setImageResource(players[1].getBackCardImage());
            }
        });
    }

    private void setCurrentState(final String stake) {
        gameActivity.runOnUiThread(new Runnable() {
            public void run() {
                stakeView.setText(stake);
            }
        });
    }

    private void addToRoundPoints( final String login, final String points)
    {
        gameActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (players[0].getLogin().equals(login)) {
                    players[0].addRoundPoints(points);
                    roundPoints[0].setText( Integer.toString( players[0].getRoundPoints() ));
                } else if (players[1].getLogin().equals(login)) {
                    players[1].addRoundPoints(points);
                    roundPoints[1].setText( Integer.toString( players[1].getRoundPoints() ));
                } else {
                    players[2].addRoundPoints(points);
                    roundPoints[2].setText( Integer.toString( players[2].getRoundPoints() ));
                }
            }
        });
    }

    private void addPoints(final String login, final String points)
    {
        gameActivity.runOnUiThread(new Runnable()
        {
            public void run() {
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

        });

    }

    private void updateWindow() {
        gameActivity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                int number = players[2].getNumberOfCards();
                listOfButtons[ number - 1].setImageResource( players[2].getCardImage(number - 1));
                listOfButtons[ number - 1].setEnabled(true);
            }

        });
    }

    private void startGame()
    {
        gameActivity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                cardsOfOpponents[0].setImageResource( R.drawable.card_7);
                cardsOfOpponents[1].setImageResource( R.drawable.card_7);

                playerNames[0].setText( players[0].getLogin());
                playerNames[1].setText( players[1].getLogin());
                playerNames[2].setText( players[2].getLogin());

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
        });

    }

    private void throwCard( int a)
    {
        serverWriter.sendMessage("THROW " + players[2].getCardCode( a));
    }

    private void initializeTextViews()
    {
        stakeView = gameActivity.findViewById( R.id.stake);
        chatMessage = gameActivity.findViewById( R.id.chatEdit);
        chatView = gameActivity.findViewById( R.id.chatView);
        chatView.setMovementMethod(new ScrollingMovementMethod());

        roundPoints[0] = gameActivity.findViewById( R.id.playerRoundPoints0);
        roundPoints[1] = gameActivity.findViewById( R.id.playerRoundPoints1);
        roundPoints[2] = gameActivity.findViewById( R.id.playerRoundPoints2);

        points[0] = gameActivity.findViewById( R.id.playerPoints0);
        points[1] = gameActivity.findViewById( R.id.playerPoints1);
        points[2] = gameActivity.findViewById( R.id.playerPoints2);

        playerNames[0] = gameActivity.findViewById( R.id.playerName0);
        playerNames[1] = gameActivity.findViewById( R.id.playerName1);
        playerNames[2] = gameActivity.findViewById( R.id.playerName2);

        playerNames[0].setText("");
        playerNames[1].setText("");
        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playerNames[2].setText( players[2].getLogin());
            }
        });

    }

    private void initializeImages()
    {
        cardsOnTable[ 0] = gameActivity.findViewById( R.id.cardTable0);
        cardsOnTable[ 1] = gameActivity.findViewById( R.id.cardTable1);
        cardsOnTable[ 2] = gameActivity.findViewById( R.id.cardTable2);

        cardsOfOpponents[ 0] = gameActivity.findViewById( R.id.opponentsCards0);
        cardsOfOpponents[ 1] = gameActivity.findViewById( R.id.opponentsCards1);

        trumpSuit = gameActivity.findViewById( R.id.trumpSuit);
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

        listOfButtons[ 0] = gameActivity.findViewById( R.id.cardButton0);
        listOfButtons[ 1] = gameActivity.findViewById( R.id.cardButton1);
        listOfButtons[ 2] = gameActivity.findViewById( R.id.cardButton2);
        listOfButtons[ 3] = gameActivity.findViewById( R.id.cardButton3);
        listOfButtons[ 4] = gameActivity.findViewById( R.id.cardButton4);
        listOfButtons[ 5] = gameActivity.findViewById( R.id.cardButton5);
        listOfButtons[ 6] = gameActivity.findViewById( R.id.cardButton6);
        listOfButtons[ 7] = gameActivity.findViewById( R.id.cardButton7);
        listOfButtons[ 8] = gameActivity.findViewById( R.id.cardButton8);
        listOfButtons[ 9] = gameActivity.findViewById( R.id.cardButton9);

        buttonSend = gameActivity.findViewById( R.id.chatButton);

        for (int j = 0; j < 10; j++)
            listOfButtons[ j].setEnabled(false);


    }
}
