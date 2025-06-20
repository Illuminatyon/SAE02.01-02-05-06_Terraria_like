package fr.iut.hev.root.view;

import fr.iut.hev.root.model.entities.Entity;
import fr.iut.hev.root.model.hitbox.Hitbox;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * View class for displaying hitboxes in red.
 * This class is responsible for rendering hitboxes from the HitboxManager.
 */
public class HitboxView {
    private HitboxManager hitboxManager;
    private AnchorPane anchorPane;
    private Map<Hitbox, Rectangle> hitboxRectangles;

    /**
     * Creates a new hitbox view.
     *
     * @param hitboxManager The hitbox manager to get hitboxes from
     * @param anchorPane The anchor pane to add hitbox rectangles to
     */
    public HitboxView(HitboxManager hitboxManager, AnchorPane anchorPane) {
        this.hitboxManager = hitboxManager;
        this.anchorPane = anchorPane;
        this.hitboxRectangles = new HashMap<>();

        // Initial rendering of all hitboxes
        updateHitboxes();
    }

    /**
     * Updates the hitbox rectangles based on the current state of the hitbox manager.
     * This method should be called periodically to keep the view in sync with the model.
     */
    public void updateHitboxes() {
        // Clear existing rectangles
        for (Rectangle rectangle : hitboxRectangles.values()) {
            anchorPane.getChildren().remove(rectangle);
        }
        hitboxRectangles.clear();

        // Get all interactive hitboxes from the hitbox manager
        Map<?, Hitbox> interactiveHitboxes = hitboxManager.getInteractiveHitboxes();
        if (interactiveHitboxes != null) {
            for (Hitbox hitbox : interactiveHitboxes.values()) {
                createHitboxRectangle(hitbox);
            }
        }
    }

    /**
     * Creates a rectangle for a hitbox and adds it to the anchor pane.
     *
     * @param hitbox The hitbox to create a rectangle for
     */
    private void createHitboxRectangle(Hitbox hitbox) {
        Rectangle rectangle = new Rectangle(
                hitbox.getCenterX() - hitbox.getWidth() / 2,
                hitbox.getCenterY() - hitbox.getHeight() / 2,
                hitbox.getWidth(),
                hitbox.getHeight()
        );

        // Set the rectangle's style
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.RED);
        rectangle.setStrokeWidth(2);

        // Add the rectangle to the anchor pane and the map
        anchorPane.getChildren().add(rectangle);
        hitboxRectangles.put(hitbox, rectangle);

        // Update the rectangle's position
        updateRectanglePosition(hitbox, rectangle);
    }

    /**
     * Updates the position of a hitbox rectangle based on the hitbox's position.
     * This method is called when the hitbox's position changes.
     *
     * @param hitbox The hitbox
     * @param rectangle The rectangle to update
     */
    private void updateRectanglePosition(Hitbox hitbox, Rectangle rectangle) {
        // Bind the rectangle's position to the hitbox's position
        rectangle.xProperty().bind(hitbox.xProperty().subtract(hitbox.getWidth() / 2));
        rectangle.yProperty().bind(hitbox.yProperty().subtract(hitbox.getHeight() / 2));
    }
}
