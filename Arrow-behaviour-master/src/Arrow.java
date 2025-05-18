import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Arrow {
    double x, y;       // Position actuelle
    double vx, vy;     // Vitesse par axe
    double gravity = 0.3; // Force de gravité

    public Arrow(double startX, double startY, double targetX, double targetY, double speed) {
        x = startX;
        y = startY;

        // Calcul direction
        double dirX = targetX - startX;
        double dirY = targetY - startY;
        double length = Math.sqrt(dirX * dirX + dirY * dirY);

        // Vitesse initiale
        vx = speed * dirX / length;
        vy = speed * dirY / length;
    }

    public void update() {
        vy += gravity;   // Gravité appliquée à la vitesse verticale
        x += vx;
        y += vy;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillOval((int)x, (int)y, 6, 6);
    }
}
