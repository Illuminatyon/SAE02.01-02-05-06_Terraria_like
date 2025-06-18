package fr.iut.hev.root.view;

import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.InputStream;

/**
 * View class for the bow animation
 */
public class BowView {
    private ImageView bowSprite;
    private AnchorPane anchorPane;

    /**
     * Constructor for the BowView
     * @param anchorPane the anchor pane to add the bow sprite to
     */
    public BowView(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
        loadBowSprite();
    }

    /**
     * Loads the bow sprite
     */
    private void loadBowSprite() {
        // Use the Bow_JE2_BE1 image for the bow
        String path = "/fr/iut/hev/root/img/items/bow.png";
        System.out.println("Trying to load bow image from path: " + path);

        try (InputStream stream = getClass().getResourceAsStream(path)) {
            if (stream == null) {
                System.err.println("ERROR: Bow image not found at path: " + path);
                return;
            }

            Image image = new Image(stream);
            this.bowSprite = new ImageView(image);

            // Set the size of the bow sprite
            bowSprite.setFitWidth(32);
            bowSprite.setFitHeight(32);

            // Initially hide the bow
            bowSprite.setVisible(false);

            // Add the bow sprite to the anchor pane
            anchorPane.getChildren().add(bowSprite);

            System.out.println("Bow image loaded successfully");
        } catch (Exception e) {
            System.err.println("Exception while loading bow image");
            e.printStackTrace();
        }
    }

    /**
     * Shows the bow animation
     * @param x the x position of the bow
     * @param y the y position of the bow
     * @param dirX the x direction of the bow
     * @param dirY the y direction of the bow
     */
    public void showBowAnimation(double x, double y, double dirX, double dirY) {
        if (bowSprite == null) {
            System.err.println("ERROR: Bow sprite is null");
            return;
        }

        // Determine if the bow should be on the left or right side of the player
        // based on the direction vector
        double offsetX = 0;
        if (dirX > 0) {
            // Player is facing right, place bow on right side
            offsetX = 20;
        } else {
            // Player is facing left, place bow on left side
            offsetX = -20;
        }

        // Set the position of the bow to be static relative to the player
        bowSprite.setLayoutX(x - bowSprite.getFitWidth() / 2 + offsetX);
        bowSprite.setLayoutY(y - bowSprite.getFitHeight() / 2);

        // Calculate the rotation angle based on the direction
        double angle = Math.toDegrees(Math.atan2(dirY, dirX));
        // Add 180 degrees to rotate the bow sprite
        bowSprite.setRotate(angle + 180);

        // Make the bow visible
        bowSprite.setVisible(true);
        bowSprite.setOpacity(1.0);
    }

    /**
     * Hides the bow
     */
    public void hideBow() {
        // In the simplified version, we don't hide the bow immediately
        // It will stay visible until the next animation
    }
}
