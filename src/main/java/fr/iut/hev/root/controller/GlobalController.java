package fr.iut.hev.root.controller;

import fr.iut.hev.root.controller.InputHandling.*;
import fr.iut.hev.root.controller.Listeners.DeathListener;
import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.entities.*;
import fr.iut.hev.root.model.enums.ConsumableStats;
import fr.iut.hev.root.model.enums.Items;
import fr.iut.hev.root.model.enums.ActorEnum;
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
    public static Mob mob;
    private GlobalView globalView;
    private HUDView hudView;
    private PlayerView playerView;
    private MouseCursorCircleView playerLightCircle;
    private InventoryView inventoryView;
    private HotbarView hotbarView;
    private MobView mobView;
    private int frameCount = 0; // Compteur de frames pour limiter les mises à jour
    private boolean gamePaused = false; // Indique si le jeu est en pause

    // Ajoutez ces variables pour la minimap
    private MinimapView minimapView;
    private MouseMinimapInputHandler mouseMinimapInputHandler;

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
        aliveActors = new ArrayList<>();
        gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        LootView lv = new LootView(globalPane);

        initMap();
        initActors();
        InventoryView Inventory = new InventoryView(inventory, hotbarInventory, expandedInventory, hudAnchorPane);
        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),
                (ev -> {
                    // Ne mettre à jour les positions que si le jeu n'est pas en pause
                    if (!gamePaused) {
                        for (int i = aliveActors.size() - 1; i >= 0; i--) {
                            Actor currentActor = aliveActors.get(i);
                            currentActor.updatePosition();
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
                    }

                    // Mise à jour constante de la minimap (toutes les 5 frames pour éviter les problèmes de performance)
                    // La minimap est toujours mise à jour, même en pause
                    if (minimapView != null) {
                        // Incrémenter le compteur de frames
                        frameCount++;

                        // Utiliser un compteur pour limiter les mises à jour
                        if (frameCount % 5 == 0) {
                            minimapView.render();
                        }
                        // Toujours mettre à jour le chemin
                        minimapView.updatePathBasedOnPlayerPosition();
                    }
                })
        );
        gameLoop.getKeyFrames().add(kf);

        gameLoop.play();
    }

    private void initMap() {
        tileMap = new TileMap(1920,1056);
        globalView = new GlobalView(tileMap, landTileMap,backgroundTileMap);
    }

    private void initPlayer() {
        player = new Player(0, 0, 32, 64, tileMap, 2, 10,3, ActorEnum.PLAYER);
        aliveActors.add(player);
        inventory = player.getInventory();
        hudView = new HUDView(player.getHealth(), heartsHbox);
        playerView = new PlayerView(player, tileMap, globalPane);
        inventoryView = new InventoryView(inventory, hotbarInventory, expandedInventory, hudAnchorPane);

        // Écouteur pour le mouvement du joueur
        player.isMovingProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("GlobalController Listener: Player isMoving changed to " + newVal);
            if (newVal) { // Si le joueur commence à bouger
                playerView.setMovingGif();
            } else { // Si le joueur s'arrête de bouger
                playerView.setStaticImage();
            }
        });
        hotbarView = new HotbarView(hotbarInventory);

        // Crée playerLightCircle AVANT mouseGameClicksHandler
        double playerCenterX = playerView.getActorSprite().getLayoutX()
                + playerView.getActorSprite().getTranslateX()
                + playerView.getActorSprite().getFitWidth() / 2;
        double playerCenterY = playerView.getActorSprite().getLayoutY()
                + playerView.getActorSprite().getTranslateY()
                + playerView.getActorSprite().getFitHeight() / 2;
        playerLightCircle = new MouseCursorCircleView(globalPane, playerCenterX, playerCenterY, player.getReach() * 32, 10);
        playerLightCircle.setCursorVisible(false);

        // Ajoutez la MinimapView
        minimapView = new MinimapView(tileMap, player, aliveActors);
        globalPane.getChildren().add(minimapView);

        // Set a higher z-index for the minimap
        minimapView.setViewOrder(-1); // Lower values appear in front

        // Positionnez la minimap dans le coin supérieur droit
        // Ajustez les ancres pour positionner correctement la minimap
        AnchorPane.setRightAnchor(minimapView, 10.0);
        AnchorPane.setTopAnchor(minimapView, 10.0);

        // Créez le gestionnaire pour la minimap
        mouseMinimapInputHandler = new MouseMinimapInputHandler(minimapView, player, tileMap);

        // Ajoutez le gestionnaire de la minimap au canvas de la minimap
        minimapView.getCanvas().setOnMouseClicked(mouseMinimapInputHandler);

        // Initialise mouseGameClicksHandler avec playerLightCircle disponible
        mouseGameClicksHandler = new MouseGameInputHandler(tileMap, globalView, player, playerLightCircle, inventoryView);
        mouseInventoryHandler = new MouseInventoryInputHandler(inventory, inventoryView);
        scrollHotbarHandler = new ScrollInputHandler(inventory, hotbarView, inventoryView);
        mouseItemActionHandler = new MouseItemActionInputHandler(inventoryView, player, inventory);

        player.healthProperty().addListener(((obs, old, t1) -> hudView.updateHealth(t1)));
        player.healthProperty().addListener(new DeathListener(player, playerView, aliveActors));

        keyboardHandler = new KeyInputHandler(player, inventoryView, minimapView, this);

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
        mob.healthProperty().addListener(new DeathListener(mob,mobView,aliveActors));
        aliveActors.add(mob);
    }

    private void initActors() {
        aliveActors = new ArrayList<>();
        initPlayer();
        initmob();
        initAggressiveMob(player);
    }

    private void initAggressiveMob(Player player) {
        AggressiveMob aggressiveMob = new AggressiveMob(
                0, 0, 32, 32, tileMap, 5, 1, 15, 10, ActorEnum.ZOMBIE, player, 20, 1500, aliveActors, globalPane, 1
        );
        MobView mobView = new MobView(aggressiveMob, tileMap, globalPane);
        aggressiveMob.healthProperty().addListener(new DeathListener(aggressiveMob, mobView, aliveActors));
        aliveActors.add(aggressiveMob);
    }

    /**
     * Met le jeu en pause
     */
    public void pauseGame() {
        gamePaused = true;
        System.out.println("Jeu mis en pause");
    }

    /**
     * Reprend le jeu après une pause
     */
    public void resumeGame() {
        gamePaused = false;
        System.out.println("Jeu repris");
    }

    /**
     * Bascule entre pause et reprise du jeu
     */
    public void togglePause() {
        gamePaused = !gamePaused;
        System.out.println("État de pause basculé: " + (gamePaused ? "En pause" : "En cours"));
    }

    /**
     * Vérifie si le jeu est en pause
     * @return true si le jeu est en pause, false sinon
     */
    public boolean isGamePaused() {
        return gamePaused;
    }
}
