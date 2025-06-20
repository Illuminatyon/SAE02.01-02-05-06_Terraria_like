package fr.iut.hev.root.view;

import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.view.actor.PlayerView;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

public class Camera {
    private final Player player;
    private final TilePane landTileMap;
    private final TilePane backgroundTileMap;
    private final Pane globalPane;
    private final PlayerView playerView;
    private final LootView lootView;

    private final double smoothFactor;

    private DoubleProperty currentCamXProperty = new SimpleDoubleProperty(0);
    private DoubleProperty currentCamYProperty = new SimpleDoubleProperty(0);

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

        if (lootView != null) {
            lootView.updateLootPositions(getCurrentCamX(), getCurrentCamY());
        }
    }

    public double getCurrentCamX() {
        return currentCamXProperty.getValue();
    }

    public double getCurrentCamY() {
        return currentCamYProperty.getValue();
    }
    public void setCurrentCamX(double offset) {this.currentCamXProperty.setValue(offset);}
    public void setCurrentCamY(double offset) {this.currentCamYProperty.setValue(offset);}
    public DoubleProperty currentCamXProperty() {return this.currentCamXProperty;}
    public DoubleProperty currentCamYProperty() {return  this.currentCamYProperty;}
}
