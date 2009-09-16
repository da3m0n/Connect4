package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonPanel extends JPanel
{
    private JButton _close = new JButton("Close");
    private JButton _startNewGame = new JButton("New Game");
    private JLabel _winner = new JLabel();
    private JLabel _playerTurn = new JLabel();

    public ButtonPanel(GridEntry gridEntry)
    {
        GridLayout _gl = new GridLayout(1, 5, 10, 10);
        setLayout(_gl);

        _startNewGame.setMnemonic('n');
        
        setPlayerText(gridEntry);

        _close.setMnemonic('c');
        _close.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        add(_winner, _gl);
        add(_playerTurn, _gl);
        add(_startNewGame, _gl);
        add(_close, _gl);
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

    public void setPlayerText(GridEntry playerTurn)
    {
        _playerTurn.setForeground(getPlayerColor(playerTurn));
        _playerTurn.setText(playerTurn.toString() + "'s Turn");
    }

    public void addReplayListener(ActionListener actionListener)
    {
        _startNewGame.addActionListener(actionListener);
    }

//    public void displayPlayerTurn(GridEntry playerTurn)
//    {
//        _playerTurn.setForeground(getPlayerColor(playerTurn));
//        _playerTurn.setText(playerTurn.toString() + "'s Turn");
//    }

    private Color getPlayerColor(GridEntry playerTurn)
    {
        if(playerTurn == GridEntry.Red)
        {
            return Color.red;
        }
        return Color.blue;
    }
}
