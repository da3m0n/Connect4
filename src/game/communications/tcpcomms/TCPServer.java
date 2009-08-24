package game.communications.tcpcomms;

import game.Game;
import game.Grid;
import game.MakeBoard;
import game.NotificationDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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
        Grid _grid = grid;
        _game = game;
        _makeBoard = makeBoard;
    }


//   public TCPServer()
//    {
//        try
//        {
//            _listener = new ServerSocket(0); // listens for a connection request
//        }
//        catch(IOException e)
//        {
//            e.printStackTrace();
//        }
//        _port = _listener.getLocalPort();
//    }

    boolean run = true;

    public void run()
    {
        // need to get access to the gui from both the server and client

        while(_running)
        {
            try
            {
               String messageOut; // Message to be sent to the client
               String messageIn;  // Message received from the client

               TCPProtocol tcpProtocol = new TCPProtocol();
               messageOut = tcpProtocol.processInput(String.valueOf(_listener.getLocalPort()));

//                System.out.println("server> waiting for connection on " + _listener.getLocalPort() + "...");
               System.out.println(messageOut);
               Socket _connection = _listener.accept(); // For communication with the client
               messageOut = tcpProtocol.processInput(_connection.getInetAddress().getHostName());
               System.out.println(messageOut);
//                System.out.println("server> connection received from: " + _connection.getInetAddress().getHostName());
                _listener.close();

                _outgoing = new PrintWriter(_connection.getOutputStream(), true);
                BufferedReader _incoming = new BufferedReader(new InputStreamReader(_connection.getInputStream()));

                String text = _incoming.readLine() + " wants to play a game with you. Wanna play?" ;
                final NotificationDialog notificationDialog = new NotificationDialog(_game.getFrame(), null, text);
                notificationDialog.addMyListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
//                        sendMessage("Connection successful!");
                       System.out.println("Connection successful");
                       notificationDialog.setVisible(false);
                    }
                });
               notificationDialog.setVisible(true);

                while((messageIn = _incoming.readLine()) != null)
                {
                   System.out.println("Message In: " + messageIn);
//                    messageOut = tcpProtocol.processInput(messageIn);
//                    sendMessage(messageOut);
//                    System.out.println("MessageOut: " + messageOut);
//                    System.out.println("Message received: " + messageIn);
//                    sendMessage(messageIn);
                }
                _outgoing.close();
                _incoming.close();

            }
            catch(IOException e)
            {
                _running = false;
                System.err.println("the other guy closed...");
//                e.printStackTrace();
            }
        }
    }

    private void sendMessage(String msg)
    {
        _outgoing.println(msg);
        _outgoing.flush();
        System.out.println("server> " + msg);
    }

    public int getPort()
    {
        return _port;
    }

}
