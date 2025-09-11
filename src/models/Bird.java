package models;

import java.awt.*;

public class Bird {
    private int x, y;
    private int baseY;
    private int speed;
    private int amplitude;
    private int size;
    private Color color;
    private int phaseOffset;
    private int panelWidth;
    private boolean isFlyingRight;

    public Bird(int startX, int baseY, int speed, int amplitude, int size, Color color) {
        this.x = startX;
        this.baseY = baseY;
        this.y = baseY;
        this.speed = speed;
        this.amplitude = amplitude;
        this.size = size;
        this.color = color;
        this.phaseOffset = (int)(Math.random() * 100);
        this.panelWidth = 800;
        this.isFlyingRight = true;
    }

    public void setPanelWidth(int width) {
        this.panelWidth = width;
    }

    public void update(int ticks) {
        x += isFlyingRight ? speed : -speed;

        double phase = (ticks + phaseOffset) * 0.1;
        y = baseY + (int)(Math.sin(phase) * amplitude);

        checkBounds();
    }

    private void checkBounds() {
        if (x > panelWidth + 50) {
            isFlyingRight = false;
        }
        else if (x < -50) {
            isFlyingRight = true;
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        g.setStroke(new BasicStroke(2));


        if (isFlyingRight) {
            g.drawLine(x, y, x - size/2, y - size/2);
            g.drawLine(x, y, x - size/2, y + size/2);
        } else {
            g.drawLine(x, y, x + size/2, y - size/2);
            g.drawLine(x, y, x + size/2, y + size/2);
        }

        g.fillOval(x - 2, y - 2, 4, 4);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getBaseY() { return baseY; }
    public boolean isFlyingRight() { return isFlyingRight; }

    public boolean isInSky(int horizonY) {
        return y < horizonY;
    }
}
