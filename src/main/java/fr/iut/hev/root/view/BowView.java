package fr.iut.hev.root.view;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.InputStream;

/**
 * View class for the bow animation
 */
public class BowView {
    private static BowView instance;
    private ImageView bowSprite;
    private AnchorPane anchorPane;

    /**
     * Gets the singleton instance of BowView
     * @param anchorPane the anchor pane to add the bow sprite to
     * @return the BowView instance
     */
    public static BowView getInstance(AnchorPane anchorPane) {
        if (instance == null) {
            instance = new BowView(anchorPane);
        } else if (instance.anchorPane != anchorPane) {
            // If the anchor pane has changed, update it
            instance.anchorPane = anchorPane;
            // Remove the sprite from the old anchor pane if it exists
            if (instance.bowSprite != null && instance.bowSprite.getParent() != null) {
                ((AnchorPane)instance.bowSprite.getParent()).getChildren().remove(instance.bowSprite);
                // Add it to the new anchor pane
                anchorPane.getChildren().add(instance.bowSprite);
            }
        }
        return instance;
    }

    /**
     * Private constructor for the BowView (singleton pattern)
     * @param anchorPane the anchor pane to add the bow sprite to
     */
    private BowView(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
        loadBowSprite();
    }

    /**
     * Loads the bow sprite
     */
    private void loadBowSprite() {
        // Use the bow image for the bow
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
     * Starts the bow animation and executes a callback when complete
     * @param playerCenterX the x position of the player center
     * @param playerCenterY the y position of the player center
     * @param dirX the normalized x direction
     * @param dirY the normalized y direction
     * @param cameraOffsetX the camera offset x
     * @param cameraOffsetY the camera offset y
     * @param onAnimationComplete callback to execute when animation completes
     */
    public void startBowAnimation(double playerCenterX, double playerCenterY, 
                                 double dirX, double dirY, 
                                 double cameraOffsetX, double cameraOffsetY,
                                 Runnable onAnimationComplete) {
        // Position the bow in front of the player in the direction of the mouse
        double bowX = playerCenterX + dirX * 20; // 20 pixels in front of player
        double bowY = playerCenterY + dirY * 20;

        // Show the bow animation
        showBowAnimation(
            bowX + cameraOffsetX, 
            bowY + cameraOffsetY, 
            dirX, 
            dirY
        );

        // Create a fade transition to make the bow disappear
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.4), bowSprite);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setDelay(Duration.seconds(0.2));

        // Create animation timeline
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0.5), e -> {
                // Execute the callback
                if (onAnimationComplete != null) {
                    onAnimationComplete.run();
                }

                // Hide the bow after the animation completes
                fadeTransition.play();
            })
        );

        timeline.play();
    }

    /**
     * Shows the bow animation
     * @param x the x position of the bow
     * @param y the y position of the bow
     * @param dirX the x direction of the bow
     * @param dirY the y direction of the bow
     */
    private void showBowAnimation(double x, double y, double dirX, double dirY) {
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
        if (bowSprite != null) {
            bowSprite.setVisible(false);
        }
    }
}
