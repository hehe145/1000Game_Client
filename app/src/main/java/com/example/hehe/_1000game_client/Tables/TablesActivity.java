package com.example.hehe._1000game_client.Tables;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hehe._1000game_client.Game.GameActivity;
import com.example.hehe._1000game_client.R;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import android.widget.AdapterView.OnItemClickListener;
import static com.example.hehe._1000game_client.Server.LoginActivity.serverReader;
import static com.example.hehe._1000game_client.Server.LoginActivity.serverWriter;

public class TablesActivity extends AppCompatActivity
{
    private TextView textViewTables;
    private ListView tableListView;
    private String playerName;
    private Button btnAddTable;
    private Button btnRefresh;
    private TableAdapter tableList;
    private List<Table> tables;
    private DialogInterface.OnClickListener dialogClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);

        Intent i=getIntent();
        playerName = i.getStringExtra("playerName");

        tableListView = findViewById( R.id.tableListView);
        btnAddTable = findViewById( R.id.tableAdd);
        btnRefresh = findViewById( R.id.btnRefresh);
        textViewTables = findViewById( R.id.textViewTable);

        btnAddTable.setOnClickListener( this.addTableListener);
        btnRefresh.setOnClickListener( this.refreshListListener);

        serverWriter.sendMessage("SHOWTABLES");

        String message = serverReader.getMessage();

        tableListView.setOnItemClickListener( this.listItemListener);

        refreshList( message.substring(6));
    }

    /**
     * Refreshes list of tables in the Activity based on messege given from server
     * @param message
     */
    private void refreshList(String message)
    {
        StringTokenizer  token = new StringTokenizer( message, " ");
        int tableCount =0;
        try
        {
            tableCount = Integer.parseInt( token.nextToken());
        }
        catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        tables = new ArrayList<>();

        createListOfTables(token, tableCount);

        tableList = new TableAdapter(this, tables);
        tableList.notifyDataSetChanged();
        tableListView.setAdapter( tableList);
    }

    /**
     *  Create list of tables
     * @param token
     * @param tableCount
     */
    private void createListOfTables(StringTokenizer token, int tableCount)
    {
        if (tableCount == 0)
            textViewTables.setText("No tables avalible. Please add new table.");
        else
            textViewTables.setText("Click on table you want to join or add new table.");

        for (int i = 0; i < tableCount; i++)
        {
            String tableName = token.nextToken();
            int playerCount = Integer.parseInt( token.nextToken());
            StringBuilder players = new StringBuilder();

            for (int j = 0; j < playerCount; j++)
            {
                players.append(token.nextToken() + "   ");
            }

            tables.add( new Table(tableName, players.toString(), playerCount));
        }
    }

    /**
     * Asks if user wants to logout and go back to LoinActivity
     */
    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder( this);
        builder.setMessage( "Do you want to log out?" ).setNegativeButton("No", logOutListener)
                .setPositiveButton("Yes", logOutListener).setCancelable(false).show();

    }

    /**
     * Logs user out and returns him do LoginActivity
     */
    private DialogInterface.OnClickListener logOutListener = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                {
                    serverWriter.sendMessage("LOGOUT");
                    Toast.makeText( getApplicationContext(), serverReader.getMessage().substring(2), Toast.LENGTH_LONG).show();
                    serverReader.interrupt();
                    serverWriter.interrupt();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", true);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                    break;
                }

                case DialogInterface.BUTTON_NEGATIVE:
                {
                    break;
                }

            }
        }
    };


    private OnItemClickListener listItemListener = new OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> parent, View v,final int pos,   long id)
        {
            dialogClickListener = new DialogInterface.OnClickListener()
            {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:

                            joinTable(pos);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:

                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder( tableListView.getContext());
            builder.setMessage("Do you want to join table \"" + tables.get(pos).getTableName() + "\" ?" ).setNegativeButton("No", dialogClickListener)
                    .setPositiveButton("Yes", dialogClickListener).show();

        }
    };

    /**
     * Joins table with given position number in list of tables
     * @param pos
     */
    private void joinTable(int pos)
    {
        serverWriter.sendMessage("JOIN " + tables.get(pos).getTableName());
        String message = serverReader.getMessage();

        if (message.startsWith("JOINED"))
        {
            Toast.makeText( getApplicationContext(), "Joined table: " + tables.get(pos).getTableName() , Toast.LENGTH_LONG).show();
            switchActivity(GameActivity.class);
        }else
        {
            Toast.makeText( getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Ask server for information about available tables
     */
    private Button.OnClickListener refreshListListener = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            serverWriter.sendMessage("SHOWTABLES");

            String message = serverReader.getMessage();
            refreshList(  message.substring(7));
        }
    };

    /**
     * Goes to AddTableActivity
     */
    private Button.OnClickListener addTableListener = new Button.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switchActivity( AddTableActivity.class);
        }
    };

    /**
     * Goes to given Activity
     * @param c
     */
    private void switchActivity(Class c)
    {
        Intent myIntent = new Intent(this, c);
        myIntent.putExtra("playerName", playerName);
        this.startActivity(myIntent);
    }


}
