package fr.iut.hev.root.controller;

import fr.iut.hev.root.controller.InputHandling.KeyInputHandler;
import fr.iut.hev.root.controller.InputHandling.MouseInputHandler;
import fr.iut.hev.root.model.*;
import fr.iut.hev.root.model.enums.Items;
import fr.iut.hev.root.view.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import fr.iut.hev.root.view.InventoryView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GlobalController implements Initializable {
    private Timeline gameLoop;
    private Player player;
    private TileMap tileMap;
    private MouseInputHandler mouseClicksPressedHandler;
    private ArrayList<Actor> aliveActors;
    private Inventory inventory;

    private GlobalView globalView;
    private HUDView hudView;
    private PlayerView playerView;
    private MouseCursorCircleView playerLightCircle;
    private InventoryView inventoryView;

    @FXML
    private TilePane backgroundTileMap;

    @FXML
    private TilePane landTileMap;

    @FXML
    private ImageView player_imageview;

    @FXML
    private HBox heartsHbox;

    @FXML
    private AnchorPane globalPane;

    @FXML
    private GridPane hotbarInventory;

    @FXML
    private GridPane expandedInventory;

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
                        currentActor.diesQuestionMark();
                        if (currentActor.getIsAliveProperty()) {
                            currentActor.updatePosition();
                        }
                        else {
                            aliveActors.remove(currentActor);
                        }
                    }
                    if (mouseClicksPressedHandler.getMouseClickIsPressed()) {
                        mouseClicksPressedHandler.clickPressedHandler();
                    }
                    if (mouseClicksPressedHandler.getMouseClickIsReleased()) {
                        mouseClicksPressedHandler.clickReleasedHandler();
                    }

                    // Mise à jour de la position de la lumière autour du joueur
                    if (playerLightCircle != null) {
                        double playerCenterX = player_imageview.getLayoutX() + player_imageview.getTranslateX() + player_imageview.getFitWidth() / 2;
                        double playerCenterY = player_imageview.getLayoutY() + player_imageview.getTranslateY() + player_imageview.getFitHeight() / 2;
                        playerLightCircle.updateCenter(playerCenterX, playerCenterY);
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
        player = new Player(0, -25, 32, 64, tileMap, 2, 10,3);
        aliveActors.add(player);
        inventory = new Inventory();

        hudView = new HUDView(player,heartsHbox);
        playerView = new PlayerView(player,player_imageview,tileMap);
        inventoryView = new InventoryView(inventory, hotbarInventory, expandedInventory);
        playerView.load();

        inventory.add(5,new Item(Items.DIRT),64);
        inventory.add(39,new Item(Items.DIRT),45);

        player.healthProperty().addListener(((obs, old, t1) -> hudView.updateHealth()));
        player.isAliveProperty().addListener(((observableValue, aBoolean, t1) -> playerView.deletePlayerSprite()));

        KeyInputHandler keyboardHandler = new KeyInputHandler(player,inventoryView);

        double playerCenterX = player_imageview.getLayoutX() + player_imageview.getTranslateX() + player_imageview.getFitWidth() / 2;
        double playerCenterY = player_imageview.getLayoutY() + player_imageview.getTranslateY() + player_imageview.getFitHeight() / 2;
        playerLightCircle = new MouseCursorCircleView(globalPane, playerCenterX, playerCenterY, player.getReach()*32, 10);
        playerLightCircle.setCursorVisible(false);

        mouseClicksPressedHandler = new MouseInputHandler(tileMap,globalView,player,playerLightCircle);
        Platform.runLater(() -> {
            landTileMap.getScene().addEventHandler(KeyEvent.ANY,keyboardHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED,mouseClicksPressedHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_RELEASED,mouseClicksPressedHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED,mouseClicksPressedHandler);
        });
    }

    private void initActors() {
        aliveActors = new ArrayList<>();
        initPlayer();
    }
}
