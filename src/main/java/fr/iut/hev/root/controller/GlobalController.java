package fr.iut.hev.root.controller;

import fr.iut.hev.root.controller.InputHandling.KeyInputHandler;
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
import java.util.ResourceBundle;

public class GlobalController implements Initializable {
    private Timeline gameLoop;
    private Player player;
    private GlobalView vue;
    private TileMap tileMap;

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

        tileMap = initMap();
        initPlayer(tileMap);

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),
                (ev -> {
                    player.updatePosition();
                    System.out.println("update");
                })
        );
        gameLoop.getKeyFrames().add(kf);

        gameLoop.play();
    }

    private TileMap initMap() {
        TileMap tilemap = new TileMap(1920,1056);
        vue = new GlobalView(tilemap, landTileMap,backgroundTileMap);
        vue.loadWorld();
        return tilemap;
    }

    private void initPlayer(TileMap tilemap) {
        player = new Player(0, -25, 32, 64, tilemap, 2, 10);
        KeyInputHandler keyboardHandler = new KeyInputHandler(player);

        player_imageview.setLayoutX((double) (tilemap.getWidth() * TileMap.format) / 2);
        player_imageview.setLayoutY((double) (tilemap.getHeight() * TileMap.format) / 2);
        player_imageview.translateXProperty().bind(player.posXProperty());
        player_imageview.translateYProperty().bind(player.posYProperty());
        player_imageview.scaleXProperty().bind(player.lookDirectionProperty());
        Platform.runLater(() -> landTileMap.getScene().addEventHandler(KeyEvent.ANY,keyboardHandler));

    }
}