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
    private ClientsDisplayPanel _clientsDisplayPanel;
    private Game _game;
    private int _selectedColumn;

    private JPanel _buttonPanel = new JPanel();
    private JButton _close = new JButton("Close");
    private JButton _startNewGame = new JButton("New Game");
    private JLabel _winner = new JLabel();
    private JLabel _playerTurn = new JLabel();


    public MakeBoard(Game game)
    {
        _game = game;
        _color = getRandomPlayer();

        _clientsDisplayPanel = new ClientsDisplayPanel(_grid, _game, this);

        try
        {
            startServers();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        BorderLayout borderLayout = new BorderLayout(0, 10);
        setLayout(borderLayout);

        GridLayout gridLayout = new GridLayout(1, 4, 10, 10);
        _buttonPanel.setLayout(gridLayout);

        setPlayerText(_turn[_color]);

        _close.setMnemonic('c');
        _close.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        _startNewGame.setMnemonic('n');
        _startNewGame.setEnabled(false);
        _startNewGame.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                _color = getRandomPlayer();
                resetGame(_color);
            }
        });

        _buttonPanel.add(_winner, gridLayout);
        _buttonPanel.add(_playerTurn, gridLayout);
        _buttonPanel.add(_startNewGame, gridLayout);
        _buttonPanel.add(_close, gridLayout);

        _paintedGrid.setFocusable(true);
        enableBoard(true);

        add(_paintedGrid, BorderLayout.CENTER);
        add(_clientsDisplayPanel, BorderLayout.EAST);
        add(_buttonPanel, BorderLayout.SOUTH);
//        this.validate();
    }

    public void resetGame(int color)
    {
        _grid.clearGrid();
        resetInternal(color);
    }

    private void resetInternal(int color)
    {
        _color = color;
        _grid.clearCoords();
        repaint();
        setWinningText(_grid.findWinner().getWinner());
        setPlayerText(_turn[color]);
        removeFocus();
//        enableBoard(true);
    }

    private void setWinningText(GridEntry winner)
    {
        if(winner != GridEntry.empty)
        {
            _winner.setForeground(getPlayerColor(winner));
            _winner.setText(winner + " Wins!");
        }
        else
        {
            _winner.setText("");
        }
    }

    private void setPlayerText(GridEntry playerTurn)
    {
        _playerTurn.setForeground(getPlayerColor(playerTurn));
        _playerTurn.setText(playerTurn.toString() + "'s Turn");
    }

    private void removeFocus()
    {
        _startNewGame.setFocusable(false);
        _close.setFocusable(false);
    }

    private Color getPlayerColor(GridEntry playerTurn)
    {
        if(playerTurn == GridEntry.Red)
        {
            return Color.red;
        }
        return Color.blue;
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
            setPlayerText(_turn[_color]);
            repaint();

            if(_grid.findWinner().hasWinner())
            {
                setWinningText(_grid.findWinner().getWinner());
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
        _grid.setEnabled(enabled);
        _paintedGrid.removeMouseListener(_clickListener);
        _paintedGrid.removeKeyListener(_myKeyListener);
        if(enabled)
        {
            _paintedGrid.addKeyListener(_myKeyListener);
            _paintedGrid.addMouseListener(_clickListener);
        }
    }

    public int getSelectedColumn()
    {
        return _selectedColumn;
    }

    public void reset(int color)
    {
        _grid.reset();
        resetInternal(color);
    }

    private void setReplayEnabled(boolean isEnabled)
    {
        _startNewGame.setEnabled(isEnabled);
    }

    public void updatePlayerText(String player)
    {
        _clientsDisplayPanel.updatePlayerText(player);
    }

    private class MyKeyListener implements KeyListener
    {
        public void keyPressed(KeyEvent e)
        {
            setReplayEnabled(true);
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
            setReplayEnabled(true);
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
