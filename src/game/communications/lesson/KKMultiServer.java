package game.communications.lesson;

import java.io.IOException;
import java.net.ServerSocket;

public class KKMultiServer
{
   public static void main(String[] args) throws IOException
   {
      ServerSocket serverSocket = null;
      boolean listening = true;

      try
      {
         serverSocket = new ServerSocket(4444);
      }
      catch (IOException e)
      {
         System.err.println("Could not listen on port: 4444.");
         System.exit(-1);
      }

      while (listening)
      {
         new KKMultiServerThread(serverSocket.accept()).start();
         System.out.println("KKMultiServer.main...started");
      }

      serverSocket.close();
   }
}