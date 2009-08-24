package game.communications.multicast;

import game.Client;
import game.ClientsDisplayPanel;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.DateFormat;
import java.util.Date;

public class Connect4MulticastClient extends Thread
{
   private MulticastSocket _multicastSocket;
   private InetAddress _address;
   private boolean _run = true;
   private ClientsDisplayPanel _clientsDisplayPanel;

   public Connect4MulticastClient(ClientsDisplayPanel clientsDisplayPanel) throws IOException
   {
      _clientsDisplayPanel = clientsDisplayPanel;
      _multicastSocket = new MulticastSocket(4446);
      _address = InetAddress.getByName("230.0.0.1");
      _multicastSocket.joinGroup(_address);
//      new CheckClients(1, _multicastSocket);
   }

   public void run()
   {
      while (_run)
      {
         byte[] packetData = new byte[256];
         final DatagramPacket packet = new DatagramPacket(packetData, packetData.length);

         try
         {
            _multicastSocket.receive(packet);
            Date now = new Date();
            DateFormat time = DateFormat.getTimeInstance();
            String formattedTime = time.format(now);
//            System.out.println("received time: " + formattedTime);

            SwingUtilities.invokeLater(new Runnable()
            {
               public void run()
               {
                  String port = new String(packet.getData(), 0, packet.getLength());
//                  System.out.println("port: " + port + " packetAddress: " + packet.getAddress() + " : " + packet.getPort());
                  _clientsDisplayPanel.addClient(new Client(port, packet.getAddress()));
//                  _clientsDisplayPanel.addNewClient(port, packet.getIPAddress());
               }
            });
         }
         catch (IOException e)
         {
            _run = false;
            e.printStackTrace();
         }
      }

      try
      {
         _multicastSocket.leaveGroup(_address);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      _multicastSocket.close();
   }

}
