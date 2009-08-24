package game;

import game.communications.tcpcomms.TCPServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Game// extends JFrame
{
   private static final int WIDTH = 650;
   private static final int HEIGHT = 550;
   private JFrame _frame = new JFrame();

    public Game() throws IOException
   {
      _frame.setTitle("Connect 4: " + TCPServer.getInstance().getPort());
      _frame.setSize(WIDTH, HEIGHT);

       MakeBoard _makeBoard = new MakeBoard(this);
       _frame.getContentPane().add(_makeBoard);

      _frame.addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent e)
         {
            _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         }
      });

      centerOnScreen();
      _frame.setVisible(true);
   }

   public void centerOnScreen()
   {
      final Toolkit tk = Toolkit.getDefaultToolkit();
      final Dimension d = tk.getScreenSize();

      _frame.setLocation(d.width / 3, d.height / 3);
   }

    public JFrame getFrame()
    {
        return _frame;
    }

   public static void main(final String[] args) throws IOException
   {
      new Game();
   }
}
