package fr.iut.hev.root.controller;

import fr.iut.hev.root.controller.InputHandling.KeyInputHandler;
import fr.iut.hev.root.model.Actor;
import fr.iut.hev.root.model.Player;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.view.GlobalView;
import fr.iut.hev.root.view.HUDView;
import fr.iut.hev.root.view.MinimapView;
import fr.iut.hev.root.view.PlayerView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GlobalController implements Initializable {
    private Timeline gameLoop;
    private Player player;
    private GlobalView globalView;
    private HUDView hudView;
    private PlayerView playerView;
    private TileMap tileMap;
    private ArrayList<Actor> aliveActors;
    private MinimapView minimapView;

    @FXML
    private TilePane backgroundTileMap;

    @FXML
    private TilePane landTileMap;

    @FXML
    private ImageView player_imageview;

    @FXML
    private HBox heartsHbox;

    @FXML
    private StackPane minimapContainer;

    @FXML
    private AnchorPane globalPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        initMap();
        initActors();

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),
                (ev -> {
                    // Update actors
                    for (int i = aliveActors.size() - 1; i >= 0; i--) {
                        Actor currentActor = aliveActors.get(i);
                        currentActor.diesQuestionMark();
                        if (currentActor.getIsAliveProperty()) {
                            currentActor.updatePosition();
                        }
                        else {
                            aliveActors.remove(currentActor);
                        }
                    }

                    // Minimap is always visible
                    if (player != null) {
                        minimapContainer.setVisible(true);
                    }
                })
        );
        gameLoop.getKeyFrames().add(kf);

        gameLoop.play();
    }

    private void initMap() {
        tileMap = new TileMap(1920,1056);
        globalView = new GlobalView(tileMap, landTileMap,backgroundTileMap);
        globalView.loadWorld();
    }

    private void initPlayer() {
        player = new Player(0, -25, 32, 64, tileMap, 2, 10);
        aliveActors.add(player);
        hudView = new HUDView(player,heartsHbox);
        playerView = new PlayerView(player,player_imageview,tileMap);
        playerView.load();

        // Initialize and add the minimap
        minimapView = new MinimapView(tileMap, player);
        minimapContainer.getChildren().add(minimapView);

        player.healthProperty().addListener(((obs, old, t1) -> hudView.updateHealth()));
        player.isAliveProperty().addListener(((observableValue, aBoolean, t1) -> playerView.deletePlayerSprite()));
        KeyInputHandler keyboardHandler = new KeyInputHandler(player);
        Platform.runLater(() -> landTileMap.getScene().addEventHandler(KeyEvent.ANY,keyboardHandler));
    }

    private void initActors() {
        aliveActors = new ArrayList<>();

        initPlayer();
    }
}
