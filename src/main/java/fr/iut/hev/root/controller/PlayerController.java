package fr.iut.hev.root.controller;

import fr.iut.hev.root.model.FixedAnimationTimer;
import fr.iut.hev.root.model.Player;
import fr.iut.hev.root.model.TileMap;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class PlayerController {
    private static TileMap tileMap;
    private Player player;

    @FXML
    private ImageView player_imageview;

    @FXML
    public void initialize() {
        player_imageview.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                player = new Player(newScene, tileMap, 0, -10, 32, 64, 2, 10);
                player_imageview.translateXProperty().bind(player.posXProperty());
                player_imageview.translateYProperty().bind(player.posYProperty());
                player_imageview.scaleXProperty().bind(player.lookDirectionProperty());

                new FixedAnimationTimer() {
                    @Override
                    protected void update() {
                        player.updateMovements();
                    }
                }.start();
            }
        });
    }

    public static void setTileMap(TileMap tilemap) {
        tileMap = tilemap; // Peut etre erreur au niveau de l'ordre d'execution
    }
}
