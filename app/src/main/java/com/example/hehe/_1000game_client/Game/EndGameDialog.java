package com.example.hehe._1000game_client.Game;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class EndGameDialog extends DialogFragment
{
    private String login;
    public EndGameDialog()
    {
        super();
    }
    @SuppressLint("ValidFragment")
    public EndGameDialog(String login)
    {
        super();
        this.login = login;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Game Over");
        builder.setCancelable(false);
        builder.setMessage("Player " + login + " wins the game!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        return builder.create();
    }
}