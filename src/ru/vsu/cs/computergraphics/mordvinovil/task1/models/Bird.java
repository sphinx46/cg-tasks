package ru.vsu.cs.computergraphics.mordvinovil.task1.models;

import ru.vsu.cs.computergraphics.mordvinovil.task1.Mover;
import java.awt.*;

public class Bird {
    private final Mover mover;
    private final int size;
    private final Color color;

    public Bird(Mover mover, int size, Color color) {
        this.mover = mover;
        this.size = size;
        this.color = color;
    }

    public Bird(int startX, int baseY, int speed, int amplitude, int size, Color color) {
        this.mover = new Mover(startX, baseY, speed, amplitude, Mover.MovementType.SINUSOIDAL_Y);
        this.size = size;
        this.color = color;
    }

    public void setPanelWidth(int width) {
        mover.setPanelWidth(width);
    }

    public void update(int ticks) {
        mover.update(ticks);
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        g.setStroke(new BasicStroke(2));

        int x = mover.getX();
        int y = mover.getY();
        boolean isMovingRight = mover.isMovingRight();

        if (isMovingRight) {
            g.drawLine(x, y, x - size/2, y - size/2);
            g.drawLine(x, y, x - size/2, y + size/2);
        } else {
            g.drawLine(x, y, x + size/2, y - size/2);
            g.drawLine(x, y, x + size/2, y + size/2);
        }

        g.fillOval(x - 2, y - 2, 4, 4);
    }

    public int getY() { return mover.getY(); }
    public Mover getMover() { return mover; }
}
