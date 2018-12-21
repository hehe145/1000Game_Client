package com.example.hehe._1000game_client.Game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hehe._1000game_client.R;

import static com.example.hehe._1000game_client.Server.LoginActivity.serverReader;
import static com.example.hehe._1000game_client.Server.LoginActivity.serverWriter;

public class GameActivity extends AppCompatActivity
{

    private ImageView[] listOfButtons = new ImageView[10];
    private ImageView[] cardsOnTable = new ImageView[3];
    private ImageView[] cardsOponents = new ImageView[2];

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
        setListeners();

        Game game = new Game( listOfButtons, cardsOnTable, cardsOponents, getApplicationContext(), playerName);

        game.start();


    }

    private void initializeImages()
    {
        int i = 0;
        cardsOnTable[ i++] = findViewById( R.id.imageView);
        cardsOnTable[ i++] = findViewById( R.id.imageView16);
        cardsOnTable[ i] = findViewById( R.id.imageView17);
        i = 0;
        cardsOponents[ i++] = findViewById( R.id.imageView18);
        cardsOponents[ i] = findViewById( R.id.imageView19);

    }

    private void setListeners()
    {
        for (int i = 0; i < 10; i++)
        {
            final int a = i;
            listOfButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "aaa: " + a, Toast.LENGTH_SHORT ).show();
                }
            });
        }
    }

    private void initializeButtons()
    {
        int i = 0;
        listOfButtons[ i++] = findViewById( R.id.imageView0);
        listOfButtons[ i++] = findViewById( R.id.imageView1);
        listOfButtons[ i++] = findViewById( R.id.imageView2);
        listOfButtons[ i++] = findViewById( R.id.imageView3);
        listOfButtons[ i++] = findViewById( R.id.imageView4);
        listOfButtons[ i++] = findViewById( R.id.imageView5);
        listOfButtons[ i++] = findViewById( R.id.imageView6);
        listOfButtons[ i++] = findViewById( R.id.imageView7);
        listOfButtons[ i++] = findViewById( R.id.imageView8);
        listOfButtons[ i] = findViewById( R.id.imageView9);

        //TODO
//        for (int j = 0; j < 10; j++)
//            listOfButtons[ j].setEnabled(false);
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
