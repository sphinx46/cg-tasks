package models;

import java.awt.*;

public class Sun {
    private int x, y, r, R, rays_number;
    private Color c;

    public Sun(int x, int y, int r, int rayLength, int rays_number, Color c) {
        this.x = x;
        this.y = y;
        this.r = r;
        R = r + rayLength;
        this.rays_number = rays_number;
        this.c = c;
    }

    public  void draw(Graphics2D g) {
        g.setColor(c);
        g.fillOval(x - r, y - r, r + r, r + r);

        double da = 2 * Math.PI / rays_number;

        for (int i = 0; i < rays_number; i++) {
            double a = da * i;
            double x1 = x + (r + 10) * Math.cos(a);
            double y1 = y + (r + 10)  * Math.sin(a);
            double x2 = x + (R + 20)  * Math.cos(a);
            double y2 = y + (R + 10)  * Math.sin(a);
            g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        }
    }
}
