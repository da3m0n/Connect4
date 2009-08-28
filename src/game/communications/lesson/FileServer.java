package game.communications.lesson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer
{

    static final int LISTENING_PORT = 3210;

    public static void main(String[] args)
    {

        File directory; // The directory from which the
        // gets the files that it serves.
        ServerSocket listener; // Listens for connection requests.
        Socket connection; // A socket for communicating with
        // a client.

        /* Check that there is a command-line argument.
If not, print a usage message and end. */

//        if(args.length == 0)
//        {
//            System.out.println("Usage: java FileServer <directory>");
//            return;
//        }

        /* Get the directory name from the command line, and make
   it into a file object. Check that the file exists and
   is in fact a directory. */

//        directory = new File(args[0]);
        directory = new File("/media/Media/movies and tv shows/");

        if(!directory.exists())
        {
            System.out.println("Specified directory does not exist.");
            return;
        }
        if(!directory.isDirectory())
        {
            System.out.println("The specified file is not a directory.");
            return;
        }

        /* Listen for connection requests from clients. For
each connection, create a separate Thread of type
ConnectionHandler to process it. The ConnectionHandler
class is defined below. The server runs until the
program is terminated, for example by a CONTROL-C. */

        try
        {
            listener = new ServerSocket(LISTENING_PORT);
            System.out.println("Listening on port " + LISTENING_PORT);
            while(true)
            {
                connection = listener.accept();
                new ConnectionHandler(directory, connection);
            }
        }
        catch(Exception e)
        {
            System.out.println("Server shut down unexpectedly.");
            System.out.println("Error: " + e);
            return;
        }

    } // end main()

    static class ConnectionHandler extends Thread
    {
        // An object of this class is a thread that will
        // process the connection with one client. The
        // thread starts itself in the constructor.

        File directory; // The directory from which files are served
        Socket connection; // A connection to the client.
        BufferedReader incoming; // For reading data from the client.
        PrintWriter outgoing; // For transmitting data to the client.

        ConnectionHandler(File dir, Socket conn)
        {
            // Constructor. Record the connection and
            // the directory and start the thread running.
            directory = dir;
            connection = conn;
            start();
        }

        void sendIndex() throws Exception
        {
            // This is called by the run() method in response
            // to an "index" command. Send the list of files
            // in the directory.
            String[] fileList = directory.list();
            for(int i = 0; i < fileList.length; i++)
                outgoing.println(fileList[i]);
            outgoing.flush();
            outgoing.close();
            if(outgoing.checkError())
            {
                throw new Exception("Error while transmitting data.");
            }
        }

        void sendFile(String fileName) throws Exception
        {
            // This is called by the run() command in response
            // to "get <fileName>" command. If the file doesn't
            // exist, send the message "error". Otherwise,
            // send the message "ok" followed by the contents
            // of the file.
            File file = new File(directory, fileName);
            if((!file.exists()) || file.isDirectory())
            {
                // (Note: Don't try to send a directory, which
                // shouldn't be there anyway.)
                outgoing.println("error");
            }
            else
            {
                outgoing.println("ok");
                BufferedReader fileIn = new BufferedReader(new FileReader(file));
                while(fileIn.readLine() != "")
                {
                    // Read and send lines from the file until
                    // an end-of-file is encountered.
                    String line = fileIn.readLine();
                    outgoing.println(line);
                }
            }
            outgoing.flush();
            outgoing.close();
            if(outgoing.checkError())
            {
                throw new Exception("Error while transmitting data.");
            }
        }

        public void run()
        {
            // This is the method that is executed by the thread.
            // It creates streams for communicating with the client,
            // reads a command from the client, and carries out that
            // command. The connection is logged to standard output.
            // An output beginning with ERROR indicates that a network
            // error occurred. A line beginning with OK means that
            // there was no network error, but does not imply that the
            // command from the client was a legal command.
            String command = "Command not read";
            try
            {
                incoming = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                outgoing = new PrintWriter(connection.getOutputStream());
                command = incoming.readLine();
                if(command.equals("index"))
                {
                    sendIndex();
                }
                else if(command.startsWith("get"))
                {
                    String fileName = command.substring(3).trim();
                    sendFile(fileName);
                }
                else
                {
                    outgoing.println("unknown command");
                    outgoing.flush();
                }
                System.out.println("OK " + connection.getInetAddress() + " " + command);
            }
            catch(Exception e)
            {
                System.out.println("ERROR " + connection.getInetAddress() + " " + command + " " + e);
            }
            finally
            {
                try
                {
                    connection.close();
                }
                catch(IOException e)
                {
                }
            }
        }

    } // end nested class ConnectionHandler

}