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
import fr.iut.hev.root.model.hitbox.RectangleHitbox;
import fr.iut.hev.root.model.items.ItemFactory;
import fr.iut.hev.root.model.utilities.Cooldown;
import fr.iut.hev.root.model.items.Weapon;
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;

public class GlobalController implements Initializable {
    private ObjectProperty<World> worldProperty;
    private Camera camera;
    private Timeline gameLoop;
    private Player player; // dans world
    private MouseInventoryInputHandler mouseInventoryHandler;
    private ScrollInputHandler scrollHotbarHandler;
    private KeyInputHandler keyboardHandler;
    private MouseItemActionInputHandler mouseItemActionHandler;
    private Inventory inventory; // dans player
    private CooldownManager cooldownManager; // controller
    private CraftingManager craftingManager; // peut etre dans le joueur
    private HitboxManager hitboxManager; // world ou controller
    private static Set<Mob> mobs;

    private GlobalView globalView;
    private HUDView hudView;
    private PlayerView playerView;
    private MouseCursorCircleView playerLightCircle;
    private InventoryView inventoryView;
    private HotbarView hotbarView;


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

    public void setWorld(World world) {
        this.worldProperty.set(world);
        //this.world = world;
    }

    private World getWorld() {
        return worldProperty.get();
    }

    private void lateInit(World world) {
        globalView = new GlobalView(world.getTileMap(), landTileMap, backgroundTileMap);
        lootView = new LootView(landTileMap);

        // Set the hitbox manager for all weapons
        //Weapon.setHitboxManager(hitboxManager);

       // aliveActors = world.getAliveActors() != null ? world.getAliveActors() : new ArrayList<>();
        //initMap();
        initPlayer();
        createMob(ActorEnum.POULET);
        createAggressiveMob(ActorEnum.ZOMBIE);
        createAggressiveMob(ActorEnum.HOMPS);

        //world.getTileMap().setItemFactory(new ItemFactory()); // Pas le choix

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),
                (ev -> {
                    world.getPlayer().update();
                    hitboxManager.updateHitboxPositions(world.getPlayer());

                    for (int i = world.getAliveMobs().size() - 1; i >= 0; i--) {
                        if (i < world.getAliveMobs().size()) { // Check if index is still valid
                            Actor currentActor = world.getAliveMobs().get(i);
                            if (currentActor != null) {
                                currentActor.updatePosition();
                                hitboxManager.updateHitboxPositions(currentActor);
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


                    cooldownManager.allCooldownsTick();
                })
        );

        gameLoop.getKeyFrames().add(kf);
        gameLoop.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        worldProperty = new SimpleObjectProperty<>();
        initItemEnums();

        gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        cooldownManager = new CooldownManager();
        hitboxManager = new HitboxManager();

        worldProperty.addListener((obs, oldWorld, newWorld) -> {
            if (newWorld != null) {
                lateInit(newWorld);
            } else {
                // Retour au menu principal
            }
        });
    }

    private void initPlayer() {
        World world = getWorld();
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

        camera = new Camera(world.getPlayer(), landTileMap, backgroundTileMap, globalPane, playerView, lootView, 0.1, world.getAliveMobs());

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
        Mob mob = new Mob(0, 0, 32, 32, getWorld().getTileMap(), 2, 2, 15, 3, mobActorEnum, hitboxManager);
        MobView mobView = new MobView(mob, getWorld().getTileMap(), entitiesPane);
        mob.setActorView(mobView);
        mob.healthProperty().addListener(new DeathListener(mob, mobView, getWorld().getAliveMobs(), getWorld().getItemFactory()));
        hitboxManager.createHitbox(mob, HitboxType.VULNERABLE);
        getWorld().getAliveMobs().add(mob);
    }

    // Methode en com dans world
    private void createAggressiveMob(ActorEnum aggressiveMobActorEnum) {
        AggressiveMob aggressiveMob = new AggressiveMob(0, 0, 40, 54, getWorld().getTileMap(), 5, 1, 15, 10, ActorEnum.ZOMBIE, player, 20, 1500, getWorld().getAliveMobs(), globalPane, 1, hitboxManager);
        MobView aggressiveMobView = new MobView(aggressiveMob, getWorld().getTileMap(), entitiesPane);
        aggressiveMob.setActorView(aggressiveMobView);
        aggressiveMob.healthProperty().addListener(new DeathListener(aggressiveMob, aggressiveMobView, getWorld().getAliveMobs(),getWorld().getItemFactory()));
        getWorld().getAliveMobs().add(aggressiveMob);
    }

    // Methode en com dans world
    private void createNPC(ActorEnum npcActorEnum) {
        Pnj npc = new Pnj(100, 0,32, 64, getWorld().getTileMap(), 2, 2, 10, 3, npcActorEnum, this.hitboxManager);
        PnjView pnjView = new PnjView(npc, getWorld().getTileMap(), entitiesPane);
        npc.setActorView(pnjView);
        npc.healthProperty().addListener(new DeathListener(npc, pnjView, getWorld().getAliveMobs(),getWorld().getItemFactory()));
        dialogueCD = new Cooldown(0);
        getWorld().getAliveMobs().add(npc);
    }

    /**
     * Checks if the player is near a PNJ and triggers dialogue if needed
     */


    private void initItemEnums() {
        for (ItemsEnum itemsEnum : ItemsEnum.values()) {
            if (itemsEnum.getItemType().equals(ItemTypesEnum.BLOCK) || itemsEnum.getItemType().equals(ItemTypesEnum.UTILITY)) {
                itemsEnum.itemEnumInit();
            }
        }
    }

    /**
     * Gets the list of alive actors
     * @return the list of alive actors
     */
    /*public ArrayList<Actor> getAliveActors() {
        return aliveActors;
    }*/

    /**
     * Gets the actors pane
     * @return the actors pane
     */
    /*public AnchorPane getActorsPane() {
        return actorsPane;
    }*/
    public AnchorPane getEntitiesPane() {
        return entitiesPane;
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
