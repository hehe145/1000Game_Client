package com.example.hehe._1000game_client.Server;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hehe._1000game_client.Tables.TablesActivity;
import com.example.hehe._1000game_client.R;
import com.example.hehe._1000game_client.ServerStreams.ServerReader;
import com.example.hehe._1000game_client.ServerStreams.ServerWriter;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LoginActivity extends AppCompatActivity
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

        if ( wifiManager.getWifiState() == wifiManager.WIFI_STATE_DISABLED)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Wifi is turned off.").setNegativeButton("Exit", dialogClickListener)
                    .setPositiveButton("Turn on Wifi", dialogClickListener).setCancelable(false).show();
        }
        else
            getConnection();

    }



    private CardView.OnClickListener loginListener = new CardView.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if ( wifiManager.getWifiState() == wifiManager.WIFI_STATE_DISABLED ) return;

            String username = userName.getText().toString();
            String password = userPasswd.getText().toString();

            if ( username.isEmpty() || password.isEmpty())
                return;


            serverWriter.sendMessage("LOGIN " + username + " " + password) ;
            String message = serverReader.getMessage();

            if (message.startsWith("LOGEDIN"))
            {
                userName.setError(null);

                switchActivity(TablesActivity.class);

                Toast.makeText(getApplicationContext(), "Logged in as: " + username + " !", Toast.LENGTH_LONG).show();
            }else
            {

                Toast.makeText(getApplicationContext(), message.substring(2), Toast.LENGTH_LONG).show();
            }


        }
    };

    private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
    {

        @Override
        public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:

                wifiManager.setWifiEnabled( true);
                getConnection();
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                finish();
                break;
        }
    }
    };

    private DialogInterface.OnClickListener connectionErrorListener = new DialogInterface.OnClickListener()
    {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:

                    getConnection();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    finish();
                    break;
            }
        }
    };

    private EditText.OnClickListener registerListener =  new EditText.OnClickListener()
    {
        public void onClick(View v)
        {
            switchActivity(RegisterActivity.class);
        }
    };

    private void switchActivity(Class c)
    {
        Intent myIntent = new Intent(this, c);
        startActivityForResult(myIntent, 1);
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
        socket = null;
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

        if ( socket == null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Could't connect to server! Try again?").setNegativeButton("No. Exit", connectionErrorListener)
                    .setPositiveButton("Yes", connectionErrorListener).setCancelable(false).show();
        }else
        {
            Toast.makeText( getApplicationContext(), "Connected to server", Toast.LENGTH_LONG).show();
            serverWriter = new ServerWriter( socket);
            serverReader = new ServerReader( socket);

            serverWriter.start();
            serverReader.start();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1)
        {
            getConnection();
        }
    }
}

