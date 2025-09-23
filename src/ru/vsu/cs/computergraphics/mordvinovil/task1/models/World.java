package ru.vsu.cs.computergraphics.mordvinovil.task1.models;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    private static final Color SKY_COLOR = new Color(135, 206, 235);
    private static final Color SEA_COLOR = new Color(0, 105, 148);
    private static final Color SAND_COLOR = new Color(194, 178, 128);
    private static final Color WAVE_REFLECTION_COLOR = new Color(255, 255, 255, 50);
    private static final float WAVE_REFLECTION_ALPHA = 0.4f;
    private static final int DEFAULT_WAVE_COUNT = 75;
    private static final int RANDOM_WAVE_COUNT = 5;
    private static final int MIN_SIZE_THRESHOLD = 50;
    private static final int FISH_SPAWN_ATTEMPTS = 50;

    private int width;
    private int height;
    private int horizonY;
    private int underwaterLineY;
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
    private final List<Seaweed> seaweeds = new ArrayList<>();
    private final Random random = new Random();

    private Image underwaterWorldBuffer;
    private boolean bufferNeedsUpdate = true;
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
            bufferNeedsUpdate = true;
        } else {
            boolean sizeChanged = updateDimensions();
            if (sizeChanged) {
                bufferNeedsUpdate = true;
            }
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
        drawUnderwaterDivision(g);
        drawUnderwaterWorld(g);
    }

    private void initialize() {
        this.horizonY = (int) (height * 0.25);
        this.underwaterLineY = (int) (height * 0.75);
        initializeAllEntities();
        lastWidth = width;
        lastHeight = height;

        if (width > 0 && height > 0) {
            underwaterWorldBuffer = new BufferedImage(width, height - underwaterLineY, BufferedImage.TYPE_INT_ARGB);
            bufferNeedsUpdate = true;
        }
    }

    private boolean updateDimensions() {
        int oldUnderwaterLineY = underwaterLineY;

        this.horizonY = (int) (height * 0.25);
        this.underwaterLineY = (int) (height * 0.75);

        boolean sizeChanged = hasSizeChangedSignificantly(width, height);
        updateAllObjectsDimensions(width, height, sizeChanged);

        if (oldUnderwaterLineY != underwaterLineY || width != lastWidth || height != lastHeight) {
            bufferNeedsUpdate = true;
        }

        lastWidth = width;
        lastHeight = height;

        return sizeChanged;
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
            initializeSeaweeds();
        }

        updateObjectsWidth(waves, wave -> wave.setPanelWidth(width));
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
        initializeSeaweeds();
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
            Wave mainWave = new Wave(cruiseShip.getX() - width, waveY, width * 3, 25, 2);
            mainWave.setPanelWidth(width * 3);
            waves.add(mainWave);
            cruiseShip.setY(horizonY + 30);
        }

        int waveCount = DEFAULT_WAVE_COUNT + random.nextInt(RANDOM_WAVE_COUNT);

        for (int i = 0; i < waveCount; i++) {
            int waveHeight = 10 + random.nextInt(3);
            int waveSpeed = 1 + random.nextInt(3);

            int minWaveY = horizonY + 30;
            int maxWaveY = underwaterLineY - waveHeight - 10;

            if (maxWaveY < minWaveY) maxWaveY = minWaveY + 50;

            int yPosition = minWaveY + random.nextInt(Math.max(maxWaveY - minWaveY, 1));

            Wave wave = new Wave(-width, yPosition, width * 3, waveHeight, waveSpeed);
            wave.setPanelWidth(width * 3);
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
            int speed = 4 + random.nextInt(3);
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
        List<Point> occupiedPositions = new ArrayList<>();

        for (int i = 0; i < fishCount; i++) {
            Point fishPosition = findValidFishPosition(occupiedPositions);
            if (fishPosition == null) break;

            int fishWidth = 20 + random.nextInt(8);
            int fishHeight = 6 + random.nextInt(6);
            int speed = 1;

            Fish fish = new Fish(fishPosition.x, fishPosition.y, fishWidth, fishHeight, speed);
            fish.setPanelWidth(width);
            fish.setPanelHeight(height);
            fish.setHorizonY(horizonY);

            fishes.add(fish);
            occupiedPositions.add(fishPosition);
        }
    }

    private Point findValidFishPosition(List<Point> occupiedPositions) {
        int minX = 50;
        int maxX = width - 50;
        int minFishY = underwaterLineY + 40;
        int maxFishY = height - 60;

        minFishY = Math.max(minFishY, underwaterLineY + 30);
        maxFishY = Math.min(maxFishY, height - 50);

        int yRange = Math.max(maxFishY - minFishY, 40);

        for (int attempt = 0; attempt < FISH_SPAWN_ATTEMPTS; attempt++) {
            int x = minX + random.nextInt(Math.max(maxX - minX, 60));
            int y = minFishY + random.nextInt(yRange);

            Point candidate = new Point(x, y);
            if (isPositionValid(candidate, occupiedPositions)) {
                return candidate;
            }
        }

        return null;
    }

    private boolean isPositionValid(Point candidate, List<Point> occupiedPositions) {
        int minDistance = 60;

        for (Point occupied : occupiedPositions) {
            if (candidate.distance(occupied) < minDistance) {
                return false;
            }
        }
        return true;
    }

    private void initializeSeaweeds() {
        seaweeds.clear();
        int seaweedCount = 8 + random.nextInt(10);

        for (int i = 0; i < seaweedCount; i++) {
            int x = random.nextInt(width);
            int seaweedHeight = 30 + random.nextInt(50);
            Seaweed seaweed = new Seaweed(x, seaweedHeight);
            seaweeds.add(seaweed);
        }
        bufferNeedsUpdate = true;
    }

    private void updateMovingObjects() {
        for (Wave wave : waves) {
            wave.update(ticksFromStart);
        }

        if (cruiseShip != null) {
            adjustShipToWaves();
            cruiseShip.update();
        }

        for (Bird bird : birds) {
            bird.update(ticksFromStart);
        }

        for (Car car : cars) {
            car.update();
        }

        for (Fish fish : fishes) {
            fish.update();
            if (fish.getY() < underwaterLineY + 20) {
                fish.getMover().setY(underwaterLineY + 20 + random.nextInt(20));
            }
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

        cruiseShip.setY(lowestWaveY - 25);
    }

    private void drawBackground(Graphics2D g) {
        g.setColor(SKY_COLOR);
        g.fillRect(0, 0, width, horizonY);

        int seaHeight = underwaterLineY - horizonY;

        for (int i = 0; i < seaHeight; i++) {
            float progress = (float) i / seaHeight;
            int red = (int) (SEA_COLOR.getRed() * (1 - progress * 0.3));
            int green = (int) (SEA_COLOR.getGreen() * (1 - progress * 0.3));
            int blue = (int) (SEA_COLOR.getBlue() * (1 - progress * 0.2));

            red = Math.max(0, Math.min(255, red));
            green = Math.max(0, Math.min(255, green));
            blue = Math.max(0, Math.min(255, blue));

            g.setColor(new Color(red, green, blue));
            g.drawLine(0, horizonY + i, width, horizonY + i);
        }

        Color deepSeaColor = SEA_COLOR.darker();
        g.setColor(deepSeaColor);
        g.fillRect(0, underwaterLineY, width, height - underwaterLineY);
    }

    private void drawStaticObjects(Graphics2D g) {
        sun.draw(g);

        for (Cloud cloud : clouds) {
            cloud.draw(g);
        }

        if (bridge != null) {
            bridge.draw(g);
        }
    }

    private void drawSeaEnvironment(Graphics2D g) {
        for (Wave wave : waves) {
            wave.draw(g);
        }

        if (cruiseShip != null) {
            cruiseShip.draw(g);
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

    private void drawUnderwaterDivision(Graphics2D g) {
        g.setColor(new Color(SEA_COLOR.getRed(), SEA_COLOR.getGreen(), SEA_COLOR.getBlue(), 150));
        g.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
            0, new float[]{10, 5}, 0));
        g.drawLine(0, underwaterLineY, width, underwaterLineY);
        g.setStroke(new BasicStroke(1));
    }

    private void drawUnderwaterWorld(Graphics2D g) {
        if (bufferNeedsUpdate && underwaterWorldBuffer != null) {
            updateUnderwaterWorldBuffer();
            bufferNeedsUpdate = false;
        }

        if (underwaterWorldBuffer != null) {
            g.drawImage(underwaterWorldBuffer, 0, underwaterLineY, null);
        }

        for (Fish fish : fishes) {
            if (fish.getY() > underwaterLineY) {
                fish.draw(g);
            }
        }
    }

    private void updateUnderwaterWorldBuffer() {
        if (underwaterWorldBuffer == null ||
            underwaterWorldBuffer.getWidth(null) != width ||
            underwaterWorldBuffer.getHeight(null) != (height - underwaterLineY)) {

            if (width > 0 && height > underwaterLineY) {
                underwaterWorldBuffer = new BufferedImage(width, height - underwaterLineY, BufferedImage.TYPE_INT_ARGB);
            }
        }

        if (underwaterWorldBuffer == null || width <= 0 || height <= underwaterLineY) {
            return;
        }

        Graphics2D bufferGraphics = (Graphics2D) underwaterWorldBuffer.getGraphics();

        bufferGraphics.setComposite(AlphaComposite.Clear);
        bufferGraphics.fillRect(0, 0, underwaterWorldBuffer.getWidth(null), underwaterWorldBuffer.getHeight(null));
        bufferGraphics.setComposite(AlphaComposite.SrcOver);

        bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawSand(bufferGraphics);
        drawSeaweeds(bufferGraphics);

        bufferGraphics.dispose();
    }

    private void drawSand(Graphics2D g) {
        int bufferHeight = height - underwaterLineY;
        int sandHeight = Math.min(40, bufferHeight);
        int sandY = bufferHeight - sandHeight;

        g.setColor(SAND_COLOR);
        g.fillRect(0, sandY, width, sandHeight);

        g.setColor(SAND_COLOR.brighter());
        Random sandRandom = new Random(12345);
        for (int i = 0; i < width; i += 4) {
            for (int j = sandY; j < bufferHeight; j += 4) {
                if (sandRandom.nextBoolean()) {
                    g.fillRect(i, j, 1, 1);
                }
            }
        }
    }

    private void drawSeaweeds(Graphics2D g) {
        int bufferHeight = height - underwaterLineY;
        for (Seaweed seaweed : seaweeds) {
            seaweed.draw(g, bufferHeight);
        }
    }
}
