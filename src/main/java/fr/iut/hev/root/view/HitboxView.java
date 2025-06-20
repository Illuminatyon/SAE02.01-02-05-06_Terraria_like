package fr.iut.hev.root.view;

import fr.iut.hev.root.controller.GlobalController;
import fr.iut.hev.root.model.entities.Actor;
import fr.iut.hev.root.model.entities.Entity;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.entities.AggressiveMob;
import fr.iut.hev.root.model.enums.HitboxType;
import fr.iut.hev.root.model.hitbox.Hitbox;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
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
    private GlobalController globalController;
    private List<Actor> aliveActors;

    /**
     * Creates a new hitbox view.
     *
     * @param hitboxManager The hitbox manager to get hitboxes from
     * @param anchorPane The anchor pane to add hitbox rectangles to
     * @param globalController The global controller to get camera offset from
     */
    public HitboxView(HitboxManager hitboxManager, AnchorPane anchorPane, GlobalController globalController) {
        this.hitboxManager = hitboxManager;
        this.anchorPane = anchorPane;
        this.hitboxRectangles = new HashMap<>();
        this.globalController = globalController;

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

        // Get hitboxes for all alive actors
        List<Actor> aliveActors = globalController.getAliveActors();
        if (aliveActors != null) {
            for (Actor actor : aliveActors) {
                // Get vulnerable hitbox for all actors
                Hitbox vulnerableHitbox = actor.getHitboxManager().getEntityHitbox(actor, HitboxType.VULNERABLE);
                if (vulnerableHitbox != null) {
                    createHitboxRectangle(vulnerableHitbox);
                }

                // Get attack hitbox for AggressiveMob
                if (actor instanceof AggressiveMob) {
                    Hitbox attackHitbox = actor.getHitboxManager().getEntityHitbox(actor, HitboxType.ATTACK);
                    if (attackHitbox != null) {
                        createHitboxRectangle(attackHitbox);
                    }
                }

                // Get interactive hitbox for Player
                if (actor instanceof Player) {
                    Hitbox interactiveHitbox = ((Player) actor).getInteractiveHitbox();
                    if (interactiveHitbox != null) {
                        createHitboxRectangle(interactiveHitbox);
                    }
                }
            }
        }
    }

    /**
     * Creates a rectangle for a hitbox and adds it to the anchor pane.
     *
     * @param hitbox The hitbox to create a rectangle for
     */
    private void createHitboxRectangle(Hitbox hitbox) {
        // Create a rectangle with initial position (will be updated by binding)
        Rectangle rectangle = new Rectangle(0, 0, hitbox.getWidth(), hitbox.getHeight());

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
     * Applies the camera offset to position the hitbox correctly in the view.
     *
     * @param hitbox The hitbox
     * @param rectangle The rectangle to update
     */
    private void updateRectanglePosition(Hitbox hitbox, Rectangle rectangle) {
        // Get the camera offset from the global controller
        double cameraOffsetX = globalController.getCameraOffsetX();
        double cameraOffsetY = globalController.getCameraOffsetY();

        // Bind the rectangle's position to the hitbox's position with camera offset
        // The hitbox's position is its center, so we need to subtract half the width and height
        // to get the top-left corner of the rectangle
        rectangle.xProperty().bind(hitbox.xProperty().subtract(hitbox.getWidth() / 2).add(cameraOffsetX));
        rectangle.yProperty().bind(hitbox.yProperty().subtract(hitbox.getHeight() / 2).add(cameraOffsetY));
    }
}
