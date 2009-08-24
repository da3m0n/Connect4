package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

public class ButtonPanel extends JPanel
{
    private JButton _close = new JButton("Close");
    private JButton _startNewGame = new JButton("New Game");
    private JLabel _winner = new JLabel();
    private JLabel _playerTurn = new JLabel();
    private GridLayout _gl = new GridLayout(1, 5, 10, 10);
    private JButton _url = new JButton("URL");
    private Grid _grid = new Grid();

    public ButtonPanel(GridEntry gridEntry, Grid grid)
    {
//      setLayout(new GridLayout(1, 4, 5, 5));
        setLayout(_gl);

        _startNewGame.setMnemonic('n');
        _playerTurn.setForeground(getPlayerColor(gridEntry));
        _playerTurn.setText(gridEntry.toString() + "'s Turn");

        _close.setMnemonic('c');
        _close.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        _url.setMnemonic('u');
        _url.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    System.out.println("Host: " + InetAddress.getLocalHost());
                }
                catch(Exception e1)
                {
                    e1.printStackTrace();
                }
            }
        });


        add(_winner, _gl);
        add(_playerTurn, _gl);
        add(_url, _gl);
        add(_startNewGame, _gl);
        add(_close, _gl);
    }

    public void setURLText(String urlText)
    {
        _url.setText(urlText);
    }

    public void setReplayEnabled(boolean isEnabled)
    {
        _startNewGame.setEnabled(isEnabled);
    }

    public void removeFocus()
    {
        _startNewGame.setFocusable(false);
        _close.setFocusable(false);
    }

    public void setWinningText(GridEntry winner)
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

    public void addReplayListener(ActionListener actionListener)
    {
        _startNewGame.addActionListener(actionListener);
    }

    public void displayPlayerTurn(GridEntry playerTurn)
    {
        _playerTurn.setForeground(getPlayerColor(playerTurn));
        _playerTurn.setText(playerTurn.toString() + "'s Turn");
        for(int row = 0; row < 6; row++)
        {
            for(int col = 0; col < 7; col++)
            {
                System.out.println("ButtonPanel.displayPlayerTurn: " + _grid.getPeice(row, col));
            }

        }

    }

    private Color getPlayerColor(GridEntry playerTurn)
    {
        if(playerTurn == GridEntry.Red)
        {
            return Color.red;
        }
        return Color.blue;
    }
}
