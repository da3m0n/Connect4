package game.communications.tcpcomms;

import game.Client;
import game.Grid;
import game.MakeBoard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.UnknownHostException;

public class TCPClient extends Thread
{
    private ServerSocket _listener; // listens for a connection request
    private Client client;
    private Grid grid;
   private MakeBoard _makeBoard;
   private PrintWriter _outgoing;
    private BufferedReader _incoming;

    public TCPClient(Client client, Grid grid, MakeBoard makeBoard)
    {
        this.client = client;
        this.grid = grid;
       _makeBoard = makeBoard;
    }

    public void run()
    {
        Socket connection = null; // For communication with the client
        try
        {
            connection = new Socket(client.getIPAddress(), client.getPort());
            _incoming = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            _outgoing = new PrintWriter(connection.getOutputStream());

            // send data over socket
            sendMessage(String.valueOf(TCPServer.getInstance().getPort()));

            String fromServer;

            while((fromServer = _incoming.readLine()) != null)
            {
                System.out.println("fromServer = " + fromServer);
                sendMessage("Waiting for repsonse");
//                    System.out.println("Waiting for other player to move");
//                    sendMessage("move");
//                    grid.play(GridEntry.Blue, 0);
               try
               {
//                  System.out.println("move " + grid.getNextMove());
//                   sendMessage("" + grid.getNextMove());
                  sendMessage(String.valueOf(grid.getNextMove()));
               }
               catch (InterruptedException e)
               {
                  e.printStackTrace();
               }
            }
        }
        catch(UnknownHostException e)
        {
            System.err.println("Don't know about host: localhost.");
            e.printStackTrace();
        }
        catch(IOException e)
        {
            System.err.println("so and so doesn't want to play");
            e.printStackTrace();
        }
        finally
        {
            _outgoing.close();
            try
            {
                _incoming.close();
                assert connection != null;
                connection.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

    }

   public void sendMessage(String msg)
    {
        _outgoing.println(msg);
        _outgoing.flush();
        System.out.println("client> " + msg);
    }
}
