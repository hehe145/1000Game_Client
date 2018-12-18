package com.example.hehe._1000game_client.Tables;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.hehe._1000game_client.R;

import java.util.List;


public class TableAdapter extends BaseAdapter
{
    private Context context;
    private List<Table> tables;

    public TableAdapter(Context context, List<Table> tables)
    {
        this.context = context;
        this.tables = tables;
    }

    @Override
    public int getCount() {
        return tables.size();
    }

    @Override
    public Object getItem(int position) {
        return tables.get( position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.table_list_item, null);

        TextView tableName = row.findViewById(R.id.tableName);
        TextView players = row.findViewById(R.id.tablePlayers);

        String text = tables.get(position).getPlayersNames();
        int numberOfPlayers = tables.get(position).getNumberOfPlayers();

        if ( numberOfPlayers == 3)
        {
            tableName.setText("Table name: " + tables.get(position).getTableName() + "\nTABLE IS FULL");
            players.setText( "Players:   " +  text);
        }
        else if (numberOfPlayers == 0)
        {
            tableName.setText("Table name: " + tables.get(position).getTableName() + "\nPLAYERS NEEDED: " + (3 - numberOfPlayers));
            players.setText( "Players:   No players playing.");
        }
        else
        {
            tableName.setText("Table name: " + tables.get(position).getTableName() + "\nPLAYERS NEEDED: " + (3 - numberOfPlayers));
            players.setText( "Players:   " +  text);
        }

        return row;
    }
}
