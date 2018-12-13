package com.example.hehe._1000game_client.Server;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hehe._1000game_client.Game.Tables;
import com.example.hehe._1000game_client.R;
import com.example.hehe._1000game_client.ServerStreams.ServerReader;
import com.example.hehe._1000game_client.ServerStreams.ServerWriter;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Login extends AppCompatActivity
{

    private EditText userName;
    private EditText userPasswd;
    private CardView loginButton;
    private TextView registerButton;
    public static ServerWriter serverWriter;
    public static ServerReader serverReader;
    private Socket socket;
    private WifiManager wifiManager;


    @SuppressLint("WifiManagerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_login);

        wifiManager = (WifiManager) this.getSystemService(WIFI_SERVICE);

        loginButton =  findViewById( R.id.loginView);
        registerButton =  findViewById( R.id.registerButton);

        userName =  findViewById( R.id.usernameEdit);
        userPasswd = findViewById( R.id.passwdEdit);

        registerButton.setOnClickListener( this.registerListener);
        loginButton.setOnClickListener( this.loginListener);
        userPasswd.setOnKeyListener( this.keyListener);

        getConnection();

        serverWriter.start();
        serverReader.start();
    }

    private CardView.OnClickListener loginListener = new CardView.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (checkWifi()) return;

            String username = userName.getText().toString();
            String password = userPasswd.getText().toString();

            if ( username.isEmpty() || password.isEmpty())
                return;


            serverWriter.sendMessage("LOGIN " + username + " " + password) ;
            String message = serverReader.getMessage();

            if (message.startsWith("LOGEDIN"))
            {
                userName.setError(null);

                switchActivity(Tables.class);

                Toast.makeText(getApplicationContext(), "Logged in as: " + username + " !", Toast.LENGTH_LONG).show();
            }else
            {

                Toast.makeText(getApplicationContext(), message.substring(2), Toast.LENGTH_LONG).show();
            }


        }
    };



    private boolean checkWifi()
    {
        if (wifiManager.getWifiState() == wifiManager.WIFI_STATE_DISABLED)
        {
            Toast.makeText(getApplicationContext(), "Turn on you Wifi " , Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private EditText.OnClickListener registerListener =  new EditText.OnClickListener()
    {
        public void onClick(View v)
        {
            switchActivity(Register.class);
        }
    };

    private void switchActivity(Class c)
    {
        Intent myIntent = new Intent(this, c);
        this.startActivity(myIntent);
    }

    private EditText.OnKeyListener keyListener = new EditText.OnKeyListener()
    {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event)
        {
            String username = userName.getText().toString();
            String password1 = userPasswd.getText().toString();

            if ( username.isEmpty()  )
                userName.setError("User name can't be empty!");
            else
                userName.setError(null);

            if (password1.isEmpty())
                userPasswd.setError("Password can't be empty!");
            else
                userPasswd.setError(null);

            return true;
        }
    };

    private void getConnection()
    {
        ServerConnection asyncTask = new ServerConnection();
        asyncTask.execute();

        try
        {
            socket = asyncTask.get(5, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        serverWriter = new ServerWriter( socket);
        serverReader = new ServerReader( socket);
    }
}

