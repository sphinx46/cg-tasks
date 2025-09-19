import models.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DrawPanel extends JPanel implements ActionListener {

    private static final Color SKY_COLOR = new Color(135, 206, 235);
    private static final Color SEA_COLOR = new Color(0, 105, 148);
    private static final Color WAVE_REFLECTION_COLOR = new Color(255, 255, 255, 50);
    private static final float WAVE_REFLECTION_ALPHA = 0.4f;
    private static final int MIN_SIZE_THRESHOLD = 50;
    private static final int DEFAULT_WAVE_COUNT = 20;
    private static final int RANDOM_WAVE_COUNT = 5;

    private final Timer timer;
    private int ticksFromStart = 0;
    private boolean initialized = false;

    private int horizonY;
    private int lastWidth = 0;
    private int lastHeight = 0;

    private final Sun sun;
    private Bridge bridge;
    private Ship cruiseShip;

    private final List<Wave> waves = new ArrayList<>();
    private final List<Bird> birds = new ArrayList<>();
    private final List<Cloud> clouds = new ArrayList<>();
    private final List<Fish> fishes = new ArrayList<>();
    private final List<Car> cars = new ArrayList<>();

    private final Random random = new Random();

    public DrawPanel(final int timerDelay) {
        this.sun = new Sun(30, 30, 15, Color.ORANGE);
        this.timer = new Timer(timerDelay, this);
        timer.start();
    }

    @Override
    public void paint(final Graphics gr) {
        super.paint(gr);

        if (!isValidSize()) return;

        Graphics2D g = (Graphics2D) gr;
        setupRenderingHints(g);

        if (!initialized) {
            initializeScene();
        } else {
            updateDimensions();
        }

        drawScene(g);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == timer) {
            repaint();
            ticksFromStart++;
        }
    }

    private boolean isValidSize() {
        return getWidth() > 0 && getHeight() > 0;
    }

    private void setupRenderingHints(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private void initializeScene() {
        updateDimensions();
        initializeAllEntities();
        initialized = true;
    }

    private void updateDimensions() {
        int width = Math.max(getWidth(), 1);
        int height = Math.max(getHeight(), 1);
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
        initializeCruiseShip();
        initializeBridge();
        initializeSea();
        initializeBirds();
        initializeClouds();
        initializeCars();
        initializeFish();
    }

    private void initializeCruiseShip() {
        this.cruiseShip = new Ship(75, horizonY + 50, 500, 50,
            Color.BLUE, Color.WHITE, Color.YELLOW, 2);
        cruiseShip.setPanelWidth(getWidth());
    }

    private void initializeBridge() {
        int bridgeY = horizonY - 15;
        int bridgeHeight = 5;
        int archHeight = 20;
        this.bridge = new Bridge(0, bridgeY, getWidth(), bridgeHeight,
            new Color(160, 82, 45), archHeight);
    }

    private void initializeSea() {
        waves.clear();

        if (cruiseShip != null) {
            int waveY = cruiseShip.getY() + cruiseShip.getHeight() - 5;
            Wave mainWave = new Wave(cruiseShip.getX() - 50, waveY, getWidth() + 100, 20, 2);
            mainWave.setTicksOffset(-20);
            waves.add(mainWave);
        }

        int waveCount = DEFAULT_WAVE_COUNT + random.nextInt(RANDOM_WAVE_COUNT);

        for (int i = 0; i < waveCount; i++) {
            int waveHeight = 8 + random.nextInt(12);
            int waveSpeed = 1 + random.nextInt(3);

            int minWaveY = horizonY + 20;
            int maxWaveY = getHeight() - waveHeight - 10;
            if (maxWaveY < minWaveY) maxWaveY = minWaveY + 10;

            int yPosition = minWaveY + random.nextInt(Math.max(maxWaveY - minWaveY, 1));

            Wave wave = new Wave(-100, yPosition, getWidth() + 200, waveHeight, waveSpeed);
            wave.setTicksOffset(i * 25 + random.nextInt(40));
            waves.add(wave);
        }
    }

    private void initializeClouds() {
        clouds.clear();
        int cloudsCount = 4 + random.nextInt(3);

        for (int i = 0; i < cloudsCount; i++) {
            int x = 10 + random.nextInt(Math.max(getWidth() - 20, 100));
            int availableHeight = Math.max(horizonY - 105, 1);
            int y = random.nextInt(availableHeight);
            clouds.add(Cloud.createRandom(x, y));
        }
    }

    private void initializeBirds() {
        birds.clear();
        int birdCount = 3 + random.nextInt(4);

        for (int i = 0; i < birdCount; i++) {
            int startX = random.nextInt(Math.max(getWidth(), 100));
            int availableHeight = Math.max(horizonY - 50, 1);
            int baseY = 30 + random.nextInt(availableHeight);
            int speed = 2 + random.nextInt(3);
            int amplitude = 3 + random.nextInt(4);
            int size = 10 + random.nextInt(6);

            Bird bird = new Bird(startX, baseY, speed, amplitude, size, Color.BLACK);
            bird.setPanelWidth(getWidth());
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
            car.setPanelWidth(getWidth());

            cars.add(car);
        }
    }

    private void initializeFish() {
        fishes.clear();
        int fishCount = 3 + random.nextInt(3);

        for (int i = 0; i < fishCount; i++) {
            int minX = cruiseShip.getX() - 50;
            int maxX = cruiseShip.getX() + cruiseShip.getLength() + 50;
            int x = minX + random.nextInt(Math.max(maxX - minX, 50));

            int shipBottomY = cruiseShip.getY() + cruiseShip.getHeight();
            int minFishY = shipBottomY + 20;
            int maxFishY = getHeight() - 30;

            if (maxFishY < minFishY) maxFishY = minFishY + 30;

            int y = minFishY + random.nextInt(Math.max(maxFishY - minFishY, 1));
            int width = 25 + random.nextInt(10);
            int height = 8 + random.nextInt(8);
            int speed = 1 + random.nextInt(2);

            Fish fish = new Fish(x, y, width, height, speed);
            fish.setPanelWidth(getWidth());
            fish.setPanelHeight(getHeight());
            fish.setHorizonY(horizonY);

            fishes.add(fish);
        }
    }

    private void drawScene(Graphics2D g) {
        drawBackground(g);
        drawStaticObjects(g);
        drawSeaEnvironment(g);
        drawMovingObjects(g);
        drawWaveReflections(g);
    }

    private void drawBackground(Graphics2D g) {
        g.setColor(SKY_COLOR);
        g.fillRect(0, 0, getWidth(), horizonY);

        g.setColor(SEA_COLOR);
        g.fillRect(0, horizonY, getWidth(), getHeight() - horizonY);
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
            wave.update(ticksFromStart);
            wave.draw(g);
        }

        if (cruiseShip != null) {
            cruiseShip.update();
            adjustShipToWaves();
            cruiseShip.draw(g);
        }

        for (Fish fish : fishes) {
            fish.update();
            if (fish.getY() > horizonY + 30) {
                fish.draw(g);
            }
        }
    }

    private void drawMovingObjects(Graphics2D g) {
        for (Bird bird : birds) {
            bird.update(ticksFromStart);
            if (bird.getY() < horizonY) {
                bird.draw(g);
            }
        }

        for (Car car : cars) {
            car.update(getWidth());
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

    private void adjustShipToWaves() {
        if (cruiseShip == null) return;

        int shipX = cruiseShip.getX() + cruiseShip.getLength() / 2;
        int lowestWaveY = getHeight();

        for (Wave wave : waves) {
            int waveY = wave.getWaveHeightAt(shipX, ticksFromStart);
            if (waveY < lowestWaveY) {
                lowestWaveY = waveY;
            }
        }

        cruiseShip.setY(lowestWaveY - 10);
    }
}
