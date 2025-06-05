package fr.iut.hev.root.controller;

import fr.iut.hev.root.controller.InputHandling.KeyInputHandler;
import fr.iut.hev.root.controller.InputHandling.MouseGameInputHandler;
import fr.iut.hev.root.controller.InputHandling.MouseInventoryInputHandler;
import fr.iut.hev.root.controller.InputHandling.ScrollInputHandler;
import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.entities.*;
import fr.iut.hev.root.controller.InputHandling.*;
import fr.iut.hev.root.model.enums.ConsumableStats;
import fr.iut.hev.root.model.enums.Items;
import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.entities.Loot;
import fr.iut.hev.root.model.items.Consumable;
import fr.iut.hev.root.view.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
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
    private KeyInputHandler keyboardHandler;
    private MouseItemActionInputHandler mouseItemActionHandler;
    private ArrayList<Actor> aliveActors;
    private Inventory inventory;
    public static Mob mob ;

    private GlobalView globalView;
    private HUDView hudView;
    private PlayerView playerView;
    private MouseCursorCircleView playerLightCircle;
    private InventoryView inventoryView;
    private HotbarView hotbarView;
    private MobView mobView;

    @FXML
    private TilePane backgroundTileMap;

    @FXML
    private TilePane landTileMap;



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
        LootView lv = new LootView(globalPane);

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
                    for (Loot loot : Loot.lootOnMapProperty) {
                        loot.updatePosition();
                    }
                    if (mouseGameClicksHandler.getMouseClickIsPressed()) {
                        mouseGameClicksHandler.clickPressedHandler();
                    }
                    if (mouseGameClicksHandler.getMouseClickIsReleased()) {
                        mouseGameClicksHandler.clickReleasedHandler();
                    }

                    // Mise à jour de la position de la lumière autour du joueur
                    if (playerLightCircle != null) {
                        double playerCenterX = playerView.getActorSprite().getLayoutX() + playerView.getActorSprite().getTranslateX() + playerView.getActorSprite().getFitWidth() / 2;
                        double playerCenterY = playerView.getActorSprite().getLayoutY() + playerView.getActorSprite().getTranslateY() + playerView.getActorSprite().getFitHeight() / 2;
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
        player = new Player(0, 0, 32, 64, tileMap, 2, 10,3, ActorEnum.PLAYER);
        aliveActors.add(player);
        inventory = player.getInventory();

        hudView = new HUDView(player,heartsHbox);
        playerView = new PlayerView(player,tileMap,globalPane);
        inventoryView = new InventoryView(inventory, hotbarInventory, expandedInventory,hudAnchorPane);
        hotbarView = new HotbarView(hotbarInventory);

        inventory.add(0,new Consumable(Items.RAW_CHICKEN, ConsumableStats.RAW_CHICKEN),100);
        inventory.add(1,new Consumable(Items.RAW_CHICKEN, ConsumableStats.RAW_CHICKEN),45);
        inventory.add(2,new Consumable(Items.RAW_CHICKEN, ConsumableStats.RAW_CHICKEN),20);

        player.healthProperty().addListener(((obs, old, t1) -> hudView.updateHealth()));
        player.isAliveProperty().addListener(((observableValue, aBoolean, t1) -> playerView.deletePlayerSprite()));

        keyboardHandler = new KeyInputHandler(player,inventoryView);

        double playerCenterX = 0/*playerView.getActorSprite().getLayoutX() + playerView.getActorSprite().getTranslateX() + playerView.getActorSprite().getFitWidth() / 2*/;
        double playerCenterY = 0/*playerView.getActorSprite().getLayoutY() + playerView.getActorSprite().getTranslateY() + playerView.getActorSprite().getFitHeight() / 2*/;
        playerLightCircle = new MouseCursorCircleView(globalPane, playerCenterX, playerCenterY, player.getReach()*32, 10);
        playerLightCircle.setCursorVisible(false);

        mouseGameClicksHandler = new MouseGameInputHandler(tileMap,globalView,player,playerLightCircle,inventoryView);
        mouseInventoryHandler = new MouseInventoryInputHandler(inventory,inventoryView);
        scrollHotbarHandler = new ScrollInputHandler(inventory,hotbarView,inventoryView);
        mouseItemActionHandler = new MouseItemActionInputHandler(inventoryView,player,inventory);

        mouseItemActionHandler.onHandItemProperty().bindBidirectional(scrollHotbarHandler.onHandItemProperty());
        mouseItemActionHandler.quantityProperty().bindBidirectional(scrollHotbarHandler.quantityProperty());
        mouseItemActionHandler.modifiedQuantityProperty().addListener((observableValue, itemIntegerHashMap, t1) -> {
            scrollHotbarHandler.removeInventoryQuantity(1);
            scrollHotbarHandler.updateOnHandItem();
        });
        mouseInventoryHandler.onHoldProperty().addListener((observableValue, o, t1) ->
            inventoryView.updateOnHoldPane(mouseInventoryHandler.getOnHold()));
        mouseInventoryHandler.xProperty().addListener((observableValue, number, t1) ->
                inventoryView.updateOnHoldPosition(mouseInventoryHandler.getX(), mouseInventoryHandler.getY()));
        mouseInventoryHandler.yProperty().addListener((observableValue, number, t1) ->
                inventoryView.updateOnHoldPosition(mouseInventoryHandler.getX(), mouseInventoryHandler.getY()));
        scrollHotbarHandler.directionProperty().addListener((observableValue, number, t1) -> {
            if (scrollHotbarHandler.getDirection() != 0)
                scrollHotbarHandler.updateHotbar();
        });


        Platform.runLater(() -> {
            landTileMap.getScene().addEventHandler(KeyEvent.ANY,keyboardHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, mouseGameClicksHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_RELEASED, mouseGameClicksHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseGameClicksHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED,mouseItemActionHandler);
            hudAnchorPane.addEventHandler(MouseEvent.MOUSE_PRESSED,mouseInventoryHandler);
            hudAnchorPane.addEventHandler(MouseEvent.MOUSE_MOVED,mouseInventoryHandler);
            landTileMap.getScene().addEventHandler(ScrollEvent.SCROLL,scrollHotbarHandler);
        });
    }

    private void initmob(){
        this.mob = new Mob(0,0,32,32,tileMap,2,2,15,3,ActorEnum.POULET);
        this.mobView = new MobView(mob,tileMap,globalPane);
        aliveActors.add(mob);
        mob.isAliveProperty().addListener(((observableValue, aBoolean, t1) -> mobView.deletePlayerSprite()));
    }

    private void initActors() {
        aliveActors = new ArrayList<>();
        initPlayer();
        initmob();


    }
}
