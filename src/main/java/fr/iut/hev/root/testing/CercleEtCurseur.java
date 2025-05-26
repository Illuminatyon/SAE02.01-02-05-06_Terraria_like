package fr.iut.hev.root.testing;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class CercleEtCurseur extends Application {

    private static final double WINDOW_WIDTH = 600;
    private static final double WINDOW_HEIGHT = 600;
    private static final double CIRCLE_RADIUS = 250;
    private static final double CURSOR_RADIUS = 10;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        double centerX = WINDOW_WIDTH / 2;
        double centerY = WINDOW_HEIGHT / 2;

        Circle boundary = new Circle(centerX, centerY, CIRCLE_RADIUS);
        boundary.setFill(Color.LIGHTGRAY);
        boundary.setStroke(Color.BLACK);

        Circle cursor = new Circle(centerX, centerY, CURSOR_RADIUS, Color.RED);

        root.setOnMouseMoved(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();

            double dx = mouseX - centerX;
            double dy = mouseY - centerY;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance <= CIRCLE_RADIUS - CURSOR_RADIUS) {
                cursor.setCenterX(mouseX);
                cursor.setCenterY(mouseY);
            } else {
                double ratio = (CIRCLE_RADIUS - CURSOR_RADIUS) / distance;
                cursor.setCenterX(centerX + dx * ratio);
                cursor.setCenterY(centerY + dy * ratio);
            }
        });

        root.setOnMouseClicked((MouseEvent event) -> {
            System.out.printf("Coordonnées du joueur : X = %.2f, Y = %.2f%n",
                    cursor.getCenterX(), cursor.getCenterY());
        });

        root.getChildren().addAll(boundary, cursor);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Déplacement dans un cercle");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
