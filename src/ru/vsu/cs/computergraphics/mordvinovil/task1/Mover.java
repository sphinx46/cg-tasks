package ru.vsu.cs.computergraphics.mordvinovil.task1;

import java.util.Random;

public class Mover {
    public enum MovementType {
        LINEAR_X,
        SINUSOIDAL_Y,
        WAVE,
        FISH_SWIM
    }

    private int x;
    private int y;
    private int startY;
    private final int startX;
    private final int speed;
    private final int amplitude;
    private int panelHeight;
    private int panelWidth;
    private final MovementType movementType;
    private boolean isMovingRight;
    private final Random random;
    private int ticks;

    public Mover(int x, int y, int speed, int amplitude, MovementType movementType) {
        this(x, y, speed, amplitude, 0, 0, movementType);
    }

    public Mover(int x, int y, int speed, int amplitude, int panelWidth, int panelHeight, MovementType movementType) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.speed = speed;
        this.amplitude = amplitude;
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.movementType = movementType;
        this.isMovingRight = speed > 0;
        this.random = new Random();
        this.ticks = 0;
    }

    public void update() {
        ticks++;
        updatePosition();
        checkBounds();
    }

    public void update(int customTicks) {
        this.ticks = customTicks;
        updatePosition();
        checkBounds();
    }

    private void updatePosition() {
        switch (movementType) {
            case LINEAR_X:
                updateLinearX();
                break;
            case SINUSOIDAL_Y:
                updateSinusoidalY();
                break;
            case WAVE:
                updateWave();
                break;
            case FISH_SWIM:
                updateFishSwim();
                break;
        }
    }

    private void updateLinearX() {
        x += speed;
    }

    private void updateSinusoidalY() {
        x += speed;
        y = startY + (int)(Math.sin(ticks * 0.1) * amplitude);
    }

    private void updateWave() {
        x += speed;
        double phase = (x + ticks * speed) * 0.05;
        y = startY + (int)(Math.sin(phase) * amplitude);
    }

    private void updateFishSwim() {
        x += speed;

        if (random.nextInt(320) == 0) {
            double yMovement = (random.nextDouble() - 0.1) * amplitude;
            y = startY + (int)yMovement;
        } else if (random.nextInt(50) == 0) {
            int deltaY = startY - y;
            y += deltaY / 25;
        }
    }

    private void checkBounds() {
        if (movementType == MovementType.WAVE || movementType == MovementType.SINUSOIDAL_Y ||
            movementType == MovementType.FISH_SWIM) {
            if (x > panelWidth + 100) {
                x = -100;
            } else if (x < -100) {
                x = panelWidth + 100;
            }
        } else {
            if (x > panelWidth + 50) {
                isMovingRight = false;
            } else if (x < -50) {
                isMovingRight = true;
            }

            if (isMovingRight) {
                x += Math.abs(speed);
            } else {
                x -= Math.abs(speed);
            }
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getPanelWidth() {
        return panelWidth;
    }

    public void setPanelWidth(int panelWidth) {
        this.panelWidth = panelWidth;
    }

    public void setPanelDimensions(int width, int height) {
        this.panelWidth = width;
        this.panelHeight = height;
    }

    public boolean isMovingRight() {
        return isMovingRight;
    }

    public int getStartX() {
        return startX;
    }
}
