package game;

import java.util.ArrayList;

public class WinningInfo
{
    private GridEntry winner;
    private ArrayList<Coordinate> coords;

    public WinningInfo(GridEntry winner, ArrayList<Coordinate> coords)
    {
        this.winner = winner;
        this.coords = coords;
    }

    public WinningInfo()
    {
        this(GridEntry.empty, new ArrayList<Coordinate>());
    }

    public boolean hasNoWinner()
    {
        return winner == GridEntry.empty;
    }

    public boolean hasWinner()
    {
        return !hasNoWinner();
    }

    public GridEntry getWinner()
    {
        return winner;
    }

    public ArrayList<Coordinate> getCoordinates()
    {
        return coords;
    }
}
