package game;

import javax.swing.*;
import java.awt.*;

public class PaintedGrid extends JPanel
{
   private int _columnWidth = 0;
   private static final int STROKE = 1;
   private final Grid _grid;

   public PaintedGrid(Grid grid)
   {
      _grid = grid;
   }

   public void paint(Graphics g)
   {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;

      g2d.setColor(Color.black);
      g2d.setStroke(new BasicStroke(STROKE));
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      int width = getWidth() - 1;
      int columnWidth = width / 7;
      int rowHeight = getHeight() / 6;

      // vertical lines
//      System.out.println("width  = " + getWidth());

      g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
      for (int i = 0; i < 7; i++)
      {
         g2d.drawLine(i * columnWidth, 0, i * columnWidth, getHeight());
      }

      // horizontal lines
      for (int i = 0; i < 6; i++)
      {
         g2d.drawLine(0, i * rowHeight, getWidth(), i * rowHeight);
      }

      for (int row = 0; row < _grid.getRowCount(); row++)
      {
         for (int col = 0; col < _grid.getColCount(); col++)
         {
            GridEntry piece = _grid.getPeice(row, col);
            if (piece != GridEntry.empty)
            {
               if (piece == GridEntry.Blue)
               {
                  g2d.setColor(Color.blue);
               }
               else
               {
                  g2d.setColor(Color.red);
               }

               g2d.fillOval(columnWidth * col + 2, rowHeight * row + 2, columnWidth - 5, rowHeight - 5);
               g2d.setColor(Color.black);
               g2d.setStroke(new BasicStroke(2));
               g2d.drawOval(columnWidth * col + 2, rowHeight * row + 2, columnWidth - 5, rowHeight - 5);
            }
         }
      }
/*
      if (_grid.findWinner().hasWinner())
      {
         for (Coordinate coord : _grid.getCoords())
         {
//            Disc disc = new Disc(g2d);
//            disc.drawCircle(columnWidth * coord.column + 2, rowHeight * coord.row + 2, columnWidth - 5, rowHeight - 5);
//            disc.drawCircle(columnWidth * coord.column + 18, rowHeight * coord.row + 18, columnWidth - 40, rowHeight - 40);
//            disc.drawLines((columnWidth * coord.column ) / 2, rowHeight * coord.row + 4, columnWidth * coord.column + 15, rowHeight * coord.row + 55);
//            disc.drawLines(columnWidth / 2, rowHeight * coord.row + 4, columnWidth * coord.column + 105, rowHeight * coord.row + 75);
            g2d.setColor(Color.yellow);
            g2d.fillOval(columnWidth * coord.column + 19, rowHeight * coord.row + 19, columnWidth - 40, rowHeight - 40);
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(columnWidth * coord.column + 18, rowHeight * coord.row + 18, columnWidth - 38, rowHeight - 38);
//            g2d.drawString("W", columnWidth * coord.column + 28, rowHeight * coord.row + 38);
            System.out.println("columnWidth: " + columnWidth + " rowHeight: " + rowHeight + " coord.column: " + coord.column);
            System.out.println("width: " + (columnWidth * (coord.column + 1)) / 2);
            g2d.drawString("W", (columnWidth * (coord.column + 1)) / 2, rowHeight * coord.row + 38);

//            new Disc(g2d, columnWidth * coord.column + 2, rowHeight * coord.row + 2, columnWidth - 5, rowHeight - 5);

         }
      }
      */
   }

   public int giveMeTheColumn(int point)
   {
      int columns = getWidth() / _grid.getColCount();

      return point / columns;
   }

}
