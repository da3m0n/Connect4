package game;

import static game.GridEntry.Blue;
import static game.GridEntry.Red;
import game.communications.multicast.Connect4MultiCastServerThread;
import game.communications.multicast.Connect4MulticastClient;
import game.communications.tcpcomms.TCPServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class MakeBoard extends JPanel
{
    private Grid _grid = new Grid();
    private PaintedGrid _paintedGrid = new PaintedGrid(_grid);
    private GridEntry[] _turn = {Red, Blue};
    private int _color;
    private ClickListener _clickListener = new ClickListener();
    private MyKeyListener _myKeyListener = new MyKeyListener();
    private ButtonPanel _buttonPanel;
    private ClientsDisplayPanel _clientsDisplayPanel;
    private Game _game;
    private int _selectedColumn;

    public MakeBoard(Game game)
    {
        _game = game;
        _color = getRandomPlayer();

        _buttonPanel = new ButtonPanel(_turn[_color]);
        _clientsDisplayPanel = new ClientsDisplayPanel(_grid, _game, this);

        BorderLayout borderLayout = new BorderLayout(0, 10);
        setLayout(borderLayout);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Connect 4"));

        try
        {
            startServers();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        _buttonPanel.setReplayEnabled(false);
        _buttonPanel.addReplayListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                _color = getRandomPlayer();
                resetGame(_color);
            }
        });

        _paintedGrid.setFocusable(true);
        enableBoard(true);

        add(_paintedGrid, BorderLayout.CENTER);
        add(_clientsDisplayPanel, BorderLayout.EAST);
        add(_buttonPanel, BorderLayout.SOUTH);
        this.validate();
    }

    public void resetGame(int color)
    {
        _color = color;
        _grid.clearGrid();
        _grid.clearCoords();
        repaint();
        _buttonPanel.setWinningText(_grid.findWinner().getWinner());
        _buttonPanel.resetPlayerText(_turn[color]);
        _buttonPanel.removeFocus();
    }

    private void startServers() throws IOException
    {
        TCPServer tcpServer = TCPServer.getInstance();
        tcpServer.init(_grid, _game, this);
        tcpServer.start();

        Connect4GameServer connect4Server = new Connect4GameServer();
        new Connect4MultiCastServerThread(connect4Server.getPort()).start();
        Connect4MulticastClient _multicastClient = new Connect4MulticastClient(_clientsDisplayPanel);
        _multicastClient.start();
    }


    public void play(int column)
    {
        if(_grid.play(_turn[_color], column))
        {
            _color = (_color + 1) % 2;
            _buttonPanel.displayPlayerTurn(_turn[_color]);
            repaint();

            if(_grid.findWinner().hasWinner())
            {
                _buttonPanel.setWinningText(_grid.findWinner().getWinner());
                enableBoard(false);
            }
        }
    }

    public int getRandomPlayer()
    {
        return (int) (Math.random() * 2);
    }

    public void enableBoard(boolean enabled)
    {
        if(enabled)
        {
            _paintedGrid.addKeyListener(_myKeyListener);
            _paintedGrid.addMouseListener(_clickListener);
        }
        else
        {
            _paintedGrid.removeMouseListener(_clickListener);
            _paintedGrid.removeKeyListener(_myKeyListener);
        }
    }

    public void setURLText(String text)
    {
        _buttonPanel.setURLText(text);
    }

    public int getSelectedColumn()
    {
        return _selectedColumn;
    }

    private class MyKeyListener implements KeyListener
    {
        public void keyPressed(KeyEvent e)
        {
            _buttonPanel.setReplayEnabled(true);
            int col = e.getKeyCode() - 97;
            if(col >= 0 && col < 7)
            {
                play(col);
            }
        }

        public void keyTyped(KeyEvent e)
        {
        }

        public void keyReleased(KeyEvent e)
        {
        }
    }

    private class ClickListener implements MouseListener
    {

        public void mouseClicked(MouseEvent e)
        {
            _buttonPanel.setReplayEnabled(true);
            _selectedColumn = _paintedGrid.giveMeTheColumn(e.getX());
            play(_selectedColumn);
        }

        public void mousePressed(MouseEvent e)
        {
        }

        public void mouseReleased(MouseEvent e)
        {
        }

        public void mouseEntered(MouseEvent e)
        {
        }

        public void mouseExited(MouseEvent e)
        {
        }
    }

}
