import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, MouseListener {
    Timer timer;
    ArrayList<Arrow> arrows = new ArrayList<>();
    int playerX = 300, playerY = 300;

    Enemy enemy = new Enemy(500, 300);

    public GamePanel() {
        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(Color.BLACK);
        this.addMouseListener(this);
        timer = new Timer(16, this); // 60 FPS
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Joueur
        g2.setColor(Color.WHITE);
        g2.fillOval(playerX - 10, playerY - 10, 20, 20);

        // Flèches
        for (Arrow arrow : arrows) {
            arrow.draw(g2);
        }

        // Ennemi
        enemy.draw(g2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ArrayList<Arrow> toRemove = new ArrayList<>();

        for (Arrow arrow : arrows) {
            arrow.update();

            if (enemy.isAlive && arrow.getHitbox().intersects(enemy.getHitbox())) {
                enemy.hit();
                toRemove.add(arrow); // flèche supprimée après le tir
            }
        }

        arrows.removeAll(toRemove);
        repaint();
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        arrows.add(new Arrow(playerX, playerY, e.getX(), e.getY(), 30));
    }

    // Non utilisés
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
