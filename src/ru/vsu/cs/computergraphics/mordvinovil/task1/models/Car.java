package ru.vsu.cs.computergraphics.mordvinovil.task1.models;

import ru.vsu.cs.computergraphics.mordvinovil.task1.Mover;

import java.awt.*;
import java.util.Random;

public class Car {
    private final Mover mover;
    private final int width, height;
    private Color bodyColor;
    private final Color borderColor;
    private final int borderWidth;
    private final CarType type;

    public Car(int x, int y, int width, int height, Color bodyColor, CarType type) {
        this.mover = new Mover(x, y, 1 + new Random().nextInt(5), 0, Mover.MovementType.LINEAR_X);
        this.width = width;
        this.height = height;
        this.bodyColor = bodyColor;
        this.type = type;
        this.borderColor = Color.BLACK;
        this.borderWidth = 2;
    }

    public Car(Mover mover, int width, int height, Color bodyColor, CarType type) {
        this.mover = mover;
        this.width = width;
        this.height = height;
        this.bodyColor = bodyColor;
        this.type = type;
        this.borderColor = Color.BLACK;
        this.borderWidth = 2;
    }

    public void setY(int y) {
        mover.setY(y);
        mover.setStartY(y);
    }

    public void setPanelWidth(int panelWidth) {
        mover.setPanelWidth(panelWidth);
    }

    public CarType getType() {
        return type;
    }

    public void update() {
        mover.update();
        if (mover.getX() > mover.getPanelWidth()) {
            mover.setX(-width - new Random().nextInt(350));
        }
    }

    public void draw(Graphics2D g) {
        Stroke originalStroke = g.getStroke();
        Color originalColor = g.getColor();

        g.setStroke(new BasicStroke(borderWidth));
        g.setColor(borderColor);

        int x = mover.getX();
        int y = mover.getY();

        drawBody(g, x, y);
        drawWheels(g, x, y);
        drawDetails(g, x, y);

        g.setStroke(originalStroke);
        g.setColor(originalColor);
    }

    private void drawBody(Graphics2D g, int x, int y) {
        g.setColor(bodyColor);
        if (type == CarType.SEDAN) {
            g.fillRect(x, y + height / 3, width, height * 2 / 3);
            g.fillRect(x + width / 4, y, width / 2, height / 2);
            g.setColor(borderColor);
            g.drawRect(x, y + height / 3, width, height * 2 / 3);
            g.drawRect(x + width / 4, y, width / 2, height / 2);
        } else if (type == CarType.MICROBUS) {
            g.fillRect(x, y, width, height);
            g.setColor(borderColor);
            g.drawRect(x, y, width, height);
        }
    }

    private void drawWheels(Graphics2D g, int x, int y) {
        int wheelDiameter = height / 3;
        int wheelY = y + height - wheelDiameter / 2;

        g.setColor(Color.BLACK);
        g.fillOval(x + width / 6 - wheelDiameter / 2, wheelY, wheelDiameter, wheelDiameter);
        g.setColor(borderColor);
        g.drawOval(x + width / 6 - wheelDiameter / 2, wheelY, wheelDiameter, wheelDiameter);

        g.setColor(Color.BLACK);
        g.fillOval(x + width * 5 / 6 - wheelDiameter / 2, wheelY, wheelDiameter, wheelDiameter);
        g.setColor(borderColor);
        g.drawOval(x + width * 5 / 6 - wheelDiameter / 2, wheelY, wheelDiameter, wheelDiameter);
    }

    private void drawDetails(Graphics2D g, int x, int y) {
        g.setColor(new Color(200, 230, 255));
        if (type == CarType.SEDAN) {
            g.fillRect(x + width / 4 + 2, y + 2, width / 2 - 4, height / 2 - 4);
            g.setColor(borderColor);
            g.drawRect(x + width / 4 + 2, y + 2, width / 2 - 4, height / 2 - 4);
        } else if (type == CarType.MICROBUS) {
            g.fillRect(x + width / 2 + 2, y + 2, width / 2 - 4, height / 2 - 4);
            g.setColor(borderColor);
            g.drawRect(x + width / 2 + 2, y + 2, width / 2 - 4, height / 2 - 4);
        }
    }

    public enum CarType {
        SEDAN(60, 30, Color.RED),
        MICROBUS(75, 40, Color.BLUE);

        private final int width;
        private final int height;
        private final Color defaultColor;

        CarType(int width, int height, Color defaultColor) {
            this.width = width;
            this.height = height;
            this.defaultColor = defaultColor;
        }

        public Car createAt(int x, int y) {
            return new Car(x, y, width, height, defaultColor, this);
        }

        public static CarType getRandom() {
            CarType[] types = values();
            return types[new Random().nextInt(types.length)];
        }
    }

    public static Car createRandom(int x, int y) {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        Car car = CarType.getRandom().createAt(x, y);
        car.bodyColor = new Color(r, g, b);
        return car;
    }

    public int getX() {
        return mover.getX();
    }

    public int getY() {
        return mover.getY();
    }

    public Mover getMover() {
        return mover;
    }
}
