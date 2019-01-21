package com.example.hehe._1000game_client.ServerStreams;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerReader extends Thread
{
    private BufferedReader reader;
    private String message;
    private boolean readed;

    public ServerReader(Socket s)
    {
        readed = false;
        try
        {
            reader = new BufferedReader(  new InputStreamReader( s.getInputStream()));

        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void run()
    {
        while ( ! isInterrupted()) {
            if ( ! readed)
            {
                try
                {
                    message = reader.readLine();
                    readed = true;

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        }
    }

    public String getMessage()
    {
        while ( ! isInterrupted())
        {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (readed)
            {
                readed = false;
                Log.e("serverStream", "----" + message);
                break;
            }
        }
        return message;
    }
}
