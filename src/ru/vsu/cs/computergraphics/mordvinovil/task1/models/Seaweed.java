package ru.vsu.cs.computergraphics.mordvinovil.task1.models;

import java.awt.*;
import java.util.Random;

public class Seaweed {
    private final int x;
    private final int y;
    private final Color color;
    private final Random random = new Random();

    private static final Color SEAWEED_COLOR = new Color(60, 179, 113);

    public Seaweed(int x, int y) {
        this.x = x;
        this.y = y;
        this.color = new Color(
            SEAWEED_COLOR.getRed() + random.nextInt(30) - 15,
            SEAWEED_COLOR.getGreen() + random.nextInt(30) - 15,
            SEAWEED_COLOR.getBlue() + random.nextInt(30) - 15
        );
    }

    public void draw(Graphics2D g, int panelHeight) {
        int seaweedY = panelHeight - y - 40;
        g.setColor(color);
        g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(x, seaweedY + y, x, seaweedY);

        int leafCount = 3 + random.nextInt(4);
        for (int i = 0; i < leafCount; i++) {
            int leafY = seaweedY + y - (i * y / leafCount);
            drawLeaf(g, x, leafY, random.nextBoolean());
        }
    }

    private void drawLeaf(Graphics2D g, int x, int y, boolean leftDirection) {
        int direction = leftDirection ? -1 : 1;
        int leafLength = 15 + random.nextInt(20);
        int x1 = x;
        int y1 = y;
        int x2 = x + direction * leafLength;
        int y2 = y - leafLength / 2;
        int x3 = x + direction * leafLength / 2;
        int y3 = y - leafLength;
        int[] xPoints = {x1, x2, x3};
        int[] yPoints = {y1, y2, y3};
        g.fillPolygon(xPoints, yPoints, 3);
    }
}
