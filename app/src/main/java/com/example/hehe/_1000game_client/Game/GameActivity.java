package com.example.hehe._1000game_client.Game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hehe._1000game_client.R;

import static com.example.hehe._1000game_client.Server.LoginActivity.serverReader;
import static com.example.hehe._1000game_client.Server.LoginActivity.serverWriter;

public class GameActivity extends AppCompatActivity
{

    private ImageView[] listOfButtons = new ImageView[10];
    private ImageView[] cardsOnTable = new ImageView[3];
    private ImageView[] cardsOponents = new ImageView[2];
    private Button buttonSend;
    private EditText chatMessage;
    private TextView chatView;
    private TextView stakeView;
    private ImageView trumpSuit;
    private TextView[] playerNames;
    private TextView[] roundPolints;
    private TextView[] points;
    private Game game;

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

        game = new Game( listOfButtons, cardsOnTable, cardsOponents, getApplicationContext(), playerName,
                chatView,  playerNames, roundPolints, points, trumpSuit, stakeView);

        game.start();


    }

    private void initializeTextViews()
    {
        stakeView = findViewById( R.id.stake);
        chatMessage = findViewById( R.id.chatEdit);
        chatView = findViewById( R.id.chatView);

        roundPolints = new TextView[3];
        roundPolints[0] = findViewById( R.id.playerRoundPoints0);
        roundPolints[1] = findViewById( R.id.playerRoundPoints1);
        roundPolints[2] = findViewById( R.id.playerRoundPoints0);

        points = new TextView[3];
        points[0] = findViewById( R.id.playerPoints0);
        points[1] = findViewById( R.id.playerPoints1);
        points[2] = findViewById( R.id.playerPoints2);

        playerNames = new TextView[3];
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

        cardsOponents[ 0] = findViewById( R.id.opponentsCards0);
        cardsOponents[ 1] = findViewById( R.id.opponentsCards1);

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
                    game.throwCard(a);
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

    private ImageView.OnClickListener s = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            Toast.makeText( getApplicationContext(), "adadasdasd", Toast.LENGTH_LONG).show();
        }
    };

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
