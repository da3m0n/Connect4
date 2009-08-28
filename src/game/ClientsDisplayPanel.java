package game;

import game.communications.tcpcomms.TCPServer;
import game.communications.tcpcomms.TCPClient;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class ClientsDisplayPanel extends JPanel
{
    private DefaultTableModel _clients = new DefaultTableModel(new Object[]{"Clients", "IP Address"}, 0)
    {
        public boolean isCellEditable(int row, int column)
        {
            return false;
        }

    };
    private JTable _table = new JTable();
    private Map<Client, Long> _currentClients = new HashMap<Client, Long>();

    public ClientsDisplayPanel(final Grid grid, final Game game, final MakeBoard makeBoard)
    {
//      setPreferredSize(new Dimension(180, 100));
        setBackground(Color.lightGray);
        setBorder(BorderFactory.createLineBorder(Color.darkGray));

        _table.setModel(_clients);
        _table.setFillsViewportHeight(true);
        _table.setPreferredScrollableViewportSize(new Dimension(180, 150));
        _table.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                if(e.getClickCount() == 2)
                {
                    int row = _table.rowAtPoint(e.getPoint());

                    final Client client = getSelectedClient(row);
                    if(row >= 0)
                    {
//                  new NotificationDialog(game.getFrame(), client, grid);
                        String text = "Do you want to play a game with " + client;
                        final NotificationDialog notificationDialog = new NotificationDialog(game.getFrame(), text);
                        notificationDialog.addMyListener(new ActionListener()
                        {
                            public void actionPerformed(ActionEvent e)
                            {
                                new TCPClient(client, grid, makeBoard).start();
                                notificationDialog.setVisible(false);
                            }
                        });
                        notificationDialog.setVisible(true);


//                  int res = JOptionPane.showConfirmDialog(ClientsDisplayPanel.this, "Do you want to play a game with " + _clients.getDataVector().get(row), "Play", JOptionPane.YES_NO_OPTION);
//                  if (res == JOptionPane.YES_OPTION)
//                  {
//                     new TCPClient(client, grid).start();
//                  }
                    }

                }
            }
        });
//      _table.setDefaultRenderer(Color.class, new ColorRenderer(true));

        JScrollPane scrollPane = new JScrollPane(_table);
        add(scrollPane);

        _clients.addTableModelListener(new TableModelListener()
        {
            public void tableChanged(TableModelEvent e)
            {
                _table.setModel(_clients);
            }
        });

        clearOldClients();
    }

    private Client getSelectedClient(int row)
    {
        Set s = _currentClients.keySet();
        Iterator itr = s.iterator();

        int i = 0;

        while(itr.hasNext())
        {
            Client cl = (Client) itr.next();
            if(i == row)
            {
                return cl;
            }
            i++;
        }

        return null;
    }

    public void clearOldClients()
    {
        Timer timer = new Timer(1, new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                for(Client c : new ArrayList<Client>(_currentClients.keySet()))
                {
                    checkIfTimeOutExceeded(c, _currentClients.get(c));
                }
            }
        });
        timer.setDelay(1000);
        timer.setRepeats(true);
        timer.start();
    }

    private void checkIfTimeOutExceeded(final Client c, final Long time)
    {
        long timeOut = time + 10000;

        if(timeOut < System.currentTimeMillis())
        {
            _currentClients.remove(c);
            displayClients();
        }
    }

    public void addClient(Client cl)
    {
        try
        {
            Client me = new Client(String.valueOf(TCPServer.getInstance().getPort()), InetAddress.getLocalHost());

            if(!cl.equals(me))
            {
                _currentClients.put(cl, System.currentTimeMillis());
            }
            displayClients();
        }
        catch(UnknownHostException e)
        {
            e.printStackTrace();
        }
    }

    private void displayClients()
    {
        int row = _table.getSelectedRow();

        _clients.setRowCount(0);
        for(Client cl : _currentClients.keySet())
        {
            _clients.addRow(new Object[]{cl.getPort(), cl.getIPAddress()});
        }

        if(row >= 0)
        {
            _table.setRowSelectionInterval(row, row);
        }
    }


    private class ColorRenderer extends JLabel implements TableCellRenderer
    {
        private boolean _bordered;
        private Border _selectedBorder;
        private Border _unselectedBorder;

        public ColorRenderer(boolean isBordered)
        {
            _bordered = isBordered;
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object color, boolean isSelected, boolean hasFocus, int row, int column)
        {
            Color newColor = (Color) color;
            setBackground(newColor);
            if(_bordered)
            {
                if(isSelected)
                {
                    if(_selectedBorder == null)
                    {
                        _selectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5, table.getSelectionBackground());
                    }
                    setBorder(_selectedBorder);
                }
                else
                {
                    if(_unselectedBorder == null)
                    {
                        _unselectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5, table.getBackground());
                    }
                    setBorder(_unselectedBorder);
                }
            }
            return this;
        }
    }

}
