package models;

import java.awt.*;

public class Sun {
    private final int r;
    private final int R;
    private final int rays_number;
    private final Color c;
    private int panelWidth;
    private int panelHeight;

    public Sun(int r, int rayLength, int rays_number, Color c) {
        this.r = r;
        R = r + rayLength;
        this.rays_number = rays_number;
        this.c = c;
    }

    public void setPanelDimensions(int width, int height) {
        this.panelWidth = width;
        this.panelHeight = height;
    }

    public void draw(Graphics2D g) {
        if (panelWidth <= 0 || panelHeight <= 0) return;

        int margin = 20;
        int x = panelWidth - r - margin;
        int y = r + margin;

        g.setColor(c);
        g.fillOval(x - r, y - r, r * 2, r * 2);

        double da = 2 * Math.PI / rays_number;

        for (int i = 0; i < rays_number; i++) {
            double a = da * i;
            double x1 = x + (r + 10) * Math.cos(a);
            double y1 = y + (r + 10) * Math.sin(a);
            double x2 = x + (R + 20) * Math.cos(a);
            double y2 = y + (R + 10) * Math.sin(a);
            g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        }
    }
}
