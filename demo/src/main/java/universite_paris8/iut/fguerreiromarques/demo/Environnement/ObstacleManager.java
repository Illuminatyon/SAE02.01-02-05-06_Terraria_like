package universite_paris8.iut.fguerreiromarques.demo.Environnement;
import universite_paris8.iut.fguerreiromarques.demo.*;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

public class ObstacleManager {
    private ArrayList<Obstacle> obstacles;

    public ObstacleManager() {
        obstacles = new ArrayList<>();
        obstacles.add(new Obstacle(300, 500, 60, 50)); // Exemple obstacle
        obstacles.add(new Obstacle(500, 470, 40, 80)); // Autre exemple
    }

    public void drawAll(GraphicsContext gc) {
        for (Obstacle obs : obstacles) {
            obs.draw(gc);
        }
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }
}
