package fr.iut.hev.root.view.weapon;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.InputStream;

/**
 * View class for the dagger animation
 */
public class DaggerView {
    private ImageView daggerSprite;
    private AnchorPane anchorPane;

    /**
     * Constructor for the DaggerView
     * @param anchorPane the anchor pane to add the dagger sprite to
     */
    public DaggerView(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
        loadDaggerSprite();
    }

    /**
     * Loads the dagger sprite
     */
    private void loadDaggerSprite() {
        String path = "/fr/iut/hev/root/img/items/dagger.png";

        try (InputStream stream = getClass().getResourceAsStream(path)) {
            if (stream == null) {
                System.err.println("ERROR: Dagger image not found at path: " + path);
                return;
            }

            Image image = new Image(stream);
            this.daggerSprite = new ImageView(image);

            // Set the size of the dagger sprite
            daggerSprite.setFitWidth(32);
            daggerSprite.setFitHeight(32);

            // Initially hide the dagger
            daggerSprite.setVisible(false);

            // Add the dagger sprite to the anchor pane
            anchorPane.getChildren().add(daggerSprite);
        } catch (Exception e) {
            System.err.println("Exception while loading dagger image");
            e.printStackTrace();
        }
    }

    /**
     * Shows the dagger animation
     * @param x the x position of the dagger
     * @param y the y position of the dagger
     * @param dirX the x direction of the dagger
     * @param dirY the y direction of the dagger
     */
    public void showDaggerAnimation(double x, double y, double dirX, double dirY) {
        if (daggerSprite == null) {
            System.err.println("ERROR: Dagger sprite is null");
            return;
        }

        // Set the position of the dagger
        daggerSprite.setLayoutX(x - daggerSprite.getFitWidth() / 2);
        daggerSprite.setLayoutY(y - daggerSprite.getFitHeight() / 2);

        // Calculate the rotation angle based on the direction
        double angle = Math.toDegrees(Math.atan2(dirY, dirX));
        daggerSprite.setRotate(angle);

        // Make the dagger visible
        daggerSprite.setVisible(true);
        daggerSprite.setOpacity(1.0);

        // Create a fade transition to make the dagger disappear
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.4), daggerSprite);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setDelay(Duration.seconds(0.2));
        fadeTransition.play();

        // Create a rotate transition to make the dagger rotate
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.2), daggerSprite);
        rotateTransition.setByAngle(45);
        rotateTransition.play();
    }

    /**
     * Hides the dagger
     */
    public void hideDagger() {
        if (daggerSprite != null) {
            daggerSprite.setVisible(false);
        }
    }
}