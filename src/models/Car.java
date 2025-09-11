package models;

import java.awt.*;
import java.util.Random;

public class Car {
    private int x, y, width, height;
    private Color bodyColor;
    private Color borderColor;
    private int borderWidth;
    private CarType type;

    public Car(int x, int y, int width, int height, Color bodyColor, CarType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bodyColor = bodyColor;
        this.type = type;
        this.borderColor = Color.BLACK;
        this.borderWidth = 2;
    }

    public Car(int x, int y, int width, int height, Color bodyColor, CarType type,
               Color borderColor, int borderWidth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bodyColor = bodyColor;
        this.type = type;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
    }

    public void draw(Graphics2D g) {
        Stroke originalStroke = g.getStroke();
        Color originalColor = g.getColor();

        g.setStroke(new BasicStroke(borderWidth));
        g.setColor(borderColor);

        drawBody(g);

        drawWheels(g);

        drawDetails(g);

        g.setStroke(originalStroke);
        g.setColor(originalColor);
    }

    private void drawBody(Graphics2D g) {
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

    private void drawWheels(Graphics2D g) {
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

    private void drawDetails(Graphics2D g) {
        if (type == CarType.SEDAN) {
            g.setColor(new Color(200, 230, 255));
            g.fillRect(x + width / 4 + 2, y + 2, width / 2 - 4, height / 2 - 4);
            g.setColor(borderColor);
            g.drawRect(x + width / 4 + 2, y + 2, width / 2 - 4, height / 2 - 4);

        } else if (type == CarType.MICROBUS) {
            g.setColor(new Color(200, 230, 255));
            g.fillRect(x + 2, y + 2, width / 2 - 4, height / 2 - 4);
            g.setColor(borderColor);
            g.drawRect(x + 2, y + 2, width / 2 - 4, height / 2 - 4);
        }
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public enum CarType {
        SEDAN(60, 30, Color.RED, Color.BLACK, 2),
        MICROBUS(75, 40, Color.BLUE, Color.BLACK, 2);

        private final int width;
        private final int height;
        private final Color defaultColor;
        private final Color borderColor;
        private final int borderWidth;

        CarType(int width, int height, Color defaultColor, Color borderColor, int borderWidth) {
            this.width = width;
            this.height = height;
            this.defaultColor = defaultColor;
            this.borderColor = borderColor;
            this.borderWidth = borderWidth;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public Color getDefaultColor() {
            return defaultColor;
        }

        public Color getBorderColor() {
            return borderColor;
        }

        public int getBorderWidth() {
            return borderWidth;
        }

        public Car createAt(int x, int y) {
            return new Car(x, y, width, height, defaultColor, this, borderColor, borderWidth);
        }

        public Car createAt(int x, int y, Color customColor) {
            return new Car(x, y, width, height, customColor, this, borderColor, borderWidth);
        }

        public static CarType getRandom() {
            Random random = new Random();
            CarType[] types = values();
            return types[random.nextInt(types.length)];
        }
    }

    public static Car createSedan(int x, int y) {
        return CarType.SEDAN.createAt(x, y);
    }

    public static Car createJeep(int x, int y) {
        return CarType.MICROBUS.createAt(x, y);
    }

    public static Car createRandom(int x, int y) {
        Random random = new Random();

        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        return CarType.getRandom().createAt(x, y, new Color(r, g, b));
    }
}
