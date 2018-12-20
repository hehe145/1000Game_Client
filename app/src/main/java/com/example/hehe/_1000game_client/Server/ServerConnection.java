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
        for (int i = 0; i < 3; i++)
        {
            try
            {
                socket = new Socket("192.168.1.101", 3456);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if ( socket == null)
            {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else
                break;
        }


        return socket;
    }
}
