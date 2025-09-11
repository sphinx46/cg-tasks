package models;

import java.awt.*;
import java.util.Random;

public class Cloud {
    private int x, y, baseDiameter, overlap;
    private Color c;
    private Color borderColor;
    private int borderWidth;

    public Cloud(int x, int y, int baseDiameter, int overlap, Color c) {
        this.x = x;
        this.y = y;
        this.baseDiameter = baseDiameter;
        this.overlap = overlap;
        this.c = c;
        this.borderColor = Color.GRAY;
        this.borderWidth = 2;
    }

    public Cloud(int x, int y, int baseDiameter, int overlap, Color c, Color borderColor, int borderWidth) {
        this.x = x;
        this.y = y;
        this.baseDiameter = baseDiameter;
        this.overlap = overlap;
        this.c = c;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
    }

    public void draw(Graphics2D g) {
        Stroke originalStroke = g.getStroke();
        Color originalColor = g.getColor();

        g.setStroke(new BasicStroke(borderWidth));

        g.setColor(borderColor);
        g.drawOval(x, y, baseDiameter, baseDiameter);
        g.drawOval(x + baseDiameter - overlap + 10, y - 10, baseDiameter, baseDiameter);
        g.drawOval(x + baseDiameter - overlap + 35, y, baseDiameter, baseDiameter);
        g.drawOval(x + baseDiameter - overlap + 10, y + 15, baseDiameter, baseDiameter);

        g.setColor(c);
        g.fillOval(x, y, baseDiameter, baseDiameter);
        g.fillOval(x + baseDiameter - overlap + 10, y - 10, baseDiameter, baseDiameter);
        g.fillOval(x + baseDiameter - overlap + 35, y, baseDiameter, baseDiameter);
        g.fillOval(x + baseDiameter - overlap + 10, y + 15, baseDiameter, baseDiameter);

        g.setStroke(originalStroke);
        g.setColor(originalColor);
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
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

        CloudSize(int baseDiameter, int overlap, Color fillColor, Color borderColor, int borderWidth) {
            this.baseDiameter = baseDiameter;
            this.overlap = overlap;
            this.fillColor = fillColor;
            this.borderColor = borderColor;
            this.borderWidth = borderWidth;
        }

        public int getBaseDiameter() {
            return baseDiameter;
        }

        public int getOverlap() {
            return overlap;
        }

        public Color getFillColor() {
            return fillColor;
        }

        public Color getBorderColor() {
            return borderColor;
        }

        public int getBorderWidth() {
            return borderWidth;
        }

        public Cloud createAt(int x, int y) {
            return new Cloud(x, y, baseDiameter, overlap, fillColor, borderColor, borderWidth);
        }

        public static CloudSize getRandom() {
            Random random = new Random();
            Cloud.CloudSize[] types = values();
            return types[random.nextInt(types.length)];
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
