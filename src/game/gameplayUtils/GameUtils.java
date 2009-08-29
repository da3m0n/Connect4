package game.gameplayUtils;

import game.MakeBoard;
import game.Grid;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class GameUtils
{
    public static void gameLoop(BufferedReader incoming, PrintWriter outgoing, final MakeBoard makeBoard, final Grid grid)
            throws IOException, InterruptedException
    {
        String messageIn;
        System.out.println("waiting for opponent");
        makeBoard.enableBoard(false);
        while((messageIn = incoming.readLine()) != null)
        {
            System.out.println("messageIn = " + messageIn);
            final int position = Integer.parseInt(messageIn);

            invoke(new Runnable()
            {
                public void run()
                {
                    makeBoard.play(position);
                }
            });
            System.out.println("waiting for our move");
            makeBoard.enableBoard(true);
            sendMessage(outgoing, String.valueOf(grid.getNextMove()));
//                    messageOut = tcpProtocol.processInput(messageIn);
//                    sendMessage(messageOut);
            makeBoard.enableBoard(false);
            System.out.println("waiting for opponent");
        }
    }

    public static void invoke(Runnable r)
    {
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

    public static void sendMessage(PrintWriter outgoing, String msg)
    {
        outgoing.println(msg);
        outgoing.flush();
        System.out.println("server> " + msg);
    }

}
