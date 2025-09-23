package ru.vsu.cs.computergraphics.mordvinovil.task1.models;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    private static final Color SKY_COLOR = new Color(135, 206, 235);
    private static final Color SEA_COLOR = new Color(0, 105, 148);
    private static final Color WAVE_REFLECTION_COLOR = new Color(255, 255, 255, 50);
    private static final float WAVE_REFLECTION_ALPHA = 0.4f;
    private static final int DEFAULT_WAVE_COUNT = 20;
    private static final int RANDOM_WAVE_COUNT = 5;
    private static final int MIN_SIZE_THRESHOLD = 50;

    private int width;
    private int height;
    private int horizonY;
    private int lastWidth = 0;
    private int lastHeight = 0;
    private int ticksFromStart = 0;

    private final Sun sun;
    private Bridge bridge;
    private Ship cruiseShip;
    private final List<Wave> waves = new ArrayList<>();
    private final List<Bird> birds = new ArrayList<>();
    private final List<Cloud> clouds = new ArrayList<>();
    private final List<Fish> fishes = new ArrayList<>();
    private final List<Car> cars = new ArrayList<>();
    private final Random random = new Random();

    private boolean initialized = false;

    public World() {
        this.sun = new Sun(30, 30, 15, Color.ORANGE);
    }

    public void update(int width, int height) {
        this.width = Math.max(width, 1);
        this.height = Math.max(height, 1);

        if (!initialized) {
            initialize();
            initialized = true;
        } else {
            updateDimensions();
        }

        updateMovingObjects();
        ticksFromStart++;
    }

    public void draw(Graphics2D g) {
        drawBackground(g);
        drawStaticObjects(g);
        drawSeaEnvironment(g);
        drawMovingObjects(g);
        drawWaveReflections(g);
    }

    private void initialize() {
        this.horizonY = (int)(height * 0.25);
        initializeAllEntities();
        lastWidth = width;
        lastHeight = height;
    }

    private void updateDimensions() {
        this.horizonY = (int)(height * 0.25);

        boolean sizeChanged = hasSizeChangedSignificantly(width, height);
        updateAllObjectsDimensions(width, height, sizeChanged);

        lastWidth = width;
        lastHeight = height;
    }

    private boolean hasSizeChangedSignificantly(int width, int height) {
        return Math.abs(width - lastWidth) > MIN_SIZE_THRESHOLD ||
            Math.abs(height - lastHeight) > MIN_SIZE_THRESHOLD;
    }

    private void updateAllObjectsDimensions(int width, int height, boolean sizeChanged) {
        sun.setPanelDimensions(width, height);

        if (cruiseShip != null) {
            cruiseShip.setPanelWidth(width);
            cruiseShip.setY(horizonY + 30);
        }

        updateBridgeDimensions(width);

        if (sizeChanged) {
            initializeSea();
        }

        updateObjectsWidth(waves, wave -> wave.setWidth(width));
        updateObjectsWidth(birds, bird -> bird.setPanelWidth(width));
        updateObjectsWidth(cars, car -> car.setPanelWidth(width));

        updateFishesDimensions(width, height);
    }

    private <T> void updateObjectsWidth(List<T> objects, WidthUpdater<T> updater) {
        for (T obj : objects) {
            updater.updateWidth(obj);
        }
    }

    private interface WidthUpdater<T> {
        void updateWidth(T obj);
    }

    private void updateBridgeDimensions(int width) {
        if (bridge != null) {
            bridge.setWidth(width);
            bridge.setY(horizonY - 15);
            updateCarPositions();
        }
    }

    private void updateFishesDimensions(int width, int height) {
        for (Fish fish : fishes) {
            fish.setPanelWidth(width);
            fish.setPanelHeight(height);
            fish.setHorizonY(horizonY);
        }
    }

    private void updateCarPositions() {
        for (Car car : cars) {
            int carY = bridge.getY() - (car.getType() == Car.CarType.MICROBUS ? 45 : 35);
            car.setY(carY);
        }
    }

    private void initializeAllEntities() {
        initializeClouds();
        initializeCruiseShip();
        initializeBridge();
        initializeSea();
        initializeBirds();
        initializeCars();
        initializeFish();
    }

    private void initializeCruiseShip() {
        this.cruiseShip = new Ship(75, horizonY + 30, 500, 50,
            Color.BLUE, Color.WHITE, Color.YELLOW, 2);
        cruiseShip.setPanelWidth(width);
    }

    private void initializeBridge() {
        int bridgeY = horizonY - 15;
        int bridgeHeight = 5;
        int archHeight = 20;
        this.bridge = new Bridge(0, bridgeY, width, bridgeHeight,
            new Color(160, 82, 45), archHeight);
    }

    private void initializeSea() {
        waves.clear();

        if (cruiseShip != null) {
            int waveY = horizonY + 60;
            Wave mainWave = new Wave(cruiseShip.getX() - 50, waveY, width + 100, 20, 2);
            mainWave.setTicksOffset(-20);
            waves.add(mainWave);
            cruiseShip.setY(horizonY + 30);
        }

        int waveCount = DEFAULT_WAVE_COUNT + random.nextInt(RANDOM_WAVE_COUNT);

        for (int i = 0; i < waveCount; i++) {
            int waveHeight = 8 + random.nextInt(12);
            int waveSpeed = 1 + random.nextInt(3);

            int minWaveY = horizonY + 30;
            int maxWaveY = height - waveHeight - 10;

            if (maxWaveY < minWaveY) maxWaveY = minWaveY + 50;

            int yPosition = minWaveY + random.nextInt(Math.max(maxWaveY - minWaveY, 1));

            Wave wave = new Wave(-100, yPosition, width + 200, waveHeight, waveSpeed);
            wave.setTicksOffset(i * 25 + random.nextInt(40));
            waves.add(wave);
        }
    }

    private void initializeClouds() {
        clouds.clear();
        int cloudsCount = 4 + random.nextInt(3);

        for (int i = 0; i < cloudsCount; i++) {
            int x = 10 + random.nextInt(Math.max(width - 20, 100));
            int availableHeight = Math.max(horizonY - 105, 1);
            int y = random.nextInt(availableHeight);
            clouds.add(Cloud.createRandom(x, y));
        }
    }

    private void initializeBirds() {
        birds.clear();
        int birdCount = 3 + random.nextInt(4);

        for (int i = 0; i < birdCount; i++) {
            int startX = random.nextInt(Math.max(width, 100));
            int availableHeight = Math.max(horizonY - 50, 1);
            int baseY = 30 + random.nextInt(availableHeight);
            int speed = 2 + random.nextInt(3);
            int amplitude = 3 + random.nextInt(4);
            int size = 10 + random.nextInt(6);

            Bird bird = new Bird(startX, baseY, speed, amplitude, size, Color.BLACK);
            bird.setPanelWidth(width);
            birds.add(bird);
        }
    }

    private void initializeCars() {
        cars.clear();
        int carCount = 2 + random.nextInt(6);

        for (int i = 0; i < carCount; i++) {
            int carX = -random.nextInt(300) - 100;
            Car car = Car.createRandom(carX, 0);

            int carY = bridge.getY() - (car.getType() == Car.CarType.MICROBUS ? 45 : 35);
            car.setY(carY);
            car.setPanelWidth(width);

            cars.add(car);
        }
    }

    private void initializeFish() {
        fishes.clear();
        int fishCount = 4 + random.nextInt(3);

        for (int i = 0; i < fishCount; i++) {
            int minX = cruiseShip.getX() - 50;
            int maxX = cruiseShip.getX() + cruiseShip.getLength() + 75;
            int x = minX + random.nextInt(Math.max(maxX - minX, 60));

            int shipBottomY = cruiseShip.getY() + cruiseShip.getHeight();
            int minFishY = Math.max(shipBottomY + 105, horizonY + 100);
            int maxFishY = Math.max(height - 60, minFishY + 50);

            if (maxFishY <= minFishY) {
                maxFishY = minFishY + 100;
            }

            int yRange = Math.max(maxFishY - minFishY, 30);
            int y = minFishY + random.nextInt(yRange);

            int fishWidth = 25 + random.nextInt(10);
            int fishHeight = 8 + random.nextInt(8);
            int speed = 3 + random.nextInt(2);

            Fish fish = new Fish(x, y, fishWidth, fishHeight, speed);
            fish.setPanelWidth(width);
            fish.setPanelHeight(height);
            fish.setHorizonY(horizonY);

            fishes.add(fish);
        }
    }

    private void updateMovingObjects() {
        for (Wave wave : waves) {
            wave.update(ticksFromStart);
        }

        if (cruiseShip != null) {
            cruiseShip.update();
            adjustShipToWaves();
        }

        for (Bird bird : birds) {
            bird.update(ticksFromStart);
        }

        for (Car car : cars) {
            car.update(width);
        }

        for (Fish fish : fishes) {
            fish.update();
        }
    }

    private void adjustShipToWaves() {
        if (cruiseShip == null) return;

        int shipX = cruiseShip.getX() + cruiseShip.getLength() / 2;
        int lowestWaveY = height;

        for (Wave wave : waves) {
            int waveY = wave.getWaveHeightAt(shipX, ticksFromStart);
            if (waveY < lowestWaveY) {
                lowestWaveY = waveY;
            }
        }

        cruiseShip.setY(lowestWaveY - 10);
    }

    private void drawBackground(Graphics2D g) {
        g.setColor(SKY_COLOR);
        g.fillRect(0, 0, width, horizonY);

        g.setColor(SEA_COLOR);
        g.fillRect(0, horizonY, width, height - horizonY);
    }

    private void drawStaticObjects(Graphics2D g) {
        sun.draw(g);

        if (bridge != null) {
            bridge.draw(g);
        }

        for (Cloud cloud : clouds) {
            cloud.draw(g);
        }
    }

    private void drawSeaEnvironment(Graphics2D g) {
        for (Wave wave : waves) {
            wave.draw(g);
        }

        if (cruiseShip != null) {
            cruiseShip.draw(g);
        }

        for (Fish fish : fishes) {
            if (fish.getY() > horizonY + 30) {
                fish.draw(g);
            }
        }
    }

    private void drawMovingObjects(Graphics2D g) {
        for (Bird bird : birds) {
            if (bird.getY() < horizonY) {
                bird.draw(g);
            }
        }

        for (Car car : cars) {
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
}
