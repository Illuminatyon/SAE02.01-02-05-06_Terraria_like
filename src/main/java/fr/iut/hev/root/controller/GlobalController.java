package fr.iut.hev.root.controller;

import fr.iut.hev.root.controller.InputHandling.*;
import fr.iut.hev.root.model.World;
import fr.iut.hev.root.model.craft.RecipesEnum;
import fr.iut.hev.root.model.entities.actor.Player;
import fr.iut.hev.root.model.items.enums.ItemTypesEnum;
import fr.iut.hev.root.model.items.enums.ItemsEnum;
import fr.iut.hev.root.model.utilities.CooldownManager;
import fr.iut.hev.root.view.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * <h2></hé><code>GlobalController</code></h2>
 * <p>Allows model and view to work in sync, bringing the game to life</p>
 */
public class GlobalController implements Initializable {
    private World world;
    private Camera camera;
    private InputHandler inputHandler;
    private CooldownManager cooldownManager;
    private GlobalView globalView;

    @FXML private AnchorPane entitiesPane;
    @FXML private AnchorPane parentPane;
    @FXML private TilePane landTileMap, backgroundTileMap;
    @FXML private AnchorPane hudAnchorPane;
    @FXML private HBox heartsHbox;
    @FXML private GridPane hotbarInventory, expandedInventory;
    @FXML private ListView<RecipesEnum> craftListView;
    @FXML private Button craftButton;
    @FXML private HBox recipeDisplay;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        world = World.getInstance();
        world.initWorld(3840,1440);
        initItemEnums();
        globalView = new GlobalView(landTileMap,backgroundTileMap,entitiesPane,heartsHbox,craftListView,craftButton,recipeDisplay,hotbarInventory,expandedInventory,hudAnchorPane);
        initCamera();
        initInputHandler();
        initGameLoop();
        globalView.initMobViews(camera,entitiesPane);
    }

    /**
     * <h3><code>initGameLoop</code></h3>
     * <p>Initialize program's gameLoop.</p>
     *
     * @see Timeline
     * @see KeyFrame
     */
    private void initGameLoop(){
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        cooldownManager = new CooldownManager();

        KeyFrame fk = new KeyFrame(
                Duration.seconds(0.017),
                ev -> update()
        );
        gameLoop.getKeyFrames().add(fk);
        gameLoop.play();
    }

    /**
     * <h3><code>update</code></h3>
     * <p>Updates every component of the game such as <code>world</code>, <code>camera</code>, <code>inputHandler</code> and <code>cooldownManager</code>.</p>
     *
     * @see World
     * @see Camera
     * @see InputHandler
     * @see CooldownManager
     */
    private void update() {
        world.updateWorld();
        camera.update();
        inputHandler.getMouseItemActionInputHandler().checkMouseInput();
        cooldownManager.allCooldownsTick();
    }

    private void initCamera() {
        camera = new Camera(Player.getInstance(), landTileMap, backgroundTileMap, parentPane, globalView.getLootView(), 0.1);
        globalView.getPlayerView().camOffsetXProperty().bind(camera.currentCamXProperty());
        globalView.getPlayerView().camOffsetYProperty().bind(camera.currentCamYProperty());
    }

    private void initInputHandler() {
        inputHandler = new InputHandler(globalView.getPlayerView().getInventoryView(),globalView.getPlayerView().getCraftView(),camera, globalView.getTileMapView(),globalView.getPlayerView().getHotbarView());
        inputHandler.initInputHandler(landTileMap,hudAnchorPane);
    }

    private void initItemEnums() {
        for (ItemsEnum itemsEnum : ItemsEnum.values()) {
            if (itemsEnum.getItemType().equals(ItemTypesEnum.BLOCK) || itemsEnum.getItemType().equals(ItemTypesEnum.UTILITY)) {
                itemsEnum.itemEnumInit();
            }
        }
    }
}