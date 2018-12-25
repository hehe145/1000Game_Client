package com.example.hehe._1000game_client.ServerStreams;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerReader extends Thread
{
    private BufferedReader reader;
    private String message;

    public ServerReader(Socket s)
    {
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
        while ( ! interrupted())
        {

        }
    }

    public String getMessage()
    {
        while ( ! interrupted())
        {

            try {
                message = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if ( ! message.isEmpty())
                break;
        }
        return message;
    }
}
