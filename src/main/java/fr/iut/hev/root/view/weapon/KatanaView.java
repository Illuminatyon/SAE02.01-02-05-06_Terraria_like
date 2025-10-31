package fr.iut.hev.root.view.weapon;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.InputStream;

/**
 * View class for the katana animation
 */
public class KatanaView {
    private ImageView katanaSprite;
    private AnchorPane anchorPane;

    /**
     * Constructor for the KatanaView
     * @param anchorPane the anchor pane to add the katana sprite to
     */
    public KatanaView(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
        loadKatanaSprite();
    }

    /**
     * Loads the katana sprite
     */
    private void loadKatanaSprite() {
        String path = "/fr/iut/hev/root/img/items/katana.png";

        try (InputStream stream = getClass().getResourceAsStream(path)) {
            if (stream == null) {
                System.err.println("ERROR: Katana image not found at path: " + path);
                return;
            }

            Image image = new Image(stream);
            this.katanaSprite = new ImageView(image);

            // Set the size of the katana sprite
            katanaSprite.setFitWidth(64);
            katanaSprite.setFitHeight(64);

            // Initially hide the katana
            katanaSprite.setVisible(false);

            // Add the katana sprite to the anchor pane
            anchorPane.getChildren().add(katanaSprite);
        } catch (Exception e) {
            System.err.println("Exception while loading katana image");
            e.printStackTrace();
        }
    }

    /**
     * Shows the katana animation with a plunging attack that moves towards the clicked location and back
     * @param x the x position of the katana (player position)
     * @param y the y position of the katana (player position)
     * @param dirX the x direction of the katana
     * @param dirY the y direction of the katana
     * @param targetX the x position of the target (where the player clicked)
     * @param targetY the y position of the target (where the player clicked)
     */
    public void showKatanaPlungeAnimation(double x, double y, double dirX, double dirY, double targetX, double targetY) {
        if (katanaSprite == null) {
            System.err.println("ERROR: Katana sprite is null");
            return;
        }

        // Calculate the rotation angle based on the direction
        double angle = Math.toDegrees(Math.atan2(dirY, dirX));

        // Position the katana at the player's position
        katanaSprite.setLayoutX(x - katanaSprite.getFitWidth() / 2);
        katanaSprite.setLayoutY(y - katanaSprite.getFitHeight() / 2);

        // Set initial rotation with an additional 45° rotation
        katanaSprite.setRotate(angle + 45);

        // Make the katana visible
        katanaSprite.setVisible(true);
        katanaSprite.setOpacity(1.0);

        // Create a timeline for the plunging attack animation
        Timeline plungeTimeline = new Timeline();

        // Phase 1: Move from player to target (0.25 seconds)
        KeyFrame startFrame = new KeyFrame(Duration.ZERO,
                new KeyValue(katanaSprite.layoutXProperty(), x - katanaSprite.getFitWidth() / 2),
                new KeyValue(katanaSprite.layoutYProperty(), y - katanaSprite.getFitHeight() / 2)
        );

        KeyFrame targetFrame = new KeyFrame(Duration.seconds(0.25),
                new KeyValue(katanaSprite.layoutXProperty(), targetX - katanaSprite.getFitWidth() / 2),
                new KeyValue(katanaSprite.layoutYProperty(), targetY - katanaSprite.getFitHeight() / 2)
        );

        // Phase 2: Stay at target briefly (0.1 seconds)
        KeyFrame stayFrame = new KeyFrame(Duration.seconds(0.35));

        // Phase 3: Return from target to player (0.25 seconds)
        KeyFrame returnFrame = new KeyFrame(Duration.seconds(0.6),
                new KeyValue(katanaSprite.layoutXProperty(), x - katanaSprite.getFitWidth() / 2),
                new KeyValue(katanaSprite.layoutYProperty(), y - katanaSprite.getFitHeight() / 2)
        );

        // Add all keyframes to the timeline
        plungeTimeline.getKeyFrames().addAll(startFrame, targetFrame, stayFrame, returnFrame);

        // Create a fade transition to make the katana disappear at the end
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.2));
        fadeTransition.setNode(katanaSprite);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setDelay(Duration.seconds(0.6)); // Start fading after the return animation

        // Play the animations
        plungeTimeline.play();
        fadeTransition.play();
    }

    /**
     * Hides the katana
     */
    public void hideKatana() {
        if (katanaSprite != null) {
            katanaSprite.setVisible(false);
        }
    }
}