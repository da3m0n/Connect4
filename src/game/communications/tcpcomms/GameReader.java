package game.communications.tcpcomms;

import game.MakeBoard;
import game.Grid;
import game.gameplayUtils.GameUtils;

import java.io.BufferedReader;
import java.io.IOException;

public class GameReader implements Runnable
{
    private final BufferedReader _incoming;
    private final MakeBoard _makeBoard;
    private Grid _grid;

    public GameReader(BufferedReader incoming, MakeBoard makeBoard, Grid grid)
    {
        this._incoming = incoming;
        this._makeBoard = makeBoard;
        _grid = grid;
    }

    public void run()
    {
        String messageIn;

        try
        {
            while(_grid.findWinner().hasNoWinner() && (messageIn = _incoming.readLine()) != null)
            {
                final String message = messageIn;
                GameUtils.invoke(new Runnable()
                {
                    public void run()
                    {
                        if(message.equals("Clear"))
                        {
                            _makeBoard.reset(0);
                        }
                        else
                        {
                            int position = Integer.parseInt(message);
                            _makeBoard.enableBoard(false);
                            _makeBoard.play(position);
                            _makeBoard.enableBoard(true);
                        }
                    }
                });
            }
           _makeBoard.enableBoard(false);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}