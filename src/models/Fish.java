package models;

import java.awt.*;

public class Fish {
    private int x, y, width, height;
    private int speed;
    private Color bodyColor;

    public Fish(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.bodyColor = Color.ORANGE;
    }

    public void draw(Graphics2D g) {
        Color originalColor = g.getColor();

        drawTail(g);
        drawBody(g);
        drawEye(g);
        drawDorsalFin(g);
        drawVentralFin(g);

        g.setColor(originalColor);
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
}
