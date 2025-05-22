package fr.iut.hev.root.controller;

import fr.iut.hev.root.controller.InputHandling.KeyInputHandler;
import fr.iut.hev.root.model.Actor;
import fr.iut.hev.root.model.Player;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.view.GlobalView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GlobalController implements Initializable {
    private Timeline gameLoop;
    private Player player;
    private GlobalView vue;
    private TileMap tileMap;
    private ArrayList<Actor> aliveActors;

    @FXML
    private TilePane backgroundTileMap;

    @FXML
    private TilePane landTileMap;

    @FXML
    private ImageView player_imageview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        initMap();
        initActors();

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),
                (ev -> {
                    for (int i = aliveActors.size() - 1; i >= 0; i--) {
                        Actor currentActor = aliveActors.get(i);
                        if (currentActor.getIsAlive()) {
                            currentActor.updatePosition();
                        }
                        else {
                            aliveActors.remove(currentActor);
                        }
                    }
                })
        );
        gameLoop.getKeyFrames().add(kf);

        gameLoop.play();
    }

    private void initMap() {
        tileMap = new TileMap(1920,1056);
        vue = new GlobalView(tileMap, landTileMap,backgroundTileMap);
        vue.loadWorld();
    }

    private void initPlayer() {
        player = new Player(0, -25, 32, 64, tileMap, 2, 10);
        aliveActors.add(player);
        KeyInputHandler keyboardHandler = new KeyInputHandler(player);

        player_imageview.setLayoutX((double) (tileMap.getWidth() * TileMap.format) / 2);
        player_imageview.setLayoutY((double) (tileMap.getHeight() * TileMap.format) / 2);
        player_imageview.translateXProperty().bind(player.posXProperty());
        player_imageview.translateYProperty().bind(player.posYProperty());
        player_imageview.scaleXProperty().bind(player.lookDirectionProperty());
        Platform.runLater(() -> landTileMap.getScene().addEventHandler(KeyEvent.ANY,keyboardHandler));

    }

    private void initActors() {
        aliveActors = new ArrayList<>();

        initPlayer();
    }
}