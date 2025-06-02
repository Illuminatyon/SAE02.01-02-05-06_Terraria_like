package fr.iut.hev.root.controller;

import fr.iut.hev.root.controller.InputHandling.KeyInputHandler;
import fr.iut.hev.root.controller.InputHandling.MouseGameInputHandler;
import fr.iut.hev.root.controller.InputHandling.MouseInventoryInputHandler;
import fr.iut.hev.root.controller.InputHandling.ScrollInputHandler;
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
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GlobalController implements Initializable {
    private Timeline gameLoop;
    private Player player;
    private TileMap tileMap;
    private MouseGameInputHandler mouseGameClicksHandler;
    private MouseInventoryInputHandler mouseInventoryHandler;
    private ScrollInputHandler scrollHotbarHandler;
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

    @FXML
    private AnchorPane hudAnchorPane;

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
                    if (mouseGameClicksHandler.getMouseClickIsPressed()) {
                        mouseGameClicksHandler.clickPressedHandler();
                    }
                    if (mouseGameClicksHandler.getMouseClickIsReleased()) {
                        mouseGameClicksHandler.clickReleasedHandler();
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
        inventoryView = new InventoryView(inventory, hotbarInventory, expandedInventory,hudAnchorPane);
        playerView.load();

        inventory.add(5,new Item(Items.DIRT),64);
        inventory.add(24,new Item(Items.STONE),45);
        inventory.add(39,new Item(Items.DIRT),40);
        inventory.add(16,new Item(Items.DIRT),120);
        inventory.add(31,new Item(Items.DIRT),30);
        inventory.add(8,new Item(Items.STONE),80);
        inventory.add(48,new Item(Items.DIRT),76);


        player.healthProperty().addListener(((obs, old, t1) -> hudView.updateHealth()));
        player.isAliveProperty().addListener(((observableValue, aBoolean, t1) -> playerView.deletePlayerSprite()));

        KeyInputHandler keyboardHandler = new KeyInputHandler(player,inventoryView);

        double playerCenterX = player_imageview.getLayoutX() + player_imageview.getTranslateX() + player_imageview.getFitWidth() / 2;
        double playerCenterY = player_imageview.getLayoutY() + player_imageview.getTranslateY() + player_imageview.getFitHeight() / 2;
        playerLightCircle = new MouseCursorCircleView(globalPane, playerCenterX, playerCenterY, player.getReach()*32, 10);
        playerLightCircle.setCursorVisible(false);

        mouseGameClicksHandler = new MouseGameInputHandler(tileMap,globalView,player,playerLightCircle,inventoryView);
        mouseInventoryHandler = new MouseInventoryInputHandler(inventory,inventoryView);
        scrollHotbarHandler = new ScrollInputHandler(inventoryView);

        mouseInventoryHandler.onHoldProperty().addListener((observableValue, o, t1) -> {inventoryView.updateOnHoldPane(mouseInventoryHandler.getOnHold());});
        mouseInventoryHandler.xProperty().addListener((observableValue, number, t1) -> {inventoryView.updateOnHoldPosition(mouseInventoryHandler.getX(), mouseInventoryHandler.getY());});
        mouseInventoryHandler.yProperty().addListener((observableValue, number, t1) -> {inventoryView.updateOnHoldPosition(mouseInventoryHandler.getX(), mouseInventoryHandler.getY());});

        Platform.runLater(() -> {
            landTileMap.getScene().addEventHandler(KeyEvent.ANY,keyboardHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, mouseGameClicksHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_RELEASED, mouseGameClicksHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseGameClicksHandler);
            hudAnchorPane.addEventHandler(MouseEvent.MOUSE_PRESSED,mouseInventoryHandler);
            hudAnchorPane.addEventHandler(MouseEvent.MOUSE_MOVED,mouseInventoryHandler);
            landTileMap.getScene().addEventHandler(ScrollEvent.SCROLL,scrollHotbarHandler);
        });
    }

    private void initActors() {
        aliveActors = new ArrayList<>();
        initPlayer();
    }
}
