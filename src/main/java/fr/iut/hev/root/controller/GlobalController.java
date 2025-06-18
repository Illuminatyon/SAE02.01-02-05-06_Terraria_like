package fr.iut.hev.root.controller;

import fr.iut.hev.root.controller.InputHandling.KeyInputHandler;
import fr.iut.hev.root.controller.InputHandling.MouseInventoryInputHandler;
import fr.iut.hev.root.controller.InputHandling.ScrollInputHandler;
import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.controller.Listeners.DeathListener;
import fr.iut.hev.root.model.CraftingManager;
import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.entities.*;
import fr.iut.hev.root.controller.InputHandling.*;
import fr.iut.hev.root.model.enums.ItemTypesEnum;
import fr.iut.hev.root.model.enums.ItemsEnum;
//import fr.iut.hev.root.model.enums.ConsumableStats;
import fr.iut.hev.root.model.enums.DialogueEnum;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.entities.Loot;
import fr.iut.hev.root.model.enums.RecipesEnum;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import fr.iut.hev.root.model.items.ItemFactory;
import fr.iut.hev.root.model.utilities.Cooldown;
import fr.iut.hev.root.model.items.Weapon;
import fr.iut.hev.root.model.utilities.CooldownManager;
import fr.iut.hev.root.view.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static fr.iut.hev.root.model.enums.DialogueEnum.INTRO;

public class GlobalController implements Initializable {
    private Timeline gameLoop;
    private Player player;
    private TileMap tileMap;
    private MouseInventoryInputHandler mouseInventoryHandler;
    private ScrollInputHandler scrollHotbarHandler;
    private KeyInputHandler keyboardHandler;
    private MouseItemActionInputHandler mouseItemActionHandler;
    private ArrayList<Actor> aliveActors;
    private Inventory inventory;
    private CooldownManager cooldownManager;
    private ItemFactory itemFactory;
    private CraftingManager craftingManager;
    private HitboxManager hitboxManager;
    public static Mob mob ;

    private GlobalView globalView;
    private HUDView hudView;
    private PlayerView playerView;
    private MouseCursorCircleView playerLightCircle;
    private InventoryView inventoryView;
    private HotbarView hotbarView;
    private MobView mobView;
    private PnjView pnjView;
    private CraftView craftView;
    private Cooldown dialogueCD;

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

    @FXML
    private ListView<RecipesEnum> craftListView;

    @FXML
    private Button craftButton;

    @FXML
    private HBox recipeDisplay;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        aliveActors = new ArrayList<>();
        gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        LootView lv = new LootView(globalPane);
        cooldownManager = new CooldownManager();
        itemFactory = new ItemFactory();
        hitboxManager = new HitboxManager();

        // Set the hitbox manager for all weapons
        Weapon.setHitboxManager(hitboxManager);

