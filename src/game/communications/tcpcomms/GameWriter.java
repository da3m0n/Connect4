package game.communications.tcpcomms;

import game.Grid;
import game.MakeBoard;
import game.gameplayUtils.GameUtils;

import java.io.PrintWriter;

public class GameWriter implements Runnable
{
    private PrintWriter _outgoing;
    private Grid _grid;
    private MakeBoard makeBoard;


    public GameWriter(PrintWriter _outgoing, Grid _grid, MakeBoard makeBoard)
    {
        this._outgoing = _outgoing;
        this._grid = _grid;
        this.makeBoard = makeBoard;
    }

    public void run()
    {
        try
        {
            while(true)
            {
                String move = _grid.getNextMove();
                GameUtils.sendMessage(_outgoing, move);
                makeBoard.enableBoard(false);
            }
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }

    }
}
