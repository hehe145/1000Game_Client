package com.example.hehe._1000game_client.Game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.hehe._1000game_client.R;

import static com.example.hehe._1000game_client.Server.LoginActivity.serverReader;
import static com.example.hehe._1000game_client.Server.LoginActivity.serverWriter;

public class GameActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        String message = serverReader.getMessage();

        Toast.makeText( getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
                serverWriter.sendMessage("YES");
                Toast.makeText( getApplicationContext(), serverReader.getMessage(), Toast.LENGTH_LONG).show();
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