        initItemEnums();
        initMap();
        initActors();

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),
                (ev -> {
                    for (int i = aliveActors.size() - 1; i >= 0; i--) {
                        Actor currentActor = aliveActors.get(i);
                        currentActor.updatePosition();

                        // Update hitbox positions for the actor
                        hitboxManager.updateHitboxPositions(currentActor);
                    }
                    for (Loot loot : Loot.lootOnMapProperty) {
                        loot.updatePosition();
                    }
                    if (mouseItemActionHandler.getMouseClickIsPressed()) {
                        mouseItemActionHandler.onClickPressedLoop();
                    }
                    if (mouseItemActionHandler.getMouseClickIsReleased()) {
                        mouseItemActionHandler.onClickReleasedLoop();
                    }

                    // Mise à jour de la position de la lumière autour du joueur
                    if (playerLightCircle != null) {
                        double playerCenterX = playerView.getActorSprite().getLayoutX() + playerView.getActorSprite().getTranslateX() + playerView.getActorSprite().getFitWidth() / 2;
                        double playerCenterY = playerView.getActorSprite().getLayoutY() + playerView.getActorSprite().getTranslateY() + playerView.getActorSprite().getFitHeight() / 2;
                        playerLightCircle.updateCenter(playerCenterX, playerCenterY);
                    }
                    if ((this.player.getCollider().hasCollisionRight() ||this.player.getCollider().hasCollisionLeft() ) && !dialogueCD.getOnGoing()) {
                        pnjView.speak();
                        this.dialogueCD.setLimit(2);
                        this.dialogueCD.start();
                        System.out.println(pnjView.getPhrase());
                    }

                    cooldownManager.allCooldownsTick();
                })
        );
        gameLoop.getKeyFrames().add(kf);

        gameLoop.play();
    }

    private void initMap() {
        tileMap = new TileMap(1920,1056,itemFactory);
        globalView = new GlobalView(tileMap, landTileMap,backgroundTileMap);
    }

    private void initPlayer() {
        player = new Player(0, 0, 32, 64, tileMap, 2, 10,3, ActorEnum.PLAYER);
        aliveActors.add(player);
        inventory = player.getInventory();
        craftingManager = new CraftingManager(inventory,itemFactory);

        // Create a vulnerable hitbox for the player
        hitboxManager.createDefaultVulnerableHitbox(player);


        hudView = new HUDView(player.getHealth(),heartsHbox);
        playerView = new PlayerView(player,tileMap,globalPane);
        craftView = new CraftView(craftListView,craftingManager.getRecipesAvailable(),craftButton,recipeDisplay);
        inventoryView = new InventoryView(inventory, hotbarInventory, expandedInventory,hudAnchorPane,craftView);
        hotbarView = new HotbarView(hotbarInventory);


        inventory.add(0,itemFactory.createItem(ItemsEnum.RAW_CHICKEN),100);
        inventory.add(1,itemFactory.createItem(ItemsEnum.RAW_CHICKEN),45);
        inventory.add(2,itemFactory.createItem(ItemsEnum.RAW_CHICKEN),20);
        inventory.add(3,itemFactory.createItem(ItemsEnum.DIRT),100);
        inventory.add(4,itemFactory.createItem(ItemsEnum.WOOD),100);
        inventory.add(5,itemFactory.createItem(ItemsEnum.IRON_INGOT),100);
        inventory.add(6,itemFactory.createItem(ItemsEnum.STONE),100);

        player.healthProperty().addListener(((obs, old, t1) -> hudView.updateHealth(t1)));
        player.healthProperty().addListener(new DeathListener(player,playerView,aliveActors));
        craftingManager.selectedRecipeProperty().bind(craftView.selectedRecipeProperty());
        craftButton.setOnAction(actionEvent -> {
            craftingManager.crafts();
        });

        keyboardHandler = new KeyInputHandler(player,inventoryView,craftView);

        double playerCenterX = 0/*playerView.getActorSprite().getLayoutX() + playerView.getActorSprite().getTranslateX() + playerView.getActorSprite().getFitWidth() / 2*/;
        double playerCenterY = 0/*playerView.getActorSprite().getLayoutY() + playerView.getActorSprite().getTranslateY() + playerView.getActorSprite().getFitHeight() / 2*/;
        playerLightCircle = new MouseCursorCircleView(globalPane, playerCenterX, playerCenterY, player.getReach()*32, 10);
        playerLightCircle.setCursorVisible(false);

        mouseInventoryHandler = new MouseInventoryInputHandler(inventory,inventoryView);
        scrollHotbarHandler = new ScrollInputHandler(inventory,hotbarView,inventoryView);
        mouseItemActionHandler = new MouseItemActionInputHandler(inventoryView,player,globalView,tileMap);

        player.itemInHandProperty().bindBidirectional(scrollHotbarHandler.onHandItemProperty());
        player.quantityOfItemInHandProperty().bindBidirectional(scrollHotbarHandler.quantityProperty());
        player.indexItemInHandProperty().bind(scrollHotbarHandler.IndexHotbarProperty());
        player.itemInHandProperty().addListener((observableValue, item, t1) -> mouseItemActionHandler.updateCooldown());
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
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED,mouseItemActionHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_RELEASED,mouseItemActionHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED,mouseItemActionHandler);
            hudAnchorPane.addEventHandler(MouseEvent.MOUSE_PRESSED,mouseInventoryHandler);
            hudAnchorPane.addEventHandler(MouseEvent.MOUSE_MOVED,mouseInventoryHandler);
            landTileMap.getScene().addEventHandler(ScrollEvent.SCROLL,scrollHotbarHandler);
        });
    }

    private void initmob() {
        this.mob = new Mob(0, 0, 32, 32, tileMap, 2, 2, 15, 3, ActorEnum.POULET);
        this.mobView = new MobView(mob, tileMap, globalPane);
        mob.healthProperty().addListener(new DeathListener(mob, mobView, aliveActors));
        aliveActors.add(mob);

        // Create a vulnerable hitbox for the mob
        hitboxManager.createDefaultVulnerableHitbox(mob);
    }

    private void initActors() {
        aliveActors = new ArrayList<>();
        initPlayer();
        initmob();
        initAggressiveMob(player);
        initPnj();
    }

    private void initAggressiveMob(Player player) {
        AggressiveMob aggressiveMob = new AggressiveMob(
                0, 0, 40, 54, tileMap, 5, 1, 15, 10, ActorEnum.ZOMBIE, player, 20, 1500, aliveActors, globalPane, 1
        );
        MobView mobView = new MobView(aggressiveMob, tileMap, globalPane);
        aggressiveMob.healthProperty().addListener(new DeathListener(aggressiveMob, mobView, aliveActors));
        aliveActors.add(aggressiveMob);
    }
    private void initPnj() {
        Pnj homps = new Pnj(100, 0,32, 64, tileMap, 2, 2, 10, 3, ActorEnum.HOMPS);
        this.pnjView = new PnjView(homps, tileMap, globalPane);
        homps.healthProperty().addListener(new DeathListener(homps, pnjView, aliveActors));
        aliveActors.add(homps);
        dialogueCD = new Cooldown(0);
        // ajouter le listener sur les colision avec le joueur a la place du check de colison des mur de cons
        /*if ((this.player.getCollider().hasCollisionRight() ||this.player.getCollider().hasCollisionLeft() ) && !dialogueCD.getOnGoing()) {
            pnjView.speak();
            this.dialogueCD.setLimit(2);
            this.dialogueCD.start();
            System.out.println(pnjView.getPhrase());
        }*/
    }

    private void initItemEnums() {
        for (ItemsEnum itemsEnum : ItemsEnum.values()) {
            if (itemsEnum.getItemType().equals(ItemTypesEnum.BLOCK) || itemsEnum.getItemType().equals(ItemTypesEnum.UTILITY)) {
                itemsEnum.itemEnumInit();
            }
        }
    }
}
