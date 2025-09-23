package ru.vsu.cs.computergraphics.mordvinovil.task1.models;

import java.awt.*;
import java.util.Random;

public class Cloud {
    private static final Color FILL_COLOR = new Color(255, 255, 255, 255);
    private static final Color BORDER_COLOR = new Color(255, 255, 255, 128);
    private static final int BORDER_WIDTH = 1;

    private final int x, y, baseDiameter, overlap;

    public Cloud(int x, int y, int baseDiameter, int overlap) {
        this.x = x;
        this.y = y;
        this.baseDiameter = baseDiameter;
        this.overlap = overlap;
    }

    public void draw(Graphics2D g) {
        Composite originalComposite = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));

        g.setStroke(new BasicStroke(BORDER_WIDTH));

        g.setColor(FILL_COLOR);
        drawCloudCircles(g, true);

        g.setColor(BORDER_COLOR);
        drawCloudCircles(g, false);

        g.setComposite(originalComposite);
    }

    private void drawCloudCircles(Graphics2D g, boolean fill) {
        int[] circleOffsetsX = {0, baseDiameter - overlap + 10, baseDiameter - overlap + 35, baseDiameter - overlap + 10};
        int[] circleOffsetsY = {0, -10, 0, 15};

        for (int i = 0; i < circleOffsetsX.length; i++) {
            int circleX = x + circleOffsetsX[i];
            int circleY = y + circleOffsetsY[i];

            if (fill) {
                g.fillOval(circleX, circleY, baseDiameter, baseDiameter);
            } else {
                g.drawOval(circleX, circleY, baseDiameter, baseDiameter);
            }
        }
    }

    public static Cloud createRandom(int x, int y) {
        Random random = new Random();
        int baseDiameter = 30 + random.nextInt(50);
        int overlap = baseDiameter / 2 + random.nextInt(baseDiameter / 4);
        return new Cloud(x, y, baseDiameter, overlap);
    }
}
