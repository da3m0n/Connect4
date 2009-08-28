package game;

import javax.swing.*;
import java.awt.*;

public class Disc extends JComponent
{
    private Graphics2D _g2d;

    public Disc(Graphics2D g2d)
    {
        _g2d = g2d;
    }

    public Disc(Graphics2D g2d, int x, int y, int width, int height)
    {
        _g2d = g2d;
        paint(g2d, x, y, width, height);
    }

    public void paint(Graphics2D g2d, int x, int y, int width, int height)
    {
//      super.paintComponent(g);
//      g2d = (Graphics2D) g;

        _g2d.setColor(Color.black);
        _g2d.setStroke(new BasicStroke(2));
        _g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        _g2d.drawOval(x, y, width, height);
    }

    public void drawLines(int x, int y, int x1, int y1)
    {
        _g2d.setColor(Color.yellow);
        _g2d.drawLine(x, y, x1, y1);
    }

    public void drawCircle(int x, int y, int width, int height)
    {
        _g2d.setColor(Color.black);
        _g2d.setStroke(new BasicStroke(2));
        _g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        _g2d.drawOval(x, y, width, height);
    }
}
