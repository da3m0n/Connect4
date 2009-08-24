package game;

import java.net.ServerSocket;
import java.io.IOException;

public class Connect4GameServer
{
   private ServerSocket serverSocket;

   public Connect4GameServer() throws IOException
   {
      serverSocket = new ServerSocket(0);
   }

   public int getPort()
   {
      return serverSocket.getLocalPort();
   }
}
