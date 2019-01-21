package com.example.hehe._1000game_client.Game;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hehe._1000game_client.R;

import static com.example.hehe._1000game_client.Server.LoginActivity.serverWriter;

public class Bidder extends DialogFragment
{
    private TextView textStake;
    private int stake;
    private String text;
    private String firstBtn;
    private String secondBtn;
    private boolean bidding;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        stake = args.getInt("stake");
        text = args.getString("name");
        firstBtn = args.getString("firstBtn");
        secondBtn = args.getString("secondBtn");
        bidding = (args.getInt("bool") != 0);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_bidder, null))
                .setMessage(text)
                .setPositiveButton( firstBtn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        if ( bidding)
                            serverWriter.sendMessage("FOLD");
                        else
                            serverWriter.sendMessage("NO");
                    }
                })
                .setNegativeButton(secondBtn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        serverWriter.sendMessage("BID " + textStake.getText().toString());
                    }
                });



        return builder.create();
    }


    @Override
    public void onStart() {
        super.onStart();

        Button btnAdd = getDialog().findViewById( R.id.btnAdd);
        Button btnSub = getDialog().findViewById( R.id.btnSub);
        textStake = getDialog().findViewById( R.id.dialogTextStake);

        textStake.setText( Integer.toString(stake));

        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int value = Integer.parseInt( textStake.getText().toString());
                if ( value < 320)
                    textStake.setText( Integer.toString(value + 10));

            }
        });
        btnSub.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int value = Integer.parseInt( textStake.getText().toString());
                if ( value > (stake) )
                    textStake.setText( Integer.toString(value - 10));
            }
        });

    }

}
