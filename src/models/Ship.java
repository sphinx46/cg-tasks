package models;

import java.awt.*;

public class Ship {
    private int x, y, length, height;
    private Color hullColor, superstructureColor, windowColor;
    private int speed;
    private int panelWidth;

    public int getY() {
        return y;
    }

    public Ship(int x, int y, int length, int height,
                Color hullColor, Color superstructureColor, Color windowColor, int speed) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.height = height;
        this.hullColor = hullColor;
        this.superstructureColor = superstructureColor;
        this.windowColor = windowColor;
        this.speed = speed;
    }

    public void setPanelWidth(int panelWidth) {
        this.panelWidth = panelWidth;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getX() {
        return x;
    }

    public int getLength() {
        return length;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void update() {
        x += speed;
        if (x > panelWidth) {
            x = -length;
        }
    }


    public void draw(Graphics2D g) {
        RenderingHints originalHints = g.getRenderingHints();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(hullColor);

        int[] hullX = {x - 5, x, x + length - 10, x + length - length/4, x + length/4};
        int[] hullY = {y + height/2, y + 5, y, y + height, y + height};

        g.fillPolygon(hullX, hullY, 5);

        g.fillArc(x - 8, y, 16, height, 90, 90);

        g.setColor(superstructureColor);
        int superstructureHeight = height / 2;
        int superstructureWidth = length / 2;
        int superstructureX = x + length/4;

        int superstructureY = y - superstructureHeight + 5;

        g.fillRect(superstructureX, superstructureY,
            superstructureWidth, superstructureHeight);

        g.setColor(windowColor);
        int windowCount = 5;
        int windowWidth = superstructureWidth / (windowCount + 2);
        int windowHeight = superstructureHeight / 2;

        for (int i = 0; i < windowCount; i++) {
            int windowX = superstructureX + (i + 1) * (superstructureWidth / (windowCount + 1)) - windowWidth/2;
            int windowY = superstructureY + superstructureHeight / 2 - windowHeight/2;
            g.fillRect(windowX, windowY, windowWidth, windowHeight);
        }

        g.setColor(new Color(180, 0,  0));
        int funnelWidth = length / 8;
        int funnelHeight = height / 2;
        int funnelX = x + length/2;
        int funnelY = superstructureY - funnelHeight;

        g.fillRect(funnelX, funnelY, funnelWidth, funnelHeight);

        g.setColor(new Color(200, 0, 0));
        g.fillRect(funnelX - 2, funnelY, funnelWidth + 4, 5);

        g.setRenderingHints(originalHints);
    }
}
