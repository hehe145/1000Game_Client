package com.example.hehe._1000game_client.Server;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hehe._1000game_client.R;

import static com.example.hehe._1000game_client.Server.LoginActivity.serverReader;
import static com.example.hehe._1000game_client.Server.LoginActivity.serverWriter;

public class RegisterActivity extends AppCompatActivity {

    private EditText userName;
    private EditText userPasswd1;
    private EditText userPasswd2;
    private Button buttonBack;
    private CardView registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton =  findViewById( R.id.registerView);
        buttonBack = findViewById( R.id.btn_back);

        userName =  findViewById( R.id.usernameREdit);
        userPasswd1 = findViewById( R.id.passwdREdit);
        userPasswd2 = findViewById( R.id.passwdREdit1);

        userPasswd2.setOnKeyListener( keyListener);
        registerButton.setOnClickListener( this.registerListener);
        buttonBack.setOnClickListener( this.backListener);

    }

    private EditText.OnKeyListener keyListener = new EditText.OnKeyListener()
    {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event)
        {
            String username = userName.getText().toString();
            String password1 = userPasswd1.getText().toString();
            String password2 = userPasswd2.getText().toString();

            if ( username.isEmpty() )
                userName.setError("User name can't be empty!");
            else
                userName.setError(null);

            if ( password1.isEmpty() )
                userPasswd1.setError("Password can't be empty!");
            else
                userPasswd1.setError(null);

            if ( ! password1.equals(password2) )
                userPasswd2.setError("Passwords are not equal!");
            else
                userPasswd2.setError(null);

            return true;
        }
    };

    private CardView.OnClickListener registerListener = new CardView.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            String username = userName.getText().toString();
            String password1 = userPasswd1.getText().toString();
            String password2 = userPasswd2.getText().toString();

            if ( username.isEmpty() || password1.isEmpty() || ! password1.equals(password2) || username.contains(" "))
                return;

            serverWriter.sendMessage("REGISTER " + username + " " + password1);
            String message = serverReader.getMessage();

            if (message.startsWith("REGISTRED"))
            {
                userName.setError(null);

                finish();

                Toast.makeText(getApplicationContext(), "Registred: " + username + " !", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(), message.substring(2), Toast.LENGTH_LONG).show();
                userName.setError("User name is taken");
            }
        }
    };


    private EditText.OnClickListener backListener =  new EditText.OnClickListener()
    {
        public void onClick(View v)
        {
            finish();
        }
    };


}

