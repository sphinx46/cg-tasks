package models;

import java.awt.*;
import java.util.Random;

public class Fish {
    private int x, y;
    private final int width, height;
    private final int speed;
    private final Color bodyColor;
    private int panelWidth;
    private int panelHeight;
    private int horizonY;

    private double angle;
    private final int amplitude;
    private int baseY;

    public Fish(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        Random random = new Random();

        Color[] colors = {
            Color.RED,
            Color.GREEN,
            Color.BLUE,
            Color.ORANGE,
            Color.YELLOW,
            Color.PINK,
            Color.CYAN,
            Color.MAGENTA
        };
        this.bodyColor = colors[random.nextInt(colors.length)];

        this.angle = 0;
        this.amplitude = 20;
        this.baseY = y;
    }

    public void setPanelWidth(int panelWidth) { this.panelWidth = panelWidth; }
    public void setPanelHeight(int panelHeight) { this.panelHeight = panelHeight; }
    public void setHorizonY(int horizonY) { this.horizonY = horizonY; }

    public void update() {
        x += speed;

        if (x > panelWidth) {
            x = -width;
            baseY = horizonY + 50 + (int)(Math.random() * (panelHeight - horizonY - 100));
        }

        angle += 0.1;
        y = baseY + (int)(Math.sin(angle) * amplitude);
    }

    public void draw(Graphics2D g) {
        RenderingHints originalHints = g.getRenderingHints();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawTail(g);
        drawBody(g);
        drawEye(g);
        drawDorsalFin(g);
        drawVentralFin(g);

        g.setRenderingHints(originalHints);
    }

    private void drawTail(Graphics2D g) {
        g.setColor(bodyColor);
        int tailWidth = width / 2;

        int[] tailXPoints = {x - tailWidth + 5, x, x - tailWidth + 5};
        int[] tailYPoints = {y, y + height / 2, y + height};
        g.fillPolygon(tailXPoints, tailYPoints, 3);
    }

    private void drawBody(Graphics2D g) {
        g.setColor(bodyColor);
        g.fillOval(x, y, width, height);
    }

    private void drawEye(Graphics2D g) {
        g.setColor(Color.WHITE);
        int eyeX = x + 3 * width / 4;
        int eyeY = y + height / 4;
        int eyeWidth = width / 6;
        int eyeHeight = height / 6;
        g.fillOval(eyeX, eyeY, eyeWidth, eyeHeight);

        g.setColor(Color.BLACK);
        int pupilOffset = 2;
        int pupilWidth = width / 10;
        int pupilHeight = height / 10;
        g.fillOval(eyeX + pupilOffset, eyeY + pupilOffset, pupilWidth, pupilHeight);
    }

    private void drawDorsalFin(Graphics2D g) {
        g.setColor(bodyColor.darker());

        int finStartX = x + width / 3;
        int finPeakX = x + width / 2;
        int finEndX = x + 2 * width / 3;

        int finStartY = y;
        int finPeakY = y - height / 2;
        int finEndY = y;

        int[] finXPoints = {finStartX, finPeakX, finEndX};
        int[] finYPoints = {finStartY, finPeakY, finEndY};

        g.fillPolygon(finXPoints, finYPoints, 3);
    }

    private void drawVentralFin(Graphics2D g) {
        g.setColor(bodyColor.darker());

        int finStartX = x + width / 3;
        int finPeakX = x + width / 2;
        int finEndX = x + 2 * width / 3;

        int finStartY = y + height;
        int finPeakY = y + height + height / 2;
        int finEndY = y + height;

        int[] finXPoints = {finStartX, finPeakX, finEndX};
        int[] finYPoints = {finStartY, finPeakY, finEndY};

        g.fillPolygon(finXPoints, finYPoints, 3);
    }

    public int getY() {
        return y;
    }

}
