import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.*;

public class DrawPanel extends JPanel implements ActionListener {

    private final int PANEL_WIDTH;
    private final int PANEL_HEIGHT;
    private final int TIMER_DELAY;
    private Timer timer;
    private int ticksFromStart = 0;

    private List<Wave> waves;
    private List<Bird> birds;
    private List<Cloud> clouds;
    private List<Fish> fishes;
    private List<Car> cars;
    private Bridge bridge;

    private int horizonY;

    Sun s1 = new Sun(670, 75, 30, 30, 15, Color.ORANGE);
    Ship cruiseShip = new Ship(75, 300, 500, 50,
        Color.BLUE, Color.WHITE, Color.YELLOW, 2);

    public DrawPanel(final int width, final int height, final int timerDelay) {
        this.PANEL_WIDTH = Math.max(width, 1);
        this.PANEL_HEIGHT = Math.max(height, 1);
        this.TIMER_DELAY = timerDelay;
        this.horizonY = (int)(PANEL_HEIGHT * 0.25);

        this.waves = new ArrayList<>();
        this.birds = new ArrayList<>();
        this.clouds = new ArrayList<>();
        this.cars = new ArrayList<>();
        this.fishes = new ArrayList<>();

        timer = new Timer(timerDelay, this);
        timer.start();

        cruiseShip.setPanelWidth(PANEL_WIDTH);

        initializeSea();
        initializeBirds();
        initializeCloud();
        initializeBridge();
        initializeCars();
        initalizeFish();
    }

    private void initializeBridge() {
        int bridgeY = horizonY - 15;
        int bridgeHeight = 5;
        int archHeight = 20;
        bridge = new Bridge(0, bridgeY, PANEL_WIDTH, bridgeHeight,
            new Color(160, 82, 45), archHeight);
    }

    private void initializeCars() {
        Random random = new Random();

        for (int i = 0; i < 2 + random.nextInt(6); i++) {
            int carX = -random.nextInt(300) - 100;
            Car car = Car.createRandom(carX, 0);

            int carY;
            if (car.getType() == Car.CarType.MICROBUS) {
                carY = bridge.getY() - 45;
            } else {
                carY = bridge.getY() - 35;
            }

            car.setY(carY);
            cars.add(car);
        }
    }

    private void initializeSea() {
        Random random = new Random();

        int mainWaveY = cruiseShip.getY() + 20;
        Wave mainWave = new Wave(0, mainWaveY, PANEL_WIDTH, 15, 2);
        waves.add(mainWave);

        int waveCount = 20 + random.nextInt(5);
        for (int i = 0; i < waveCount; i++) {
            int waveHeight = 5 + random.nextInt(15);
            int waveSpeed = 1 + random.nextInt(3);
            int yPosition = horizonY + random.nextInt(PANEL_HEIGHT - horizonY);

            Wave wave = new Wave(0, yPosition, PANEL_WIDTH, waveHeight, waveSpeed);
            wave.setTicksOffset(i * 20 + random.nextInt(30));
            waves.add(wave);
        }
    }

    private void initializeCloud() {
        Random random = new Random();
        int cloudsCount = 3 + random.nextInt(3);
        for (int i = 0; i < cloudsCount; i++) {
            int x = 10 + random.nextInt(550);
            int y = random.nextInt(horizonY - 105);
            Cloud c = Cloud.createRandom(x, y);
            clouds.add(c);
        }
    }

    private void initializeBirds() {
        Random random = new Random();
        int birdCount = 3 + random.nextInt(4);
        for (int i = 0; i < birdCount; i++) {
            int startX = random.nextInt(PANEL_WIDTH);
            int baseY = 30 + random.nextInt(horizonY - 50);
            int speed = 2 + random.nextInt(3);
            int amplitude = 3 + random.nextInt(4);
            int size = 10 + random.nextInt(6);

            Bird bird = new Bird(startX, baseY, speed, amplitude, size, Color.BLACK);
            bird.setPanelWidth(PANEL_WIDTH);
            birds.add(bird);
        }
    }

    private void initalizeFish() {
        Random random = new Random();
        int fishCount = 3 + random.nextInt(3);
        for (int i = 0; i < fishCount; i++) {
            int x = random.nextInt(PANEL_WIDTH);
            int y = horizonY + 60 + random.nextInt(PANEL_HEIGHT - horizonY - 40);
            int width = 25 + random.nextInt(10);
            int height = 8 + random.nextInt(8);
            int speed = 1 + random.nextInt(2);

            Fish fish = new Fish(x, y, width, height, speed);
            fish.setPanelWidth(PANEL_WIDTH);
            fish.setPanelHeight(PANEL_HEIGHT);
            fish.setHorizonY(horizonY);
            fishes.add(fish);
        }
    }

    @Override
    public void paint(final Graphics gr) {
        super.paint(gr);
        Graphics2D g = (Graphics2D) gr;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawSky(g);
        s1.draw(g);
        bridge.draw(g);

        g.setColor(new Color(0, 105, 148));
        g.fillRect(0, horizonY, PANEL_WIDTH, PANEL_HEIGHT - horizonY);

        for (Wave wave : waves) {
            wave.update(ticksFromStart);
            wave.draw(g);
        }

        cruiseShip.update();
        adjustShipToWaves();
        cruiseShip.draw(g);

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
            car.update(PANEL_WIDTH);
            car.draw(g);
        }



        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        for (int i = 0; i < waves.size() / 2; i++) {
            Wave wave = waves.get(i);
            wave.update(ticksFromStart + 10);
            g.setColor(new Color(255, 255, 255, 50));
            wave.draw(g);
        }
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        for (Fish fish : fishes) {
            fish.update();
            fish.draw(g);
        }
    }

    private void adjustShipToWaves() {
        int shipX = cruiseShip.getX() + cruiseShip.getLength() / 2;
        int lowestWaveY = PANEL_HEIGHT;

        for (Wave wave : waves) {
            int waveY = wave.getWaveHeightAt(shipX, ticksFromStart);
            if (waveY < lowestWaveY) {
                lowestWaveY = waveY;
            }
        }
        cruiseShip.setY(lowestWaveY - 10);
    }

    private void drawSky(Graphics2D g) {
        g.setColor(new Color(135, 206, 235));
        g.fillRect(0, 0, PANEL_WIDTH, horizonY);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == timer) {
            repaint();
            ++ticksFromStart;
        }
    }
}
