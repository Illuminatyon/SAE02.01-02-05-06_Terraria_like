package fr.iut.hev.root.controller;

import fr.iut.hev.root.model.FixedAnimationTimer;
import fr.iut.hev.root.model.Player;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.view.GlobalView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

import java.net.URL;
import java.util.ResourceBundle;

public class GlobalController implements Initializable {

    private Player player;

    @FXML
    private TilePane backgroundTileMap;

    @FXML
    private TilePane tileMap;

    @FXML
    private ImageView player_imageview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TileMap tilemap = new TileMap(1920,1056);
        GlobalView vue = new GlobalView(tilemap,tileMap,backgroundTileMap);
        player_imageview.setLayoutX((double) (tilemap.getWidth() * TileMap.format) / 2);
        player_imageview.setLayoutY((double) (tilemap.getHeight() * TileMap.format) / 2);
        vue.loadWorld();
        player_imageview.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                player = new Player(newScene, tilemap, 0, -25, 32, 64, 2, 10);
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
}
