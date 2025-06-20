package fr.iut.hev.root.view;

import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.view.actor.PlayerView;
import fr.iut.hev.root.view.actor.PnjView;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

/**
 * Manages the camera view that follows the player in the game world.
 * This class handles the positioning and smooth movement of the game view
 * to keep the player centered on the screen.
 */
public class Camera {
    /**
     * The player that the camera follows.
     */
    private final Player player;

    /**
     * The tile pane containing the foreground terrain.
     */
    private final TilePane landTileMap;

    /**
     * The tile pane containing the background terrain.
     */
    private final TilePane backgroundTileMap;

    /**
     * The main pane containing all game elements.
     */
    private final Pane globalPane;

    /**
     * The view for the player character.
     */
    private final PlayerView playerView;

    /**
     * The view for item drops in the world.
     */
    private final LootView lootView;
    private PnjView pnjView;

    /**
     * Factor controlling how smoothly the camera follows the player.
     * Lower values result in slower, smoother camera movement.
     */
    private final double smoothFactor;

    /**
     * Property for the current X position of the camera.
     */
    private DoubleProperty currentCamXProperty = new SimpleDoubleProperty(0);

    /**
     * Property for the current Y position of the camera.
     */
    private DoubleProperty currentCamYProperty = new SimpleDoubleProperty(0);

    /**
     * Creates a new camera that follows the specified player.
     *
     * @param player The player to follow
     * @param landTileMap The tile pane containing the foreground terrain
     * @param backgroundTileMap The tile pane containing the background terrain
     * @param globalPane The main pane containing all game elements
     * @param playerView The view for the player character
     * @param lootView The view for item drops
     * @param smoothFactor Factor controlling camera movement smoothness
     */
    public Camera(Player player, TilePane landTileMap, TilePane backgroundTileMap,
                  Pane globalPane, PlayerView playerView, LootView lootView, double smoothFactor) {
        this.player = player;
        this.landTileMap = landTileMap;
        this.backgroundTileMap = backgroundTileMap;
        this.globalPane = globalPane;
        this.playerView = playerView;
        this.lootView = lootView;
        this.smoothFactor = smoothFactor;
    }

    public void setPnjView(PnjView pnjView) {
        this.pnjView = pnjView;
    }

    /**
     * Updates the camera position to follow the player.
     * This method is called on each frame to smoothly adjust the camera position
     * so that the player remains centered on the screen.
     */
    public void update() {
        if (player == null || landTileMap == null || backgroundTileMap == null) {
            return;
        }

        double screenWidth = globalPane.getWidth();
        double screenHeight = globalPane.getHeight();

        double playerCenterX = player.getPosX() + player.getWidth() / 2;
        double playerCenterY = player.getPosY() + player.getHeight() / 2;

        double targetCamX = (screenWidth / 2) - playerCenterX;
        double targetCamY = (screenHeight / 2) - playerCenterY;

//        currentCamXProperty += (targetCamX - currentCamXProperty) * smoothFactor;
//        currentCamYProperty += (targetCamY - currentCamYProperty) * smoothFactor;

        setCurrentCamX(getCurrentCamX() + (targetCamX - getCurrentCamX()) * smoothFactor);
        setCurrentCamY(getCurrentCamY() + (targetCamY - getCurrentCamY()) * smoothFactor);


//        landTileMap.setTranslateX(currentCamXProperty);
//        landTileMap.setTranslateY(currentCamYProperty);
//        backgroundTileMap.setTranslateX(currentCamXProperty);
//        backgroundTileMap.setTranslateY(currentCamYProperty);

        landTileMap.setTranslateX(getCurrentCamX());
        landTileMap.setTranslateY(getCurrentCamY());
        backgroundTileMap.setTranslateX(getCurrentCamX());
        backgroundTileMap.setTranslateY(getCurrentCamY());
        if (pnjView != null) {
            pnjView.getLabel().setTranslateX(getCurrentCamX());
            pnjView.getLabel().setTranslateY(getCurrentCamY());
        }

        if (lootView != null) {
            lootView.updateLootPositions(getCurrentCamX(), getCurrentCamY());
        }
    }

    /**
     * Gets the current X position of the camera.
     *
     * @return The current X position
     */
    public double getCurrentCamX() {
        return currentCamXProperty.getValue();
    }

    /**
     * Gets the current Y position of the camera.
     *
     * @return The current Y position
     */
    public double getCurrentCamY() {
        return currentCamYProperty.getValue();
    }

    /**
     * Sets the current X position of the camera.
     *
     * @param offset The new X position
     */
    public void setCurrentCamX(double offset) {this.currentCamXProperty.setValue(offset);}

    /**
     * Sets the current Y position of the camera.
     *
     * @param offset The new Y position
     */
    public void setCurrentCamY(double offset) {this.currentCamYProperty.setValue(offset);}

    /**
     * Gets the property for the current X position of the camera.
     * This can be used for binding to other properties.
     *
     * @return The X position property
     */
    public DoubleProperty currentCamXProperty() {return this.currentCamXProperty;}

    /**
     * Gets the property for the current Y position of the camera.
     * This can be used for binding to other properties.
     *
     * @return The Y position property
     */
    public DoubleProperty currentCamYProperty() {return this.currentCamYProperty;}
}