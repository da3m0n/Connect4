package game.communications.tcpcomms;

import game.*;
import game.gameplayUtils.GameUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread
{
    private PrintWriter _outgoing;
    private static int _port;
    private ServerSocket _listener;
    private static TCPServer _instance = null;
    private Game _game;
    private MakeBoard _makeBoard;
    private boolean _running = true;
    private Grid _grid;
    private boolean _acceptGame;

    private TCPServer()
    {
        createServerSocket();
    }

    public static TCPServer getInstance()
    {
        if(_instance == null)
        {
            _instance = new TCPServer();
        }
        return _instance;
    }

    private void createServerSocket()
    {
        try
        {
            _listener = new ServerSocket(0); // listens for a connection request
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        _port = _listener.getLocalPort();
//        System.out.println("_port = " + _port);
    }

    public void init(Grid grid, Game game, MakeBoard makeBoard)
    {
        _grid = grid;
        _game = game;
        _makeBoard = makeBoard;
    }

    boolean run = true;

    public void run()
    {
        while(_running)
        {
            try
            {
                String messageOut; // Message to be sent to the client
                String messageIn;  // Message received from the client

                TCPProtocol tcpProtocol = new TCPProtocol();
                messageOut = tcpProtocol.processInput(String.valueOf(_listener.getLocalPort()));

                System.out.println(messageOut);
                Socket _connection = _listener.accept(); // For communication with the client

                _outgoing = new PrintWriter(_connection.getOutputStream(), true);
                final BufferedReader incoming = new BufferedReader(new InputStreamReader(_connection.getInputStream()));

                String text = incoming.readLine() + " wants to play a game with you. Wanna play?";
                final String player = incoming.readLine();
                
                final NotificationDialog notificationDialog = new NotificationDialog(_game.getFrame(), text);

                notificationDialog.addYesButtonListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        sendMessage(Dictionary.ACCEPT_GAME_INVITE);
                        _acceptGame = true;
                        notificationDialog.setVisible(false);
                        _makeBoard.reset(Integer.parseInt(player));
                    }
                });

                notificationDialog.addNoButtonListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e)
                    {
                        sendMessage(Dictionary.DECLINE_GAME_INVITE);
                        _acceptGame = false;
                        notificationDialog.setVisible(false);
                    }
                });

                GameUtils.invoke(new Runnable()
                {
                    public void run()
                    {
                        notificationDialog.setVisible(true);
                    }
                });

                if(_acceptGame)
                {
                    _makeBoard.enableBoard(false);
                    GameUtils.gameLoop(incoming, _outgoing, _makeBoard, _grid);
                }
                _outgoing.close();
                incoming.close();
            }
            catch(IOException e)
            {
                System.err.println("the other guy closed...");
                e.printStackTrace();
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(String messageOut)
    {
        GameUtils.sendMessage(_outgoing, messageOut);
    }

    public int getPort()
    {
        return _port;
    }

}
