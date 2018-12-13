package com.example.hehe._1000game_client.ServerStreams;


import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ServerWriter  extends Thread
{
    private OutputStream writer;

    public ServerWriter (Socket s)
    {
        try
        {
            writer = s.getOutputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message)
    {
        try
        {
            writer.write( (message + "\n").getBytes() );
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
