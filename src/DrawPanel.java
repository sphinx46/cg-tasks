import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.Bird;
import models.Cloud;
import models.Sun;
import models.Wave;

public class DrawPanel extends JPanel implements ActionListener {

    private final int PANEL_WIDTH;
    private final int PANEL_HEIGHT;
    private final int TIMER_DELAY;
    private Timer timer;
    private int ticksFromStart = 0;

    private List<Wave> waves;
    private List<Bird> birds;
    private List<Cloud> clouds;

    private int horizonY;

    Sun s1 = new Sun(670, 75, 30, 30, 15, Color.ORANGE);

    public DrawPanel(final int width, final int height, final int timerDelay) {
        this.PANEL_WIDTH = Math.max(width, 1);
        this.PANEL_HEIGHT = Math.max(height, 1);
        this.TIMER_DELAY = timerDelay;
        this.horizonY = (int)(PANEL_HEIGHT * 0.25);

        this.waves = new ArrayList<>();
        this.birds = new ArrayList<>();

        timer = new Timer(timerDelay, this);
        timer.start();

        initializeSea();
        initializeBirds();
        initalizeCloud();
    }

    private void initializeSea() {
        waves = new ArrayList<>();
        Random random = new Random();

        int waveCount = 15 + random.nextInt(6);

        for (int i = 0; i < waveCount; i++) {
            int waveHeight = 5 + random.nextInt(20);
            int waveSpeed = 1 + random.nextInt(5);

            int seaHeight = PANEL_HEIGHT - horizonY;
            int yPosition = horizonY + random.nextInt(seaHeight - waveHeight);

            Wave wave = new Wave(0, yPosition, PANEL_WIDTH, waveHeight, waveSpeed);
            wave.setTicksOffset(i * 15 + random.nextInt(30));
            waves.add(wave);
        }

        for (int i = 0; i < 10; i++) {
            int waveHeight = 2 + random.nextInt(8);
            int waveSpeed = 2 + random.nextInt(4);
            int seaHeight = PANEL_HEIGHT - horizonY;
            int yPosition = horizonY + random.nextInt(seaHeight - waveHeight - 20);

            Wave wave = new Wave(0, yPosition, PANEL_WIDTH, waveHeight, waveSpeed);
            wave.setTicksOffset(i * 25 + random.nextInt(40));
            waves.add(wave);
        }
    }

    private void initalizeCloud() {
        clouds = new ArrayList<>();
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
        birds = new ArrayList<>();
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


    @Override
    public void paint(final Graphics gr) {
        super.paint(gr);
        Graphics2D g = (Graphics2D) gr;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawSky(g);

        s1.draw(g);


        g.setColor(new Color(0, 105, 148));
        g.fillRect(0, horizonY, PANEL_WIDTH, PANEL_HEIGHT - horizonY);

        for (Wave wave : waves) {
            wave.update(ticksFromStart);
            wave.draw(g);
        }

        for (Cloud cloud : clouds) {
            cloud.draw(g);
        }

        for (Bird bird : birds) {
            bird.update(ticksFromStart);
            if (bird.getY() < horizonY) {
                bird.draw(g);
            }
        }



        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        for (int i = 0; i < waves.size() / 2; i++) {
            Wave wave = waves.get(i);
            wave.update(ticksFromStart + 10);
            g.setColor(new Color(255, 255, 255, 50));
            wave.draw(g);
        }
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
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
