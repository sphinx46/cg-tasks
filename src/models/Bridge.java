package models;

import java.awt.*;
import java.awt.geom.QuadCurve2D;

public class Bridge {
    private int x, y, width, height;
    private Color color;
    private int archHeight;

    public Bridge(int x, int y, int width, int height, Color color, int archHeight) {
        this.x = x;
        this.y = y - 30;
        this.width = width;
        this.height = height;
        this.color = color;
        this.archHeight = archHeight;
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        QuadCurve2D arch = new QuadCurve2D.Float(
            x, y + height,
            x + width / 2, y - archHeight,
            x + width, y + height
        );
        g.fill(arch);

        g.setColor(color.darker());
        QuadCurve2D road = new QuadCurve2D.Float(
            x, y + height - 5,
            x + width / 2, y - archHeight + 5,
            x + width, y + height - 5
        );

        g.fill(road);

        g.setColor(new Color(80, 80, 80));
        int pillarCount = width / 50;
        int pillarSpacing = width / pillarCount;

        for (int i = 1; i < pillarCount - 1; i++) {
            int pillarX = x + i * pillarSpacing;
            int pillarY = y + height;
            g.fillRect(pillarX - 5, pillarY, 10, 60);
        }

        g.setColor(new Color(180, 180, 180));
        g.setStroke(new BasicStroke(3));

        QuadCurve2D topRail = new QuadCurve2D.Float(
            x, y + height - 15,
            x + width / 2, y - archHeight - 10,
            x + width, y + height - 15
        );
        QuadCurve2D bottomRail = new QuadCurve2D.Float(
            x, y + height - 5,
            x + width / 2, y - archHeight,
            x + width, y + height - 5
        );
        g.draw(topRail);
        g.draw(bottomRail);

        for (int i = 1; i < pillarCount; i++) {
            int pillarX = x + i * pillarSpacing;
            double t = (double) i / pillarCount;
            int railY = (int) (y + height - archHeight * 4 * t * (1 - t)) ;

            g.drawLine(pillarX, railY + height - 15, pillarX, railY + height - 5);
        }
    }
}
