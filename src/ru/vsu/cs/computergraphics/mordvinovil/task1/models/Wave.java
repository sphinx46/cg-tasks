package ru.vsu.cs.computergraphics.mordvinovil.task1.models;

import ru.vsu.cs.computergraphics.mordvinovil.task1.Mover;

import java.awt.*;

public class Wave {
    private final Mover mover;
    private final int width;
    private final int height;
    private final Color waveColor;
    private final int ticksOffset;
    private final double waveFrequency;
    private final double wavePhase;

    public Wave(int x, int y, int width, int height, int speed) {
        this.width = Math.max(width, 1);
        this.height = height;
        this.waveColor = new Color(0, 105, 148 + (int) (Math.random() * 40));
        this.ticksOffset = (int) (Math.random() * 100);
        this.waveFrequency = 0.02 + Math.random() * 0.01;
        this.wavePhase = Math.random() * Math.PI * 2;
        this.mover = new Mover(x, y, speed, 0, height / 2, 0, Mover.MovementType.WAVE);
    }

    public Wave(Mover mover, int width, int height) {
        this.mover = mover;
        this.width = Math.max(width, 1);
        this.height = height;
        this.waveColor = new Color(0, 105, 148 + (int) (Math.random() * 40));
        this.ticksOffset = (int) (Math.random() * 100);
        this.waveFrequency = 0.02 + Math.random() * 0.01;
        this.wavePhase = Math.random() * Math.PI * 2;
    }

    public void setPanelWidth(int panelWidth) {
        mover.setPanelWidth(panelWidth);
    }

    public void update(int ticks) {
        mover.update(ticks);
    }

    public int getWaveHeightAt(int pointX, int ticks) {
        double phase = wavePhase + pointX * waveFrequency + (ticks + ticksOffset) * 0.05;
        return mover.getStartY() - (int) (Math.sin(phase) * height / 2);
    }

    public void draw(Graphics2D g) {
        g.setColor(waveColor);
        int x = mover.getX();
        int y = mover.getY();

        int waveLength = width * 2;
        int startX = x - width / 2;

        for (int offset = -waveLength; offset <= waveLength; offset += waveLength) {
            Polygon wavePolygon = new Polygon();
            int step = Math.max(3, waveLength / 200);

            for (int i = 0; i <= waveLength; i += step) {
                int waveX = startX + i + offset;
                double phase = wavePhase + waveX * waveFrequency + ticksOffset * 0.05;
                int waveY = y + (int) (Math.sin(phase) * height / 2);
                wavePolygon.addPoint(waveX, waveY);
            }

            wavePolygon.addPoint(startX + waveLength + offset, y + height * 2);
            wavePolygon.addPoint(startX + offset, y + height * 2);
            g.fillPolygon(wavePolygon);
        }
    }

    public int getX() {
        return mover.getX();
    }

    public int getY() {
        return mover.getY();
    }
}
