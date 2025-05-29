package fr.iut.hev.root.view;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MouseCursorCircleView {
    private final Circle boundary;
    private final Circle cursor;

    private double centerX;
    private double centerY;
    private final double boundaryRadius;
    private final double cursorRadius;

    public MouseCursorCircleView(Pane parent, double centerX, double centerY, double boundaryRadius, double cursorRadius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.boundaryRadius = boundaryRadius;
        this.cursorRadius = cursorRadius;

        boundary = new Circle(centerX, centerY, boundaryRadius);
        boundary.setFill(Color.TRANSPARENT);
        boundary.setStroke(Color.BLACK);

        cursor = new Circle(centerX, centerY, cursorRadius, Color.RED);

        parent.getChildren().addAll(boundary, cursor);

        parent.setOnMouseMoved(this::handleMouseMove);
    }

    private void handleMouseMove(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        double dx = mouseX - centerX;
        double dy = mouseY - centerY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= boundaryRadius - cursorRadius) {
            cursor.setCenterX(mouseX);
            cursor.setCenterY(mouseY);
        } else {
            double ratio = (boundaryRadius - cursorRadius) / distance;
            cursor.setCenterX(centerX + dx * ratio);
            cursor.setCenterY(centerY + dy * ratio);
        }
    }

    public void registerClickHandler() {
        boundary.setOnMouseClicked(event -> {
            System.out.printf("Coordonnées du curseur : X = %.2f, Y = %.2f%n", cursor.getCenterX(), cursor.getCenterY());
        });
    }

    public void updateCenter(double x, double y) {
        this.centerX = x;
        this.centerY = y;

        boundary.setCenterX(x);
        boundary.setCenterY(y);

        cursor.setCenterX(x);
        cursor.setCenterY(y);
    }
}
