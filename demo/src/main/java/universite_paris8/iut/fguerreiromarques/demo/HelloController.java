package universite_paris8.iut.fguerreiromarques.demo;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;
import universite_paris8.iut.fguerreiromarques.demo.Environnement.ObstacleManager;
import universite_paris8.iut.fguerreiromarques.demo.Player.Gravity;
import universite_paris8.iut.fguerreiromarques.demo.Player.GroundCheck;

public class HelloController {

    @FXML

    private Pane gamePane;
    private ObstacleManager obstacleManager = new ObstacleManager();
    private final int WIDTH = 900, HEIGHT = 600;
    private final int GROUND_Y = 550;
    private Canvas canvas;
    private GraphicsContext gc;
    private double x = 100, y = 500;
    private final int playerWidth = 50, playerHeight = 50;
    private double velocityX = 0, velocityY = 0;
    private final double JUMP_STRENGTH = -15;
    private boolean onGround = true;
    private final Gravity gravity = new Gravity();
    private final GroundCheck gravityChecker = new GroundCheck();
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    @FXML
    public void initialize() {
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        gamePane.getChildren().add(canvas);

        gamePane.setFocusTraversable(true);
        gamePane.setOnKeyPressed(this::handleKeyPress);
        gamePane.setOnKeyReleased(this::handleKeyRelease);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
            }
        };
        timer.start();
    }
    // faire une délimitation pour les touches dans une autre classe
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.Q) {
            leftPressed = true;
            updateHorizontalVelocity();
        } else if (event.getCode() == KeyCode.D) {
            rightPressed = true;
            updateHorizontalVelocity();
        } else if (event.getCode() == KeyCode.SPACE && onGround) {
            velocityY = JUMP_STRENGTH;
            onGround = false;
        }
    }

    private void handleKeyRelease(KeyEvent event) {
        if (event.getCode() == KeyCode.Q) {
            leftPressed = false;
            updateHorizontalVelocity();
        } else if (event.getCode() == KeyCode.D) {
            rightPressed = false;
            updateHorizontalVelocity();
        }
    }

    // Méthode utilitaire pour gérer la vitesse horizontale
    private void updateHorizontalVelocity() {
        if (leftPressed && !rightPressed) {
            velocityX = -5;
        } else if (rightPressed && !leftPressed) {
            velocityX = 5;
        } else {
            velocityX = 0;
        }
    }

    private void update() {
        velocityY += gravity.getGravityForce();

        x += velocityX;
        y += velocityY;

        // Réinitialise l'état au début
        onGround = false;

        // Vérifie les collisions avec les obstacles
        checkCollisions();

        // Vérifie si le joueur touche le sol uniquement si aucune collision n'a déjà mis onGround à true
        if (!onGround && gravityChecker.isOnGround(y, playerHeight, GROUND_Y)) {
            y = GROUND_Y - playerHeight;
            velocityY = 0;
            onGround = true;
        }
    }

    private void checkCollisions() {
        Rectangle2D playerBounds = new Rectangle2D(x, y, playerWidth, playerHeight);
        for (var obs : obstacleManager.getObstacles()) {
            Rectangle2D obsBounds = obs.getBounds();
            if (playerBounds.intersects(obsBounds)) {
                double playerBottom = y + playerHeight;
                double playerTop = y;
                double playerRight = x + playerWidth;
                double playerLeft = x;
                double obsTop = obs.getY();
                double obsBottom = obs.getY() + obs.getHeight();
                double obsLeft = obs.getX();
                double obsRight = obs.getX() + obs.getWidth();
                double overlapBottom = playerBottom - obsTop;
                double overlapTop = obsBottom - playerTop;
                double overlapRight = playerRight - obsLeft;
                double overlapLeft = obsRight - playerLeft;
                double minOverlapX = Math.min(overlapRight, overlapLeft);
                double minOverlapY = Math.min(overlapBottom, overlapTop);
                if (minOverlapY < minOverlapX) {
                    if (playerBottom > obsTop && playerTop < obsTop && velocityY >= 0) {
                        y = obsTop - playerHeight;
                        velocityY = 0;
                        onGround = true;
                    } else if (playerTop < obsBottom && playerBottom > obsBottom && velocityY < 0) {
                        y = obsBottom;
                        velocityY = 0;
                    }
                } else {
                    // Collision latérale
                    if (playerRight > obsLeft && playerLeft < obsLeft) {
                        x = obsLeft - playerWidth;
                    } else if (playerLeft < obsRight && playerRight > obsRight) {
                        x = obsRight;
                    }
                    if (onGround) {
                        velocityX = 0;
                    }
                }
            }
        }
    }
    private void render() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Dessine le sol
        gc.setFill(Color.GRAY);
        gc.fillRect(0, GROUND_Y, WIDTH, HEIGHT - GROUND_Y);

        // Dessine les obstacles
        obstacleManager.drawAll(gc);

        // Dessine le joueur
        gc.setFill(Color.BLUE);
        gc.fillRect(x, y, playerWidth, playerHeight);
    }

}
