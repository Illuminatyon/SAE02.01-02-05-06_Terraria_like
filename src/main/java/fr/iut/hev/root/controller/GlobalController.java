package fr.iut.hev.root.controller;

import fr.iut.hev.root.controller.InputHandling.KeyInputHandler;
import fr.iut.hev.root.controller.InputHandling.MouseInventoryInputHandler;
import fr.iut.hev.root.controller.InputHandling.ScrollInputHandler;
import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.controller.Listeners.DeathListener;
import fr.iut.hev.root.model.*;
import fr.iut.hev.root.model.entities.*;
import fr.iut.hev.root.model.enums.*;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import fr.iut.hev.root.model.items.ItemFactory;
import fr.iut.hev.root.model.utilities.Cooldown;
import fr.iut.hev.root.model.utilities.CooldownManager;
import fr.iut.hev.root.view.*;
import fr.iut.hev.root.view.actor.MobView;
import fr.iut.hev.root.view.actor.PlayerView;
import fr.iut.hev.root.view.actor.PnjView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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
import java.util.Set;

public class GlobalController implements Initializable {
    private World world;
    private Camera camera;
    private Timeline gameLoop;
    private Player player; // dans world
    private TileMap tileMap; // dans world
    private MouseInventoryInputHandler mouseInventoryHandler;
    private ScrollInputHandler scrollHotbarHandler;
    private KeyInputHandler keyboardHandler;
    private MouseItemActionInputHandler mouseItemActionHandler;
    private ArrayList<Actor> aliveActors; // dans world
    private Inventory inventory; // dans player
    private CooldownManager cooldownManager; // controller
    private CraftingManager craftingManager; // peut etre dans le joueur
    private HitboxManager hitboxManager; // world ou controller
    //public static Mob mob ;
    private static Set<Mob> mobs;
    private ItemFactory itemFactory;

    private GlobalView globalView; // TODO: Rename to MapView instead for more clarity
    private HUDView hudView;
    private PlayerView playerView;
    private MouseCursorCircleView playerLightCircle;
    private InventoryView inventoryView;
    private HotbarView hotbarView;
    private MobView mobView;
    private MobView aggressiveMobView;
    private PnjView pnjView;
    private CraftView craftView;
    private Cooldown dialogueCD;
    private LootView lootView;

    // AnchorPane for actors that will move with the camera
    @FXML
    private AnchorPane entitiesPane; // was in weapons

    // Idk what is this, name is not clear
    @FXML private AnchorPane globalPane; // TODO: Rename to something more clear MAYBE

    // Map
    @FXML private TilePane landTileMap, backgroundTileMap;

    // HUD
    @FXML private AnchorPane hudAnchorPane;
    @FXML private HBox heartsHbox;
    @FXML private GridPane hotbarInventory, expandedInventory;
    @FXML private ListView<RecipesEnum> craftListView;
    @FXML private Button craftButton;
    @FXML private HBox recipeDisplay;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initWorld();
        initItemEnums();

        gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        cooldownManager = new CooldownManager();
        globalView = new GlobalView(world.getTileMap(), landTileMap, backgroundTileMap);
        lootView = new LootView(entitiesPane);

        // Set the hitbox manager for all weapons
        //Weapon.setHitboxManager(hitboxManager);

