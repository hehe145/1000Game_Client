package com.example.hehe._1000game_client.ServerStreams;


import java.io.BufferedReader;
import java.io.IOException;
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
            reader = new BufferedReader( new InputStreamReader( s.getInputStream()));

        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void run()
    {
        while (true) {
            if ( ! readed)
            {
                try {

                    message = reader.readLine();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                readed = true;
            }


        }
    }

    public String getMessage()
    {
        while (true)
        {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (readed)
            {
                readed = false;
                return message;
            }
        }
    }
}
