package game;

public class Coordinate
{
   public final int row;
   public final int column;

   public Coordinate(int row, int column)
   {
      this.row = row;
      this.column = column;
   }


   public int getRow()
   {
      return row;
   }

   public int getColumn()
   {
      return column;
   }
}
