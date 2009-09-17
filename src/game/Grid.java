package game;

import static game.GridEntry.*;

import java.util.ArrayList;

public class Grid
{
    private static final int MAX_ROW_INDEX = 5;
    private static final int MAX_COLUMN_INDEX = 6;
    private GridEntry[][] _grid = new GridEntry[MAX_ROW_INDEX + 1][MAX_COLUMN_INDEX + 1];
    private int _currentRow = 0;
    private int _currentColumn = 0;
    private final Object _mutex = new Object();
    private ArrayList<Coordinate> _coords = new ArrayList<Coordinate>();
    private boolean _clearPressed;
    private boolean _enabled;

    public Grid()
    {
        reset();
    }

    public boolean play(GridEntry e, int column)
    {

        _currentColumn = column;
        for(int row = MAX_ROW_INDEX; row >= 0; row--)
        {
            _currentRow = row;

            if(_grid[row][column] == empty)
            {
                _grid[row][column] = e;
                synchronized(_mutex)
                {
                    if(_enabled)
                    {
                        _mutex.notify();
                    }
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
        if(winner.hasNoWinner())
        {
            winner = findRowWinner(false);
            if(winner.hasNoWinner())
            {
                winner = findDiagonalWinner();
            }
        }
        return winner;
    }


    private WinningInfo findRowWinner(boolean colIncr)
    {
        int index[] = new int[]{_currentRow, _currentColumn};
        int incrIndex;
        int size;
//      ArrayList<Coordinate> _coords = new ArrayList<Coordinate>();

        if(colIncr)
        {
            size = MAX_COLUMN_INDEX + 1;
            incrIndex = 1;
        }
        else
        {
            size = MAX_ROW_INDEX + 1;
            incrIndex = 0;
        }

        for(GridEntry winner : new GridEntry[]{Red, Blue})
        {
            index[incrIndex] = 0;
            int winners = 0;
            for(int column = 0; column < size; column++)
            {
                if(_grid[index[0]][index[1]] == winner)
                {
                    _coords.add(new Coordinate(index[0], index[1]));
                    winners++;
                    if(winners == 4)
                    {
                        return new WinningInfo(winner, _coords);
                    }
                }
                else
                {
                    _coords.clear();
                    winners = 0;
                }
                index[incrIndex]++;
            }
        }
        return new WinningInfo();
    }

    public void clearGrid()
    {
        reset();
        synchronized(_mutex)
        {
            _clearPressed = true;
            _mutex.notify();
        }
    }

    public void clearCoords()
    {
        _coords.clear();
    }

    public ArrayList<Coordinate> getCoords()
    {
        return findWinner().getCoordinates();
    }

    private WinningInfo findDiagonalWinner()
    {
        ArrayList<Coordinate> positions = new ArrayList<Coordinate>();

        for(int row = 0; row < _grid.length; row++)
        {
            for(int col = 0; col < _grid[row].length; col++)
            {
                if(checkDiagonal(row, col, -1, -1, positions) >= 4 || checkDiagonal(row, col, -1, 1, positions) >= 4)
                {
                    return new WinningInfo(_grid[row][col], positions);
                }
            }
        }
        return new WinningInfo();
    }

    private int checkDiagonal(int row, int col, int dRow, int dCol, ArrayList<Coordinate> positions)
    {
        int count = 1;
        GridEntry color = _grid[row][col];
        positions.clear();

        if(color == empty)
        {
            return 0;
        }
        positions.add(new Coordinate(row, col));

        row += dRow;
        col += dCol;

        while(row < _grid.length && row >= 0 && col >= 0 && col < _grid[0].length)
        {
            if(color != _grid[row][col])
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

    public void print()
    {
        System.out.println("");
        System.out.println("-----------------------------");
        for(GridEntry[] r : _grid)
        {
            System.out.print("| ");
            for(GridEntry c : r)
            {
                switch(c)
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
        return _grid[row][col];
    }

    public String getNextMove() throws InterruptedException
    {
        synchronized(_mutex)
        {
            _mutex.wait();
            if(_clearPressed)
            {
                _clearPressed = false;
                return "Clear";
            }

            return String.valueOf(_currentColumn);
        }
    }

    public void reset()
    {
        for(int i = 0; i < _grid.length; i++)
        {
            for(int j = 0; j < _grid[i].length; j++)
            {
                _grid[i][j] = empty;
            }
        }
    }

    public void setEnabled(boolean enabled)
    {
        _enabled = enabled;
    }
}
