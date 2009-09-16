package game.communications.tcpcomms;

import game.MakeBoard;
import game.gameplayUtils.GameUtils;

import java.io.BufferedReader;
import java.io.IOException;

public class GameReader implements Runnable
{
    private final BufferedReader _incoming;
    private final MakeBoard _makeBoard;

    public GameReader(BufferedReader incoming, MakeBoard makeBoard)
    {
        this._incoming = incoming;
        this._makeBoard = makeBoard;
    }

    public void run()
    {
        String messageIn;

        System.out.println("waiting for opponent");
        try
        {
            while((messageIn = _incoming.readLine()) != null)
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
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}