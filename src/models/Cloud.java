package models;

import java.awt.*;
import java.util.Random;

public class Cloud {
    private static final int DEFAULT_BORDER_WIDTH = 2;
    private static final Color DEFAULT_BORDER_COLOR = Color.GRAY;

    private final int x, y, baseDiameter, overlap;
    private final Color fillColor;
    private final Color borderColor;
    private final int borderWidth;

    public Cloud(int x, int y, int baseDiameter, int overlap, Color fillColor) {
        this(x, y, baseDiameter, overlap, fillColor, DEFAULT_BORDER_COLOR, DEFAULT_BORDER_WIDTH);
    }

    public Cloud(int x, int y, int baseDiameter, int overlap,
                 Color fillColor, Color borderColor, int borderWidth) {
        this.x = x;
        this.y = y;
        this.baseDiameter = baseDiameter;
        this.overlap = overlap;
        this.fillColor = fillColor;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
    }

    public void draw(Graphics2D g) {
        configureGraphicsForDrawing(g);

        drawCloudOutlines(g);
        drawCloudFill(g);

    }

    private void configureGraphicsForDrawing(Graphics2D g) {
        g.setStroke(new BasicStroke(borderWidth));
    }

    private void drawCloudOutlines(Graphics2D g) {
        g.setColor(borderColor);
        drawCloudCircles(g, false);
    }

    private void drawCloudFill(Graphics2D g) {
        g.setColor(fillColor);
        drawCloudCircles(g, true);
    }

    private void drawCloudCircles(Graphics2D g, boolean fill) {
        int[] circleOffsetsX = {
            0,
            baseDiameter - overlap + 10,
            baseDiameter - overlap + 35,
            baseDiameter - overlap + 10
        };

        int[] circleOffsetsY = {
            0,
            -10,
            0,
            15
        };

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

    public enum CloudSize {
        SMALL(30, 20, new Color(240, 240, 240), new Color(200, 200, 200), 1),
        MEDIUM(50, 30, new Color(240, 240, 240), new Color(200, 200, 200), 1),
        LARGE(70, 40, new Color(240, 240, 240), new Color(200, 200, 200), 1);

        private final int baseDiameter;
        private final int overlap;
        private final Color fillColor;
        private final Color borderColor;
        private final int borderWidth;

        CloudSize(int baseDiameter, int overlap, Color fillColor,
                  Color borderColor, int borderWidth) {
            this.baseDiameter = baseDiameter;
            this.overlap = overlap;
            this.fillColor = fillColor;
            this.borderColor = borderColor;
            this.borderWidth = borderWidth;
        }

        public Cloud createAt(int x, int y) {
            return new Cloud(x, y, baseDiameter, overlap,
                fillColor, borderColor, borderWidth);
        }

        public static CloudSize getRandom() {
            Random random = new Random();
            CloudSize[] sizes = values();
            return sizes[random.nextInt(sizes.length)];
        }
    }

    public static Cloud createSmall(int x, int y) {
        return CloudSize.SMALL.createAt(x, y);
    }

    public static Cloud createMedium(int x, int y) {
        return CloudSize.MEDIUM.createAt(x, y);
    }

    public static Cloud createLarge(int x, int y) {
        return CloudSize.LARGE.createAt(x, y);
    }

    public static Cloud createRandom(int x, int y) {
        return CloudSize.getRandom().createAt(x, y);
    }
}
