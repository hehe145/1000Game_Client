package com.example.hehe._1000game_client.Server;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.Socket;

public class ServerConnection extends AsyncTask<Void, Void, Socket>
{

    @Override
    protected Socket doInBackground(Void... voids)
    {
        Socket socket = null;
        try {
            socket = new Socket("192.168.1.100", 3456);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return socket;
    }
}
