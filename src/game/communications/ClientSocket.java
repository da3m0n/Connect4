package game.communications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;

public class ClientSocket
{
   private static final String HOST = "localhost";
   private static final int PORT = 4445;
   private Socket _echoSocket = null;
   private PrintWriter _out = null;
   private BufferedReader _in = null;

   public ClientSocket() throws IOException
   {
      try
      {
         InetAddress addr = InetAddress.getByName(HOST);
         _echoSocket = new Socket(addr, PORT);
         _out = new PrintWriter(_echoSocket.getOutputStream(), true);
         _in = new BufferedReader(new InputStreamReader(_echoSocket.getInputStream()));
      }
      catch (UnknownHostException e)
      {
         System.err.println("Don't know about host localhost");
         System.exit(1);
      }
      catch (IOException e)
      {
         System.err.println("Coouldn't get I/O for the connection to localhost");
         System.exit(1);
      }     

      BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
      String fromserver;
      String fromUser;

      while ((fromserver = _in.readLine()) != null)
      {
         System.out.println("Server: " + fromserver);
         if (fromserver.equals("bye"))
         {
            break;
         }

         fromUser = stdIn.readLine();
         if (fromUser != null)
         {
            System.out.println("ClientSocket: " + fromUser);
            _out.println(fromUser);
         }
//         _out.println(fromserver);
//         System.out.println("echo: " + _in.readLine());
      }

      _out.close();
      _in.close();
      stdIn.close();
      _echoSocket.close();
   }

   public void connectSocket()
   {

   }
}
