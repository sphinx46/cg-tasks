package ru.vsu.cs.computergraphics.mordvinovil.task1.models;

import ru.vsu.cs.computergraphics.mordvinovil.task1.Mover;

import java.awt.*;
import java.util.Random;

public class Fish {
    private final Mover mover;
    private final int width, height;
    private final Color bodyColor;

    public Fish(Mover mover, int width, int height, Color bodyColor) {
        this.mover = mover;
        this.width = width;
        this.height = height;
        this.bodyColor = bodyColor;
    }

    public Fish(int x, int y, int width, int height, int speed) {
        Random random = new Random();
        Color[] colors = {
            Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE,
            Color.YELLOW, Color.PINK, Color.CYAN, Color.MAGENTA
        };
        this.bodyColor = colors[random.nextInt(colors.length)];
        this.width = width;
        this.height = height;
        this.mover = new Mover(x, y, speed, 20, Mover.MovementType.FISH_SWIM);
    }

    public void setPanelWidth(int panelWidth) {
        mover.setPanelWidth(panelWidth);
    }

    public void setPanelHeight(int panelHeight) {
        mover.setPanelDimensions(mover.getX(), panelHeight);
    }

    public void setHorizonY(int horizonY) {
        mover.setStartY(horizonY + 50);
    }

    public void update() {
        mover.update();
    }

    public void draw(Graphics2D g) {
        RenderingHints originalHints = g.getRenderingHints();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = mover.getX();
        int y = mover.getY();

        drawTail(g, x, y);
        drawBody(g, x, y);
        drawEye(g, x, y);
        drawDorsalFin(g, x, y);
        drawVentralFin(g, x, y);

        g.setRenderingHints(originalHints);
    }

    private void drawTail(Graphics2D g, int x, int y) {
        g.setColor(bodyColor);
        int tailWidth = width / 2;
        int[] tailXPoints = {x - tailWidth + 5, x, x - tailWidth + 5};
        int[] tailYPoints = {y, y + height / 2, y + height};
        g.fillPolygon(tailXPoints, tailYPoints, 3);
    }

    private void drawBody(Graphics2D g, int x, int y) {
        g.setColor(bodyColor);
        g.fillOval(x, y, width, height);
    }

    private void drawEye(Graphics2D g, int x, int y) {
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

    private void drawDorsalFin(Graphics2D g, int x, int y) {
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

    private void drawVentralFin(Graphics2D g, int x, int y) {
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
        return mover.getY();
    }

    public Mover getMover() {
        return mover;
    }
}
