import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.*;

public class DrawPanel extends JPanel implements ActionListener {

    private static final Color SKY_COLOR = new Color(135, 206, 235);
    private static final Color SEA_COLOR = new Color(0, 105, 148);
    private static final Color WAVE_REFLECTION_COLOR = new Color(255, 255, 255, 50);
    private static final float WAVE_REFLECTION_ALPHA = 0.4f;

    private final int panelWidth;
    private final int panelHeight;
    private final Timer timer;
    private int ticksFromStart = 0;
    private final int horizonY;

    private final List<Wave> waves = new ArrayList<>();
    private final List<Bird> birds = new ArrayList<>();
    private final List<Cloud> clouds = new ArrayList<>();
    private final List<Fish> fishes = new ArrayList<>();
    private final List<Car> cars = new ArrayList<>();

    private final Bridge bridge;
    private final Sun sun;
    private final Ship cruiseShip;

    public DrawPanel(final int width, final int height, final int timerDelay) {
        this.panelWidth = Math.max(width, 1);
        this.panelHeight = Math.max(height, 1);
        this.horizonY = (int)(panelHeight * 0.25);

        this.sun = new Sun(670, 75, 30, 30, 15, Color.ORANGE);
        this.cruiseShip = new Ship(75, 300, 500, 50,
            Color.BLUE, Color.WHITE, Color.YELLOW, 2);
        this.bridge = createBridge();

        cruiseShip.setPanelWidth(panelWidth);

        initializeScene();

        this.timer = new Timer(timerDelay, this);
        timer.start();
    }

    private Bridge createBridge() {
        int bridgeY = horizonY - 15;
        int bridgeHeight = 5;
        int archHeight = 20;
        return new Bridge(0, bridgeY, panelWidth, bridgeHeight,
            new Color(160, 82, 45), archHeight);
    }

    private void initializeScene() {
        initializeSea();
        initializeBirds();
        initializeClouds();
        initializeCars();
        initializeFish();
    }

    private void initializeSea() {
        Random random = new Random();

        Wave mainWave = new Wave(0, cruiseShip.getY() + 20, panelWidth, 15, 2);
        waves.add(mainWave);

        int waveCount = 20 + random.nextInt(5);
        for (int i = 0; i < waveCount; i++) {
            int waveHeight = 5 + random.nextInt(15);
            int waveSpeed = 1 + random.nextInt(3);
            int yPosition = horizonY + random.nextInt(panelHeight - horizonY);

            Wave wave = new Wave(0, yPosition, panelWidth, waveHeight, waveSpeed);
            wave.setTicksOffset(i * 20 + random.nextInt(30));
            waves.add(wave);
        }
    }

    private void initializeClouds() {
        Random random = new Random();
        int cloudsCount = 3 + random.nextInt(3);
        for (int i = 0; i < cloudsCount; i++) {
            int x = 10 + random.nextInt(550);
            int y = random.nextInt(horizonY - 105);
            clouds.add(Cloud.createRandom(x, y));
        }
    }

    private void initializeBirds() {
        Random random = new Random();
        int birdCount = 3 + random.nextInt(4);
        for (int i = 0; i < birdCount; i++) {
            int startX = random.nextInt(panelWidth);
            int baseY = 30 + random.nextInt(horizonY - 50);
            int speed = 2 + random.nextInt(3);
            int amplitude = 3 + random.nextInt(4);
            int size = 10 + random.nextInt(6);

            Bird bird = new Bird(startX, baseY, speed, amplitude, size, Color.BLACK);
            bird.setPanelWidth(panelWidth);
            birds.add(bird);
        }
    }

    private void initializeCars() {
        Random random = new Random();
        int carCount = 2 + random.nextInt(6);

        for (int i = 0; i < carCount; i++) {
            int carX = -random.nextInt(300) - 100;
            Car car = Car.createRandom(carX, 0);

            int carY = bridge.getY() - (car.getType() == Car.CarType.MICROBUS ? 45 : 35);
            car.setY(carY);

            cars.add(car);
        }
    }

    private void initializeFish() {
        Random random = new Random();
        int fishCount = 3 + random.nextInt(3);

        for (int i = 0; i < fishCount; i++) {
            int x = random.nextInt(panelWidth);
            int y = horizonY + 60 + random.nextInt(panelHeight - horizonY - 100);
            int width = 25 + random.nextInt(10);
            int height = 8 + random.nextInt(8);
            int speed = 1 + random.nextInt(2);

            Fish fish = new Fish(x, y, width, height, speed);
            fish.setPanelWidth(panelWidth);
            fish.setPanelHeight(panelHeight);
            fish.setHorizonY(horizonY);

            fishes.add(fish);
        }
    }

    @Override
    public void paint(final Graphics gr) {
        super.paint(gr);
        Graphics2D g = (Graphics2D) gr;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBackground(g);
        drawSunAndBridge(g);
        drawSeaAndWaves(g);
        drawMovingObjects(g);
        drawWaveReflections(g);
        drawFish(g);
    }

    private void drawBackground(Graphics2D g) {
        g.setColor(SKY_COLOR);
        g.fillRect(0, 0, panelWidth, horizonY);

        g.setColor(SEA_COLOR);
        g.fillRect(0, horizonY, panelWidth, panelHeight - horizonY);
    }

    private void drawSunAndBridge(Graphics2D g) {
        sun.draw(g);
        bridge.draw(g);
    }

    private void drawSeaAndWaves(Graphics2D g) {
        for (Wave wave : waves) {
            wave.update(ticksFromStart);
            wave.draw(g);
        }

        cruiseShip.update();
        adjustShipToWaves();
        cruiseShip.draw(g);
    }

    private void drawMovingObjects(Graphics2D g) {
        for (Cloud cloud : clouds) {
            cloud.draw(g);
        }

        for (Bird bird : birds) {
            bird.update(ticksFromStart);
            if (bird.getY() < horizonY) {
                bird.draw(g);
            }
        }

        for (Car car : cars) {
            car.update(panelWidth);
            car.draw(g);
        }
    }

    private void drawWaveReflections(Graphics2D g) {
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, WAVE_REFLECTION_ALPHA));
        g.setColor(WAVE_REFLECTION_COLOR);

        for (int i = 0; i < waves.size() / 2; i++) {
            Wave wave = waves.get(i);
            wave.update(ticksFromStart + 10);
            wave.draw(g);
        }

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    private void drawFish(Graphics2D g) {
        for (Fish fish : fishes) {
            fish.update();
            fish.draw(g);
        }
    }

    private void adjustShipToWaves() {
        int shipX = cruiseShip.getX() + cruiseShip.getLength() / 2;
        int lowestWaveY = panelHeight;

        for (Wave wave : waves) {
            int waveY = wave.getWaveHeightAt(shipX, ticksFromStart);
            if (waveY < lowestWaveY) {
                lowestWaveY = waveY;
            }
        }
        cruiseShip.setY(lowestWaveY - 10);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == timer) {
            repaint();
            ticksFromStart++;
        }
    }
}
