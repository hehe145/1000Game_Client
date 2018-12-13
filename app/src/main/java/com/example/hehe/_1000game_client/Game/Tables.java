package com.example.hehe._1000game_client.Game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.hehe._1000game_client.R;
import static com.example.hehe._1000game_client.Server.Login.serverReader;
import static com.example.hehe._1000game_client.Server.Login.serverWriter;

public class Tables extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);

        serverWriter.sendMessage("SHOWTABLES");

        String message = serverReader.getMessage();

        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
    }
}
