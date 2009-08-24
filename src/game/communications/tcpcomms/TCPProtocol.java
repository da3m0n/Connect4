package game.communications.tcpcomms;

public class TCPProtocol
{
    private static final int WAITING = 0;
    private static final int CONNECTED = 1;
    private static final int PLAYER_MOVE = 2;

    private int _status = WAITING;

   public TCPProtocol()
   {
   }

   public String processInput(String theInput)
    {
        String theOutput = null;
        System.out.println("theInput = " + theInput);
        if(_status == WAITING)
        {
            theOutput = "Waiting for connection on port..." + theInput;
            _status = CONNECTED;
        }
        else if(_status == CONNECTED)
        {
            theOutput = "We are connected";
            _status = PLAYER_MOVE;
        }
        else if(_status == PLAYER_MOVE)
        {
            theOutput = "Playa Move";
            _status = PLAYER_MOVE;
        }
        return theOutput;
    }
}
