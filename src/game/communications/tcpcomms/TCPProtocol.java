package game.communications.tcpcomms;

public class TCPProtocol
{
    private static final int WAITING_FOR_CONNECTION = 0;
    private static final int CONNECTED = 1;
    private static final int WAITING_FOR_PLAYER_RESPONSE = 2;
    private static final int PLAYER_A_MOVE = 3;
    private static final int PLAYER_B_MOVE = 4;
    private static final int ANOTHER = 5;

    private int _status = WAITING_FOR_CONNECTION;

    public TCPProtocol()
    {
        
    }

   public String processInput(String theInput)
    {
        String theOutput = null;

        if(_status == WAITING_FOR_CONNECTION)
        {
            theOutput = "Waiting for connection on port..." + theInput;
            _status = CONNECTED;
        }
        else if(_status == CONNECTED)
        {
            theOutput = "Connection established...";
            _status = WAITING_FOR_PLAYER_RESPONSE;
        }
        else if(_status == WAITING_FOR_PLAYER_RESPONSE)
        {
            theOutput = "Waiting for player B to move...";
            _status = PLAYER_B_MOVE;
        }
        else if(_status == PLAYER_A_MOVE)
        {
            theOutput = "Player A, your move...";
//            _status = PLAYER_B_MOVE;
        }
        else if(_status == PLAYER_B_MOVE)
        {
            theOutput = "Player B, your move...";
//            _status = PLAYER_A_MOVE;
        }
        else if(_status == ANOTHER)
        {
            theOutput = "Game over";
            _status = WAITING_FOR_CONNECTION;
        }

        if(theInput.equalsIgnoreCase("3"))
        {
           System.out.println("clicked pos 3");
        }
        return theOutput;
    }
}
