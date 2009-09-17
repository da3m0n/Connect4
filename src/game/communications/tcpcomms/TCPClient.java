package game.communications.tcpcomms;

import game.*;
import game.gameplayUtils.GameUtils;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.UnknownHostException;

public class TCPClient extends Thread
{
    private Client client;
    private Grid grid;
    private MakeBoard _makeBoard;
    private Game _game;
    private PrintWriter _outgoing;

    public TCPClient(Client client, Grid grid, MakeBoard makeBoard, Game game)
    {
        this.client = client;
        this.grid = grid;
        _makeBoard = makeBoard;
        _game = game;
    }

    public void run()
    {
        Socket connection = null; // For communication with the client
        BufferedReader incoming = null;

        try
        {
            connection = new Socket(client.getIPAddress(), client.getPort());
            incoming = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            _outgoing = new PrintWriter(connection.getOutputStream());

            // send data over socket
            sendMessage(String.valueOf(TCPServer.getInstance().getPort()));

            int selectPlayerColor = _makeBoard.getRandomPlayer();
            sendMessage(String.valueOf(selectPlayerColor));
            
            String fromServer;
            fromServer = incoming.readLine();
            if(fromServer != null)
            {
                if(fromServer.equals(Dictionary.ACCEPT_GAME_INVITE))
                {
                    _makeBoard.reset(selectPlayerColor);
                    _makeBoard.enableBoard(true);
                    GameUtils.gameLoop(incoming, _outgoing, _makeBoard, grid);
                }
                else
                {
                    JOptionPane.showMessageDialog(_game.getFrame(), "Opponent doesn't want to play.");
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
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        _outgoing.close();
        try
        {
            incoming.close();
            connection.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    }

    public void sendMessage(String msg)
    {
        GameUtils.sendMessage(_outgoing, msg);
    }
}
