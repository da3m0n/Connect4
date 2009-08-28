package game.communications.tcpcomms;

import game.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.reflect.InvocationTargetException;

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

//               System.out.println(messageOut);

                _outgoing = new PrintWriter(_connection.getOutputStream(), true);
                final BufferedReader incoming = new BufferedReader(new InputStreamReader(_connection.getInputStream()));

                String text = incoming.readLine() + " wants to play a game with you. Wanna play?";

                final NotificationDialog notificationDialog = new NotificationDialog(_game.getFrame(), text);

                notificationDialog.addMyListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        sendMessage("Accepted invitation");
                        notificationDialog.setVisible(false);
                    }
                });
                invoke(new Runnable() {

                        public void run()
                        {
                            notificationDialog.setVisible(true);
                        }
                    });

                gameLoop(incoming, _outgoing, _makeBoard, _grid);
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
        sendMessage(_outgoing, messageOut);
    }

    public static void gameLoop(BufferedReader incoming, PrintWriter outgoing, final MakeBoard _makeBoard, final Grid _grid)
            throws IOException, InterruptedException
    {
        String messageIn;
        System.out.println("waiting for opponent");
        while((messageIn = incoming.readLine()) != null)
        {
            System.out.println("messageIn = " + messageIn);
            final int position = Integer.parseInt(messageIn);

            invoke(new Runnable() {

                        public void run()
                        {
                            _makeBoard.play(position);
                        }
                    });
            System.out.println("waiting for our move");
            sendMessage(outgoing, String.valueOf(_grid.getNextMove()));
//                    messageOut = tcpProtocol.processInput(messageIn);
//                    sendMessage(messageOut);
            System.out.println("waiting for opponent");
        }
    }

    private static void invoke(Runnable r) {
        try
        {
            SwingUtilities.invokeAndWait(r);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        catch(InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }
    public static void sendMessage(PrintWriter _outgoing, String msg)
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
