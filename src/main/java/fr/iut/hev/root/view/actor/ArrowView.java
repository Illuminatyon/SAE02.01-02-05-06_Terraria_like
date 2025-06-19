package fr.iut.hev.root.view.actor;

import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.entities.Arrow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.InputStream;

/**
 * View class for the arrow projectile
 */
public class ArrowView extends ActorView {
    private Arrow arrow;
    private ImageView arrowSprite;

    /**
     * Constructor for the ArrowView
     * @param arrow the arrow entity
     * @param tileMap the tile map
     * @param anchorPane the anchor pane to add the arrow sprite to
     */
    public ArrowView(Arrow arrow, TileMap tileMap, AnchorPane anchorPane) {
        super(arrow, tileMap, anchorPane);
        this.arrow = arrow;
    }

    /**
     * Loads the arrow sprite
     */
    /*@Override
    public void load() {
        // Use the arrow.png image for the arrow
        String path = "/fr/iut/hev/root/img/items/arrow.png";
        System.out.println("Trying to load arrow image from path: " + path);

        // First try to load the arrow image
        InputStream arrowStream = getClass().getResourceAsStream(path);

        // If arrow image is not found, try to load a fallback image
        if (arrowStream == null) {
            System.err.println("ERROR: Arrow image not found at path: " + path);
            path = "/fr/iut/hev/root/img/items/stick.png"; // Fallback to stick if arrow image is not found
            System.out.println("Trying to load fallback image from path: " + path);
            arrowStream = getClass().getResourceAsStream(path);

            if (arrowStream == null) {
                System.err.println("ERROR: Fallback image not found either");
                return;
            }
        }

        try {
            Image image = new Image(arrowStream);
            this.arrowSprite = new ImageView(image);

            // Check if arrow is null (can happen during initialization)
            if (arrow == null) {
                // Use default size if arrow is null
                arrowSprite.setFitWidth(32);
                arrowSprite.setPreserveRatio(true);
            } else {
                // Set the size of the arrow sprite while preserving aspect ratio
                arrowSprite.setFitWidth(32);
                arrowSprite.setPreserveRatio(true);

                // Rotate the arrow based on its velocity
                arrowSprite.setRotate(arrow.getAngle());
            }

            // Initially position the arrow in the world plane
            arrowSprite.setTranslateX(0);
            arrowSprite.setTranslateY(0);

            // Add the arrow sprite to the anchor pane
            getAnchorPane().getChildren().add(arrowSprite);

            System.out.println("Arrow image loaded successfully");
        } catch (Exception e) {
            System.err.println("Exception while loading arrow image");
            e.printStackTrace();
        }
    }*/

    /**
     * Updates the arrow sprite's position and rotation
     */
    /*public void update() {
        if (arrowSprite != null && arrow != null) {
            // Debug: Log arrow update
            System.out.println("[DEBUG_LOG] Updating arrow sprite at position: " + arrow.getPosX() + ", " + arrow.getPosY());

            // Update position with camera offset
            double cameraOffsetX = 0;
            double cameraOffsetY = 0;

            // Get camera offset from GlobalController if available
            if (arrow instanceof fr.iut.hev.root.model.entities.Arrow) {
                fr.iut.hev.root.model.entities.Arrow arrowEntity = (fr.iut.hev.root.model.entities.Arrow) arrow;
                if (arrowEntity.getGlobalController() != null) {
                    cameraOffsetX = arrowEntity.getGlobalController().getCameraOffsetX();
                    cameraOffsetY = arrowEntity.getGlobalController().getCameraOffsetY();
                }
            }

            // Update position with camera offset
            // Use setTranslateX/Y to position arrows in the world plane
            arrowSprite.setTranslateX(arrow.getPosX() + cameraOffsetX);
            arrowSprite.setTranslateY(arrow.getPosY() + cameraOffsetY);

            // Update rotation based on arrow's velocity
            arrowSprite.setRotate(arrow.getAngle());

            // Make sure the arrow is visible
            arrowSprite.setVisible(true);
        } else {
            // Debug: Log why update was skipped
            if (arrowSprite == null) {
                System.out.println("[DEBUG_LOG] Arrow update skipped: arrowSprite is null");
            }
            if (arrow == null) {
                System.out.println("[DEBUG_LOG] Arrow update skipped: arrow is null");
            }
        }
    }*/

    /**
     * Gets the arrow sprite
     * @return the arrow sprite or null if not initialized
     */
    /*@Override
    public ImageView getActorSprite() {
        return arrowSprite;
    }*/

    /**
     * Overrides the deleteActorSprite method to set arrowSprite to null after removing it from the AnchorPane
     */
    /*@Override
    public void deleteActorSprite() {
        // Debug: Log arrow sprite deletion
        System.out.println("[DEBUG_LOG] Deleting arrow sprite");

        // Check if arrowSprite is already null
        if (arrowSprite == null) {
            System.out.println("[DEBUG_LOG] Arrow sprite is already null, nothing to delete");
            return;
        }

        // Check if arrowSprite is in the AnchorPane
        if (getAnchorPane() != null && getAnchorPane().getChildren().contains(arrowSprite)) {
            System.out.println("[DEBUG_LOG] Arrow sprite found in AnchorPane, removing it");
        } else {
            System.out.println("[DEBUG_LOG] Arrow sprite not found in AnchorPane");
        }

        // Call the parent method to remove the sprite from the AnchorPane
        super.deleteActorSprite();

        // Set arrowSprite to null to prevent further updates
        arrowSprite = null;
        System.out.println("[DEBUG_LOG] Arrow sprite set to null");

        // Force a refresh of the AnchorPane to ensure the sprite is visually removed
        if (getAnchorPane() != null) {
            getAnchorPane().requestLayout();
            System.out.println("[DEBUG_LOG] Requested layout refresh for AnchorPane");
        }
    }*/
}
