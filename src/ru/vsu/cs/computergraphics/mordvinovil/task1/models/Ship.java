package ru.vsu.cs.computergraphics.mordvinovil.task1.models;

import ru.vsu.cs.computergraphics.mordvinovil.task1.Mover;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ship {
    private final Mover mover;
    private final int length;
    private final int height;
    private final Color hullColor;
    private final Color superstructureColor;
    private final Color windowColor;

    private final List<float[]> smokeParticles;
    private final Random random;
    private int smokeTimer;

    public Ship(int x, int y, int length, int height,
                Color hullColor, Color superstructureColor, Color windowColor, int speed) {
        this.mover = new Mover(x, y, speed, 0, Mover.MovementType.LINEAR_X);
        this.length = length;
        this.height = height;
        this.hullColor = hullColor;
        this.superstructureColor = superstructureColor;
        this.windowColor = windowColor;

        this.smokeParticles = new ArrayList<>();
        this.random = new Random();
        this.smokeTimer = 0;
    }

    public Ship(Mover mover, int length, int height,
                Color hullColor, Color superstructureColor, Color windowColor) {
        this.mover = mover;
        this.length = length;
        this.height = height;
        this.hullColor = hullColor;
        this.superstructureColor = superstructureColor;
        this.windowColor = windowColor;

        this.smokeParticles = new ArrayList<>();
        this.random = new Random();
        this.smokeTimer = 0;
    }

    public int getX() {
        return mover.getX();
    }

    public int getY() {
        return mover.getY();
    }

    public int getLength() {
        return length;
    }

    public void setY(int y) {
        mover.setY(y);
        mover.setStartY(y);
    }

    public void setPanelWidth(int panelWidth) {
        mover.setPanelWidth(panelWidth);
    }

    public void update() {
        mover.update();
        if (mover.getX() > mover.getPanelWidth()) {
            mover.setX(-length);
        }
        updateSmoke();
    }

    public void draw(Graphics2D g) {
        RenderingHints originalHints = g.getRenderingHints();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = mover.getX();
        int y = mover.getY();

        drawHull(g, x, y);
        drawSuperstructure(g, x, y);
        drawWindows(g, x, y);
        drawFunnel(g, x, y);
        drawSmoke(g, x, y);
        drawShipName(g, x, y);

        g.setRenderingHints(originalHints);
    }

    private void drawHull(Graphics2D g, int x, int y) {
        g.setColor(hullColor);
        int[] hullX = {x - 5, x, x + length - 10, x + length - length / 4, x + length / 4};
        int[] hullY = {y + height / 2, y + 5, y, y + height, y + height};
        g.fillPolygon(hullX, hullY, 5);
        g.fillArc(x - 8, y, 16, height, 90, 90);
    }

    private void drawSuperstructure(Graphics2D g, int x, int y) {
        g.setColor(superstructureColor);
        int superstructureHeight = height / 2;
        int superstructureWidth = length / 2;
        int superstructureX = x + length / 4;
        int superstructureY = y - superstructureHeight + 5;
        g.fillRect(superstructureX, superstructureY, superstructureWidth, superstructureHeight);
    }

    private void drawWindows(Graphics2D g, int x, int y) {
        g.setColor(windowColor);
        int superstructureHeight = height / 2;
        int superstructureWidth = length / 2;
        int superstructureX = x + length / 4;
        int superstructureY = y - superstructureHeight + 5;

        int windowCount = 5;
        int windowWidth = superstructureWidth / (windowCount + 2);
        int windowHeight = superstructureHeight / 2;

        for (int i = 0; i < windowCount; i++) {
            int windowX = superstructureX + (i + 1) * (superstructureWidth / (windowCount + 1)) - windowWidth / 2;
            int windowY = superstructureY + superstructureHeight / 2 - windowHeight / 2;
            g.fillRect(windowX, windowY, windowWidth, windowHeight);
        }
    }

    private void drawFunnel(Graphics2D g, int x, int y) {
        int superstructureHeight = height / 2;
        int superstructureY = y - superstructureHeight + 5;
        int funnelWidth = length / 8;
        int funnelHeight = height / 2;
        int funnelX = x + length / 2;
        int funnelY = superstructureY - funnelHeight;

        g.setColor(new Color(180, 0, 0));
        g.fillRect(funnelX, funnelY, funnelWidth, funnelHeight);
        g.setColor(new Color(200, 0, 0));
        g.fillRect(funnelX - 2, funnelY, funnelWidth + 4, 5);
    }

    private void drawSmoke(Graphics2D g, int x, int y) {
        for (float[] smoke : smokeParticles) {
            float smokeX = smoke[0];
            float smokeY = smoke[1];
            float size = smoke[2];
            int alpha = (int) smoke[3];
            g.setColor(new Color(150, 150, 150, alpha));
            g.fillOval((int) smokeX, (int) smokeY, (int) size, (int) size);
        }
    }

    private void updateSmoke() {
        smokeTimer++;
        if (smokeTimer >= 3) {
            smokeTimer = 0;
            int[] funnelCoords = getFunnelCoordinates(mover.getX(), mover.getY());
            int smokeStartY = funnelCoords[1] - 10;
            int smokeStartX = funnelCoords[0] + funnelCoords[2] / 2;

            int particlesToCreate = random.nextInt(2) + 1;
            for (int i = 0; i < particlesToCreate; i++) {
                int offsetX = random.nextInt(funnelCoords[2]) - funnelCoords[2] / 2;
                float[] smoke = {
                    smokeStartX + offsetX,
                    smokeStartY,
                    random.nextInt(5) + 8,
                    200,
                    random.nextInt(20) + 30
                };
                smokeParticles.add(smoke);
            }
        }

        for (int i = smokeParticles.size() - 1; i >= 0; i--) {
            float[] smoke = smokeParticles.get(i);
            smoke[1] -= 2;
            smoke[0] += random.nextInt(3) - 1;
            smoke[2] += 0.5f;
            smoke[4]--;
            smoke[3] = Math.max(0, smoke[3] - 5);

            if (smoke[4] <= 0 || smoke[2] > 25 || smoke[3] <= 10) {
                smokeParticles.remove(i);
            }
        }
    }

    private int[] getFunnelCoordinates(int shipX, int shipY) {
        int superstructureHeight = height / 2;
        int superstructureY = shipY - superstructureHeight + 5;
        int funnelWidth = length / 8;
        int funnelHeight = height / 2;
        int funnelX = shipX + length / 2;
        int funnelY = superstructureY - funnelHeight;
        return new int[]{funnelX, funnelY, funnelWidth, funnelHeight};
    }

    private void drawShipName(Graphics2D g, int x, int y) {
        int fontSize = Math.max(12, length / 25);
        Font shipFont = new Font("Arial", Font.BOLD, fontSize);
        g.setFont(shipFont);
        g.setColor(Color.BLACK);
        g.drawString("Таск 1", x + length / 4, y + height - 5);
    }

    public int getHeight() {
        return height;
    }

    public Mover getMover() {
        return mover;
    }
}