        //aliveActors = world.getAliveActors() != null ? world.getAliveActors() : new ArrayList<>();
        //initMap();
        initPlayer();
        System.out.println("crashed ?");
        //createAggressiveMob(ActorEnum.ZOMBIE);
        //createMob(ActorEnum.POULET);
        createNPC(ActorEnum.HOMPS);
        System.out.println("recrashed .");

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),
                (ev -> {
                    world.getPlayer().update();

                    for (int i = world.getAliveMobs().size() - 1; i >= 0; i--) {
                        if (i < world.getAliveMobs().size()) { // Check if index is still valid
                            Actor currentActor = world.getAliveMobs().get(i);
                            if (currentActor != null) {
                                currentActor.updatePosition();
                            } else {
                                world.getAliveMobs().remove(i);
                            }
                        }
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

                    camera.update();
                    checkPnjDialogue();

                    cooldownManager.allCooldownsTick();
                })
        );

        gameLoop.getKeyFrames().add(kf);
        gameLoop.play();
    }

    private void initWorld() {
        hitboxManager = new HitboxManager();
        itemFactory = new ItemFactory(hitboxManager);
        tileMap = new TileMap(1920, 1056, itemFactory);
        player = new Player(0, -25, 32, 64, tileMap, 2, 10, 3, ActorEnum.PLAYER, hitboxManager);
        aliveActors = new ArrayList<>();
        world = new World("Default World", tileMap, player, aliveActors, hitboxManager, itemFactory);
    }

    private void initPlayer() {
        ItemFactory itemFactory = world.getItemFactory();
        player = world.getPlayer();
        inventory = player.getInventory();
        craftingManager = new CraftingManager(inventory,itemFactory);
        hitboxManager.createHitbox(player, HitboxType.VULNERABLE);

        hudView = new HUDView(player.healthProperty(), heartsHbox);
        playerView = new PlayerView(player, world.getTileMap(), entitiesPane);
        craftView = new CraftView(craftListView,craftingManager.getRecipesAvailable(),craftButton,recipeDisplay);
        inventoryView = new InventoryView(inventory, hotbarInventory, expandedInventory,hudAnchorPane,craftView);
        hotbarView = new HotbarView(hotbarInventory);

        camera = new Camera(world.getPlayer(), landTileMap, backgroundTileMap, globalPane, playerView, lootView, 0.1);

        inventory.add(0,itemFactory.createItem(ItemsEnum.RAW_CHICKEN),100);
        inventory.add(1,itemFactory.createItem(ItemsEnum.WOOD),100);
        inventory.add(3,itemFactory.createItem(ItemsEnum.DIRT),100);
        inventory.add(4,itemFactory.createItem(ItemsEnum.FURNACE),100);
        inventory.add(5, itemFactory.createItem(ItemsEnum.KATANA), 1);
        inventory.add(6,itemFactory.createItem(ItemsEnum.DAGGER), 1);
        inventory.add(7,itemFactory.createItem(ItemsEnum.BOW), 1);
        inventory.add(8,itemFactory.createItem(ItemsEnum.ARROW), 64);
        inventory.add(9,itemFactory.createItem(ItemsEnum.WOODEN_PICKAXE),1);
        inventory.add(10,itemFactory.createItem(ItemsEnum.WOODEN_SHOVEL),1);

        //player.healthProperty().addListener(((obs, old, t1) -> hudView.updateHealth(t1)));
        player.healthProperty().addListener(new DeathListener(player, playerView, world.getAliveMobs(), itemFactory));
        // Utiliser un bind pour le deathlistener

        craftingManager.selectedRecipeProperty().bind(craftView.selectedRecipeProperty());
        craftButton.setOnAction(actionEvent -> {
            craftingManager.crafts();
        });

        keyboardHandler = new KeyInputHandler(world, inventoryView,craftView);
        mouseInventoryHandler = new MouseInventoryInputHandler(inventory,inventoryView);
        scrollHotbarHandler = new ScrollInputHandler(inventory,hotbarView,inventoryView);
        mouseItemActionHandler = new MouseItemActionInputHandler(world, camera, inventoryView, globalView);

        double playerCenterX = 0;
        double playerCenterY = 0;
        playerLightCircle = new MouseCursorCircleView(globalPane, playerCenterX, playerCenterY, player.getReach()*32, 10);
        playerLightCircle.setCursorVisible(false);

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

    private void createMob(ActorEnum mobActorEnum) {
        Mob mob = new Mob(0, 0, 32, 32, tileMap, 2, 2, 15, 3, mobActorEnum, hitboxManager);
        mobView = new MobView(mob, tileMap, entitiesPane);
        mob.healthProperty().addListener(new DeathListener(mob, mobView, aliveActors, world.getItemFactory()));
        hitboxManager.createHitbox(mob, HitboxType.VULNERABLE);
        world.getAliveMobs().add(mob);
    }

    // Methode en com dans world
    private void createAggressiveMob(ActorEnum aggressiveMobActorEnum) {
        AggressiveMob aggressiveMob = new AggressiveMob(
                0, 0, 40, 54, tileMap, 5, 1, 15, 10, aggressiveMobActorEnum, player, 20, 1500, aliveActors, entitiesPane, 1, this.hitboxManager // Use actorsPane instead of globalPane
        );
        this.aggressiveMobView = new MobView(aggressiveMob, tileMap, entitiesPane);
        aggressiveMob.healthProperty().addListener(new DeathListener(aggressiveMob, aggressiveMobView, aliveActors,world.getItemFactory()));
        world.getAliveMobs().add(aggressiveMob);
    }

    // Methode en com dans world
    private void createNPC(ActorEnum npcActorEnum) {
        Pnj npc = new Pnj(100, 0,32, 64, tileMap, 2, 2, 10, 3, npcActorEnum, this.hitboxManager);
        this.pnjView = new PnjView(npc, tileMap, entitiesPane);
        npc.healthProperty().addListener(new DeathListener(npc, pnjView, aliveActors,world.getItemFactory()));
        dialogueCD = new Cooldown(0);
        aliveActors.add(npc);
    }

    /**
     * Checks if the player is near a PNJ and triggers dialogue if needed
     */
    private void checkPnjDialogue() {
        if (player == null || pnjView == null || dialogueCD == null) {
            return;
        }

        if ((player.getCollider().hasCollisionRight() || player.getCollider().hasCollisionLeft()) && !dialogueCD.getOnGoing()) {
            pnjView.speak();
            dialogueCD.setLimit(2);
            dialogueCD.start();
        }
    }

    private void initItemEnums() {
        for (ItemsEnum itemsEnum : ItemsEnum.values()) {
            if (itemsEnum.getItemType().equals(ItemTypesEnum.BLOCK) || itemsEnum.getItemType().equals(ItemTypesEnum.UTILITY)) {
                itemsEnum.itemEnumInit();
            }
        }
    }

    /**
     * Gets the item factory
     * @return the item factory
     */
    /*public ItemFactory getItemFactory() {
        return itemFactory;
    }*/

    public Camera getCamera() {
        return camera;
    }
}
