package models;

import java.awt.*;

public class Wave {
    private int x;
    private int y;
    private int width;
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

    public void setWidth(int width) {
        this.width = Math.max(width, 1);
    }

    public void update(int ticks) {
        if (width != 0) {
            x = (-width + ((ticks + ticksOffset) * speed));
        }
    }

    public int getWaveHeightAt(int pointX, int ticks) {
        double phase = (double)(pointX + (ticks + ticksOffset) * speed) / 30.0;
        return y - (int)(Math.sin(phase) * height / 2);
    }

    public void draw(Graphics2D g) {
        g.setColor(waveColor);

        Polygon wavePolygon = new Polygon();

        int step = Math.max(5, width / 200);
        for (int i = 0; i <= width; i += step) {
            int waveX = x + i;
            double phase = (double)(waveX + ticksOffset * speed) / 30.0;
            int waveY = y + (int)(Math.sin(phase) * height / 2);
            wavePolygon.addPoint(waveX, waveY);
        }

        wavePolygon.addPoint(x + width, y + height * 2);
        wavePolygon.addPoint(x, y + height * 2);

        g.fillPolygon(wavePolygon);
    }

    public void setTicksOffset(int offset) {
        this.ticksOffset = offset;
    }
}
