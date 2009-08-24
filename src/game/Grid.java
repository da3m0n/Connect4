package game;

import static game.GridEntry.*;

import java.util.ArrayList;

public class Grid
{
   private static final int MAX_ROW_INDEX = 5;
   private static final int MAX_COLUMN_INDEX = 6;
   private GridEntry[][] grid = new GridEntry[MAX_ROW_INDEX + 1][MAX_COLUMN_INDEX + 1];
   private int currentRow = 0;
   private int currentColumn = 0;
   private final Object _mutex = new Object();
   private ArrayList<Coordinate> coords = new ArrayList<Coordinate>();

   public Grid()
   {
      clearGrid();
   }

   public boolean play(GridEntry e, int column)
   {

      currentColumn = column;
      for (int row = MAX_ROW_INDEX; row >= 0; row--)
      {
         currentRow = row;

         if (grid[row][column] == empty)
         {
            grid[row][column] = e;
            synchronized(_mutex) {
                  _mutex.notify();
            }
            return true;
         }
      }
      return false;

   }

   public WinningInfo findWinner()
   {
      // check four in row
      WinningInfo winner = findRowWinner(true);
      if (winner.hasNoWinner())
      {
         winner = findRowWinner(false);
         if (winner.hasNoWinner())
         {
            winner = findDiagonalWinner();
         }
      }
      return winner;
   }


   private WinningInfo findRowWinner(boolean colIncr)
   {
      int index[] = new int[]{currentRow, currentColumn};
      int incrIndex;
      int size;
//      ArrayList<Coordinate> coords = new ArrayList<Coordinate>();

      if (colIncr)
      {
         size = MAX_COLUMN_INDEX + 1;
         incrIndex = 1;
      }
      else
      {
         size = MAX_ROW_INDEX + 1;
         incrIndex = 0;
      }

      for (GridEntry winner : new GridEntry[]{Red, Blue})
      {
         index[incrIndex] = 0;
         int winners = 0;
         for (int column = 0; column < size; column++)
         {
            if (grid[index[0]][index[1]] == winner)
            {
               coords.add(new Coordinate(index[0], index[1]));
               winners++;
               if (winners == 4)
               {
                  return new WinningInfo(winner, coords);
               }
            }
            else
            {
               coords.clear();
               winners = 0;
            }
            index[incrIndex]++;
         }
      }
      return new WinningInfo();
   }

   public void clearGrid()
   {
      for (int i = 0; i < grid.length; i++)
      {
         for (int j = 0; j < grid[i].length; j++)
         {
            grid[i][j] = empty;
         }
      }
   }

   public void clearCoords()
   {
      coords.clear();
   }

   public ArrayList<Coordinate> getCoords()
   {
      return findWinner().getCoordinates();
   }

   private WinningInfo findDiagonalWinner()
   {
      ArrayList<Coordinate> positions = new ArrayList<Coordinate>();

      for (int row = 0; row < grid.length; row++)
      {
         for (int col = 0; col < grid[row].length; col++)
         {
            if (checkDiagonal(row, col, -1, -1, positions) >= 4 || checkDiagonal(row, col, -1, 1, positions) >= 4)
            {
               return new WinningInfo(grid[row][col], positions);
            }
         }
      }
      return new WinningInfo();
   }

   private int checkDiagonal(int row, int col, int dRow, int dCol, ArrayList<Coordinate> positions)
   {
      int count = 1;
      GridEntry color = grid[row][col];
      positions.clear();

      if (color == empty)
      {
         return 0;
      }
      positions.add(new Coordinate(row, col));

      row += dRow;
      col += dCol;

      while (row < grid.length && row >= 0 && col >= 0 && col < grid[0].length)
      {
         if (color != grid[row][col])
         {
            return count;
         }

         positions.add(new Coordinate(row, col));

         count++;
         row += dRow;
         col += dCol;
      }

      return count;
   }

   private GridEntry findColumnWinner()
   {
      int reds = 0;
      int blues = 0;

      for (int row = 0; row < MAX_ROW_INDEX + 1; row++)
      {
         if (grid[row][currentColumn] == Red)
         {
            blues = 0;
            reds++;
            if (reds == 4)
            {
               System.out.println("Red Wins!!!");
               return Red;
            }
         }
         else if (grid[row][currentColumn] == Blue)
         {
            reds = 0;
            blues++;
            if (blues == 4)
            {
               System.out.println("Blue Wins!!!");
               return Blue;
            }
         }
      }
      return empty;
   }

   public void print()
   {
      System.out.println("");
      System.out.println("-----------------------------");
      for (GridEntry[] r : grid)
      {
         System.out.print("| ");
         for (GridEntry c : r)
         {
            switch (c)
            {
               case Red:
                  System.out.print("R");
                  break;
               case Blue:
                  System.out.print("B");
                  break;
               case empty:
                  System.out.print(" ");
                  break;
            }
            System.out.print(" | ");
         }
         System.out.println();
         System.out.println("-----------------------------");
      }
   }

   public boolean isFull()
   {
      return false;
   }

   public int getColCount()
   {
      return MAX_COLUMN_INDEX + 1;
   }

   public int getRowCount()
   {
      return MAX_ROW_INDEX + 1;
   }

   public GridEntry getPeice(int row, int col)
   {
      return grid[row][col];
   }

   public int getNextMove() throws InterruptedException
   {
      synchronized(_mutex) {
         _mutex.wait();
         return currentColumn;
      }
   }
}
