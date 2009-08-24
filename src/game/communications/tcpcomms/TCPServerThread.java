package game.communications.tcpcomms;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TCPServerThread extends Thread
{
    private Socket _connection; // For communication with the client
    private ServerSocket _listener; // listens for a connection request
    private static final String HANDSHAKE = "Connect4";
    private static final char MESSAGE = '0'; // Added to the beginning of each message that is sent
    private static final char CLOSING = '1'; // Sent to the connect program when the user quits

    public TCPServerThread(Socket socket)
    {
        super("TCPServerThread");
        _connection = socket;
//        _listener = new ServerSocket()
    }

    public void run()
    {
        try
        {
            PrintWriter outgoing = new PrintWriter(_connection.getOutputStream(), true);
            BufferedReader incoming = new BufferedReader(new InputStreamReader(_connection.getInputStream()));
            String inputLine;
            String messageIn; // Message received from the client
            String messageOut; // Message to be sent to the client


            while((inputLine = incoming.readLine()) != null)
            {
                System.out.println("From server: " + inputLine);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

//   private String _data = "Establised connection";
//
//      try
//      {
//         ServerSocket serverSocket = new ServerSocket(44445);
//         Socket socket = serverSocket.accept();
//         System.out.println("Server connected...!");
//
//         System.out.println("Sending data..." + _data);
//         out.println(_data);
//         out.close();
//         serverSocket.close();
//         socket.close();
//      }
//      catch (IOException e)
//      {
//         e.printStackTrace();
//      }

    }
}
