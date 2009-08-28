package game.Timer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.text.DateFormat;
import java.util.*;

public class CheckClients
{
    private MulticastSocket _multicastSocket;
    //   private HashSet<Integer> _portsCollection = new HashSet<Integer>();
    private Map<Integer, String> _portsCollectionMap = new HashMap<Integer, String>();

    public CheckClients(int seconds, MulticastSocket multicastSocket)
    {
        _multicastSocket = multicastSocket;
//      _address = address;

        Timer timer = new Timer();
        timer.schedule(new CheckClientsTask(), seconds * 1000);
    }


    public CheckClients(int seconds)
    {
        Timer timer = new Timer();
        timer.schedule(new CheckClientsTask(), seconds * 1000);
    }

    private class CheckClientsTask extends TimerTask
    {

        public void run()
        {
            byte[] packetData = new byte[256];
            final DatagramPacket packet = new DatagramPacket(packetData, packetData.length);

            while(true)
            {
                try
                {
                    _multicastSocket.receive(packet);
                    Date now = new Date();
                    DateFormat time = DateFormat.getTimeInstance();
                    String formattedTime = time.format(now);

//               _portsCollection.add(packet.getPort());
//               checkIfClientSentRecentMessage(formattedTime, _portsCollection);

//               _portsCollectionMap.put(packet.getPort(), formattedTime);

//               checkIfClientSentRecentMessage(_portsCollectionMap);
//               System.out.println("CheckClients receive: " + now + " " + packet.getIPAddress() + " port: " + packet.getPort());
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
//            String receivedData = new String(packet.getData(), 0, packet.getLength());
            }
        }
    }

    private void checkIfClientSentRecentMessage(Map<Integer, String> portsCollectionMap)
    {
        _portsCollectionMap = portsCollectionMap;

        Collection col = _portsCollectionMap.values();
        Iterator it = col.iterator();

        while(it.hasNext())
        {
            Map.Entry<Integer, String> entry = (Map.Entry<Integer, String>) it.next();
            Integer key = (Integer) entry.getKey();
            String value = entry.getValue();
            System.out.println("Entry: " + key + " " + value);
        }
    }

//   private boolean checkIfClientSentRecentMessage(String formattedTime, HashSet<Integer> portsCollection)
//   {
//      _portsCollection = portsCollection;
//
//      Iterator it = _portsCollection.iterator();
//      while(it.hasNext())
//      {
//         System.out.println(it.next() + " " + formattedTime);
//      }


//      Calendar cal = Calendar.getInstance();
//      long before = cal.getTimeInMillis();
//
//      System.out.println("Current time: " + cal.getTime());
//      System.out.println("Time in millis: " + before);
//
//      // add to the time
//      cal.add(Calendar.MINUTE, 2);
//      long after = cal.getTimeInMillis();
//
//      System.out.println("Adding 2 mins...");
//      System.out.println("New Current time: " + cal.getTime());
//      System.out.println("New Time in millis: " + after);

//      return false;
//   }
}
