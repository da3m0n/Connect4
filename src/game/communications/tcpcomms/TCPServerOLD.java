package game.communications.tcpcomms;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerOLD
{
    public TCPServerOLD() throws IOException
    {
        ServerSocket serverSocket = null;
        Socket client = null;
        boolean listening = true;

        try
        {
            serverSocket = new ServerSocket(4446);
            client = serverSocket.accept();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        while(listening)
        {
            new TCPServerThread(serverSocket.accept()).start();
            System.out.println("server socket started...");
        }

        serverSocket.close();
    }

}
