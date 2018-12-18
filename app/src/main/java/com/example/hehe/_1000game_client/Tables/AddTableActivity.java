package com.example.hehe._1000game_client.Tables;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hehe._1000game_client.R;

import static com.example.hehe._1000game_client.Server.LoginActivity.serverReader;
import static com.example.hehe._1000game_client.Server.LoginActivity.serverWriter;

public class AddTableActivity extends AppCompatActivity {

    private EditText tableName;
    private Button btnCancel;
    private Button btnTableAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_table);

        tableName = findViewById( R.id.editTableName);
        btnCancel = findViewById( R.id.btnCancel);
        btnTableAdd = findViewById( R.id.btnAdd);

        btnTableAdd.setOnClickListener( this.AddTableListener);
        btnCancel.setOnClickListener( this.cancelListener);
    }

    private Button.OnClickListener AddTableListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            String name = tableName.getText().toString();

            if (name.isEmpty())
            {
                tableName.setError("Table name can't be empty!");
                return;
            }else
                tableName.setError(null);

            serverWriter.sendMessage("ADDTABLE " + name);

            String message = serverReader.getMessage();

            if (message.startsWith("M"))
            {
                Toast.makeText(getApplicationContext(), message.substring(2) , Toast.LENGTH_LONG).show();
                finish();
            }else
            {
                tableName.setError( message.substring(6));
            }

        }
    };
    private Button.OnClickListener cancelListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
