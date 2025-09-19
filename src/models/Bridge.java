package models;

import java.awt.*;
import java.awt.geom.QuadCurve2D;

public class Bridge {
    private final int x, height;
    private int y;
    private int  width;
    private final Color color;
    private final int archHeight;
    private int pillarCount;
    private int pillarSpacing;

    public Bridge(int x, int y, int width, int height, Color color, int archHeight) {
        this.x = x;
        this.y = y - 30;
        this.width = width;
        this.height = height;
        this.color = color;
        this.archHeight = archHeight;
        this.pillarCount = calculatePillarCount();
        this.pillarSpacing = width / pillarCount;
        updatePillars();
    }

    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y - 30;
    }

    public void setWidth(int width) {
        updatePillars();
        this.width = width;
    }

    public void updatePillars() {
        this.pillarCount = calculatePillarCount();
        this.pillarSpacing = width / pillarCount;
    }

    private int calculatePillarCount() {
        return Math.max(width / 50, 3);
    }

    public void draw(Graphics2D g) {
        saveRenderingHints(g);

        drawBridgeBase(g);
        drawArch(g);
        drawRoad(g);
        drawPillars(g);
        drawRailings(g);

    }

    private void saveRenderingHints(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private void drawBridgeBase(Graphics2D g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    private void drawArch(Graphics2D g) {
        QuadCurve2D arch = new QuadCurve2D.Float(
            x, y + height,
            x + (float) width / 2, y - archHeight,
            x + width, y + height
        );
        g.fill(arch);
    }

    private void drawRoad(Graphics2D g) {
        g.setColor(color.darker());
        QuadCurve2D road = new QuadCurve2D.Float(
            x, y + height - 5,
            x + (float) width / 2, y - archHeight + 5,
            x + width, y + height - 5
        );
        g.fill(road);
    }

    private void drawPillars(Graphics2D g) {
        g.setColor(new Color(80, 80, 80));

        for (int i = 1; i < pillarCount - 1; i++) {
            int pillarX = x + i * pillarSpacing;
            int pillarY = y + height;
            g.fillRect(pillarX - 5, pillarY, 10, 60);
        }
    }

    private void drawRailings(Graphics2D g) {
        drawMainRails(g);
        drawVerticalRailSupports(g);
    }

    private void drawMainRails(Graphics2D g) {
        g.setColor(new Color(180, 180, 180));
        g.setStroke(new BasicStroke(3));

        QuadCurve2D topRail = new QuadCurve2D.Float(
            x, y + height - 15,
            x + (float) width / 2, y - archHeight - 10,
            x + width, y + height - 15
        );
        g.draw(topRail);

        QuadCurve2D bottomRail = new QuadCurve2D.Float(
            x, y + height - 5,
            x + (float) width / 2, y - archHeight,
            x + width, y + height - 5
        );
        g.draw(bottomRail);
    }

    private void drawVerticalRailSupports(Graphics2D g) {
        for (int i = 1; i < pillarCount; i++) {
            int pillarX = x + i * pillarSpacing;
            double progress = (double) i / pillarCount;
            int railY = calculateRailYPosition(progress);

            g.drawLine(pillarX, railY + height - 15, pillarX, railY + height - 5);
        }
    }

    private int calculateRailYPosition(double progress) {
        return (int) (y + height - archHeight * 4 * progress * (1 - progress));
    }
}
