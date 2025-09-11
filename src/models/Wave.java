package models;

import java.awt.*;

public class Wave {
    private int x;
    private final int y;
    private final int width;
    private final int height;
    private final int speed;
    private final Color waveColor;
    private int ticksOffset;

    public Wave(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = Math.max(width, 1);
        this.height = height;
        this.speed = speed;
        this.waveColor = new Color(0, 105, 148 + (int)(Math.random() * 40));
        this.ticksOffset = 0;
    }

    public void update(int ticks) {
        if (width * 2 != 0) {
            x = (-width + ((ticks + ticksOffset) * speed) % (width * 2));
        }
    }

    public int getWaveHeightAt(int pointX, int ticks) {
        double phase = (double)(pointX + (ticks + ticksOffset) * speed) / 30.0;
        return y - (int)(Math.sin(phase) * height / 2);
    }

    public void draw(Graphics2D g) {
        g.setColor(waveColor);

        Polygon wavePolygon = new Polygon();

        for (int i = 0; i <= width; i += 5) {
            int waveX = x + i;
            double phase = (double)(waveX + ticksOffset * speed) / 30.0;
            int waveY = y - (int)(Math.sin(phase) * height / 2);
            wavePolygon.addPoint(waveX, waveY);
        }

        wavePolygon.addPoint(x + width, y + height);
        wavePolygon.addPoint(x, y + height);

        g.fillPolygon(wavePolygon);
    }

    public void setTicksOffset(int offset) {
        this.ticksOffset = offset;
    }
}
