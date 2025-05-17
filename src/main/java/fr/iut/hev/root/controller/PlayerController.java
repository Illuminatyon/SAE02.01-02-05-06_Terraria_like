package fr.iut.hev.root.controller;

import fr.iut.hev.root.model.Player;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class PlayerController {
    private Player player;

    @FXML
    private ImageView player_imageview;

    @FXML
    public void initialize() {
        player_imageview.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                player = new Player(newScene, 0, 0, 2, 10);
                player_imageview.translateXProperty().bind(player.posXProperty());
                player_imageview.translateYProperty().bind(player.posYProperty());

                AnimationTimer inputTimer = new AnimationTimer() {
                    @Override
                    public void handle(long l) {
                        player.updateMovements();
                    }
                };
                inputTimer.start();
            }
        });
    }
}
