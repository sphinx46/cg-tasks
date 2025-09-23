package ru.vsu.cs.computergraphics.mordvinovil.task1;
import ru.vsu.cs.computergraphics.mordvinovil.task1.models.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DrawPanel extends JPanel implements ActionListener {
    private final Timer timer;
    private final World world;

    public DrawPanel(final int timerDelay) {
        this.world = new World();
        this.timer = new Timer(timerDelay, this);
        timer.start();
    }

    @Override
    public void paint(final Graphics gr) {
        super.paint(gr);

        if (getWidth() <= 0 || getHeight() <= 0) return;

        Graphics2D g = (Graphics2D) gr;
        setupRenderingHints(g);

        world.update(getWidth(), getHeight());

        world.draw(g);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == timer) {
            repaint();
        }
    }

    private void setupRenderingHints(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
}
