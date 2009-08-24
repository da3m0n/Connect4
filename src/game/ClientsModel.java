package game;

import javax.swing.table.AbstractTableModel;

public class ClientsModel extends AbstractTableModel
{
   private String[] _columnNames = {"Clients", "IP Address"};
   private String[][] _data;

   public int getRowCount()
   {
      return _data.length;
   }

   public int getColumnCount()
   {
      return _columnNames.length;
   }

   public void setValueAt(String value, int row, int col)
   {
      _data[row][col] = value;
      fireTableCellUpdated(row, col);

   }

   public Object getValueAt(int rowIndex, int columnIndex)
   {
      return _data[rowIndex][columnIndex];
   }
}
