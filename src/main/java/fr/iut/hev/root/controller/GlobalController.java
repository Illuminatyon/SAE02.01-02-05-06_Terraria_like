package fr.iut.hev.root.controller;

import fr.iut.hev.root.controller.InputHandling.KeyInputHandler;
import fr.iut.hev.root.controller.InputHandling.MouseInventoryInputHandler;
import fr.iut.hev.root.controller.InputHandling.ScrollInputHandler;
import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.controller.Listeners.DeathListener;
import fr.iut.hev.root.model.CraftingManager;
import fr.iut.hev.root.model.Tile;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.entities.*;
import fr.iut.hev.root.model.enums.ItemTypesEnum;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.enums.RecipesEnum;
import fr.iut.hev.root.model.enums.TilesEnum;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import fr.iut.hev.root.model.items.ItemFactory;
import fr.iut.hev.root.model.items.Weapon;
import fr.iut.hev.root.model.utilities.Cooldown;
import fr.iut.hev.root.model.*;
import fr.iut.hev.root.model.utilities.CooldownManager;
import fr.iut.hev.root.view.*;
import fr.iut.hev.root.view.actor.MobView;
import fr.iut.hev.root.view.actor.PlayerView;
import fr.iut.hev.root.view.actor.PnjView;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GlobalController implements Initializable {
    private World world;
    private Camera camera;
    private Timeline gameLoop;
    private MouseInventoryInputHandler mouseInventoryHandler;
    private ScrollInputHandler scrollHotbarHandler;
    private KeyInputHandler keyboardHandler;
    private MouseItemActionInputHandler mouseItemActionHandler;
    private CooldownManager cooldownManager; // controller
    //private ItemFactory itemFactory;
    private CraftingManager craftingManager; // peut etre dans le joueur
    private HitboxManager hitboxManager; // world ou controller
    public static Mob mob ;

    // Variables for the scrolling camera
    //private double cameraOffsetX = 0;
    //private double cameraOffsetY = 0;

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
    private Cooldown dialogueCD; // Recheck c'est quoi ca parce que chelou le nom
    private LootView lootView;

    // AnchorPane for actors that will move with the camera
    private AnchorPane actorsPane;

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
        this.world = world;
    }

    public void lateInit() {
        gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        // Initialize actorsPane
        actorsPane = new AnchorPane();
        globalPane.getChildren().add(actorsPane);

        // Init item enums
        for (ItemsEnum itemsEnum : ItemsEnum.values()) {
            if (itemsEnum.getItemType().equals(ItemTypesEnum.BLOCK) || itemsEnum.getItemType().equals(ItemTypesEnum.UTILITY)) {
                itemsEnum.itemEnumInit();
            }
        }

        cooldownManager = new CooldownManager();
        craftingManager = new CraftingManager(world.getPlayer().getInventory(), world.getItemFactory()); // Vrm chelou tt ca
        hitboxManager = new HitboxManager();
        Weapon.setHitboxManager(hitboxManager);

        //lootView = new LootView(actorsPane); // Use actorsPane instead of globalPane
        //cooldownManager = new CooldownManager();
        //itemFactory = new ItemFactory();
        //hitboxManager = new HitboxManager();
        globalView = new GlobalView(world.getTileMap(), landTileMap, backgroundTileMap);
        //hudView = new HUDView(world.getPlayer().getHealth(), heartsHbox);
        lootView = new LootView(landTileMap);
        //playerView = new PlayerView(world.getPlayer(), world.getTileMap(), globalPane);

        // Set the hitbox manager for all weapons
        //Weapon.setHitboxManager(hitboxManager);

        //aliveActors = world.getAliveActors() != null ? world.getAliveActors() : new ArrayList<>();
        //initMap();
        initPlayer();
        camera = new Camera(world.getPlayer(), landTileMap, backgroundTileMap, globalPane, playerView, lootView, 0.1);
        /*playerView = new PlayerView(world.getPlayer(), world.getTileMap(), actorsPane);
        craftView = new CraftView(craftListView, craftingManager.getRecipesAvailable(), craftButton, recipeDisplay);
        inventoryView = new InventoryView(world.getPlayer().getInventory(), hotbarInventory, expandedInventory, hudAnchorPane, craftView);
        hotbarView = new HotbarView(hotbarInventory);*/

        keyboardHandler = new KeyInputHandler(world, inventoryView, craftView);
        mouseInventoryHandler = new MouseInventoryInputHandler(world.getPlayer().getInventory(), inventoryView);
        scrollHotbarHandler = new ScrollInputHandler(world.getPlayer().getInventory(), hotbarView, inventoryView);
        mouseItemActionHandler = new MouseItemActionInputHandler(world, inventoryView, globalView);

        // Initialize camera position
        //updateCameraPosition();

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),
                (ev -> {
                    world.getPlayer().update();
                    hitboxManager.updateHitboxPositions(world.getPlayer());
                    for (int i = world.getAliveActors().size() - 1; i >= 0; i--) {
                        if (i < world.getAliveActors().size()) { // Check if index is still valid
                            Actor currentActor = world.getAliveActors().get(i);
                            if (currentActor != null) { // Check if actor is not null
                                currentActor.updatePosition();
                                // Update hitbox positions for the actor
                                hitboxManager.updateHitboxPositions(currentActor);
                            } else {
                                // Remove null actors from the list
                                world.getAliveActors().remove(i);
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

                    // Update camera position to follow the player
                    //updateCameraPosition();
                    camera.update();

                    // Check if player is near crafting stations
                    //checkCraftingStationProximity();

                    // Check if player is near PNJ for dialogue
                    checkPnjDialogue();

                    cooldownManager.allCooldownsTick();
                })
        );
        gameLoop.getKeyFrames().add(kf);

        gameLoop.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /*private void initMap() {
        tileMap = new TileMap(1920,1056,itemFactory);
        //tileMap = new TileMap(1920,1056);
        tileMap = world.getTileMap();
        globalView = new GlobalView(tileMap, landTileMap,backgroundTileMap);
    }*/

    private void initPlayer() {
        //player = new Player(0, 0, 32, 64, tileMap, 2, 10,3, ActorEnum.PLAYER);
        Player player = world.getPlayer();
        //world.getAliveActors().add(player); // Si gerer dedans duplication de la save du joueur dans entities
        //aliveActors.add(player);
        //inventory = player.getInventory();
        Inventory inventory = player.getInventory();
        ItemFactory itemFactory = world.getItemFactory();
        craftingManager = new CraftingManager(inventory, itemFactory);

        // Create a vulnerable hitbox for the player
        hitboxManager.createDefaultVulnerableHitbox(player);

        hudView = new HUDView(player.healthProperty(), heartsHbox);
        playerView = new PlayerView(player, world.getTileMap(), actorsPane);
        craftView = new CraftView(craftListView,craftingManager.getRecipesAvailable(),craftButton,recipeDisplay);
        inventoryView = new InventoryView(inventory, hotbarInventory, expandedInventory,hudAnchorPane,craftView);
        hotbarView = new HotbarView(hotbarInventory);


        inventory.add(0, itemFactory.createItem(ItemsEnum.RAW_CHICKEN),100);
        inventory.add(1, itemFactory.createItem(ItemsEnum.RAW_CHICKEN),45);
        inventory.add(2, itemFactory.createItem(ItemsEnum.RAW_CHICKEN),20);
        inventory.add(3, itemFactory.createItem(ItemsEnum.DIRT),100);
        inventory.add(4, itemFactory.createItem(ItemsEnum.FURNACE),100);
        inventory.add(5, itemFactory.createItem(ItemsEnum.KATANA), 1);
        inventory.add(6, itemFactory.createItem(ItemsEnum.DAGGER), 1);
        inventory.add(7, itemFactory.createItem(ItemsEnum.BOW), 1);
        inventory.add(8, itemFactory.createItem(ItemsEnum.ARROW), 64);
        inventory.add(9, itemFactory.createItem(ItemsEnum.WOODEN_PICKAXE),1);
        inventory.add(10, itemFactory.createItem(ItemsEnum.WOODEN_SHOVEL),1);

        //player.healthProperty().addListener(((obs, old, t1) -> hudView.updateHealth(t1)));
        player.healthProperty().addListener(new DeathListener(player, playerView, world.getAliveActors()));
        // Utiliser un bind pour le deathlistener

        craftingManager.selectedRecipeProperty().bind(craftView.selectedRecipeProperty());
        craftButton.setOnAction(actionEvent -> {
            craftingManager.crafts();
        });
        //KeyInputHandler keyboardHandler = new KeyInputHandler(player);
        //KeyInputHandler keyboardHandler = new KeyInputHandler(world);

        keyboardHandler = new KeyInputHandler(world, inventoryView, craftView);

        double playerCenterX = 0;
        double playerCenterY = 0;
        playerLightCircle = new MouseCursorCircleView(globalPane, playerCenterX, playerCenterY, player.getReach()*32, 10);
        playerLightCircle.setCursorVisible(false);

        mouseInventoryHandler = new MouseInventoryInputHandler(inventory,inventoryView);
        scrollHotbarHandler = new ScrollInputHandler(inventory,hotbarView,inventoryView);
        mouseItemActionHandler = new MouseItemActionInputHandler(world, inventoryView, globalView); ////////////////////////////////////////////////////////////////

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

    /*private void initmob() {
        this.mob = new Mob(0, 0, 32, 32, tileMap, 2, 2, 15, 3, ActorEnum.POULET);
        this.mobView = new MobView(mob, tileMap, actorsPane); // Use actorsPane instead of globalPane
        mob.healthProperty().addListener(new DeathListener(mob, mobView, aliveActors));
        aliveActors.add(mob);

        // Create a vulnerable hitbox for the mob
        hitboxManager.createDefaultVulnerableHitbox(mob);
    }*/

    /*private void initActors() {
        aliveActors = new ArrayList<>();
        initPlayer();
        initmob();
        initAggressiveMob(player);
        initPnj();
    }*/

    /*private void initAggressiveMob(Player player) {
        AggressiveMob aggressiveMob = new AggressiveMob(
                0, 0, 40, 54, tileMap, 5, 1, 15, 10, ActorEnum.ZOMBIE, player, 20, 1500, aliveActors, actorsPane, 1 // Use actorsPane instead of globalPane
        );
        this.aggressiveMobView = new MobView(aggressiveMob, tileMap, actorsPane); // Use actorsPane instead of globalPane
        aggressiveMob.healthProperty().addListener(new DeathListener(aggressiveMob, aggressiveMobView, aliveActors));
        aliveActors.add(aggressiveMob);
    }*/

    /*private void initPnj() {
        Pnj homps = new Pnj(100, 0,32, 64, tileMap, 2, 2, 10, 3, ActorEnum.HOMPS);
        this.pnjView = new PnjView(homps, tileMap, actorsPane); // Use actorsPane instead of globalPane
        homps.healthProperty().addListener(new DeathListener(homps, pnjView, aliveActors));
        aliveActors.add(homps);
        dialogueCD = new Cooldown(0);
    }*/

    /**
     * Checks if the player is near a PNJ and triggers dialogue if needed
     */
    private void checkPnjDialogue() {
        if (world.getPlayer() == null || pnjView == null || dialogueCD == null) {
            return;
        }

        if ((world.getPlayer().getCollider().hasCollisionRight() || world.getPlayer().getCollider().hasCollisionLeft()) && !dialogueCD.getOnGoing()) {
            pnjView.speak();
            dialogueCD.setLimit(2);
            dialogueCD.start();
        }
    }

    /*private void initItemEnums() {
        for (ItemsEnum itemsEnum : ItemsEnum.values()) {
            if (itemsEnum.getItemType().equals(ItemTypesEnum.BLOCK) || itemsEnum.getItemType().equals(ItemTypesEnum.UTILITY)) {
                itemsEnum.itemEnumInit();
            }
        }
    }*/

    /**
     * Updates the camera position to center on the player
     */
    private double currentOffsetX = 0;
    private double currentOffsetY = 0;
    private void updateCameraPosition() {
        Player player = world.getPlayer();
        if (player == null || landTileMap == null || backgroundTileMap == null) {
            return;
        }

        // Calculate the center of the screen
        double screenWidth = globalPane.getWidth();
        double screenHeight = globalPane.getHeight();

        // Calculate the player's center position
        double playerCenterX = player.getPosX() + player.getWidth() / 2;
        double playerCenterY = player.getPosY() + player.getHeight() / 2;

        // Calculate the camera offset to center the player
        /*double cameraOffsetX = (screenWidth / 2) - playerCenterX;
        double cameraOffsetY = (screenHeight / 2) - playerCenterY;*/
        double targetOffsetX = (screenWidth / 2) - playerCenterX;
        double targetOffsetY = (screenHeight / 2) - playerCenterY;

        currentOffsetX += (targetOffsetX - currentOffsetX) * 0.1; // Smooth transition
        currentOffsetY += (targetOffsetY - currentOffsetY) * 0.1; // Smooth transition

        // Apply the camera offset to the tile maps
        landTileMap.setTranslateX(currentOffsetX);
        landTileMap.setTranslateY(currentOffsetY);
        backgroundTileMap.setTranslateX(currentOffsetX);
        backgroundTileMap.setTranslateY(currentOffsetY);

        // TODO: adapter avec des actorview pour eviter de faire plein de if
        // Update the positions of all actors based on the camera offset
        /*for (Actor actor : aliveActors) {
            if (actor == null) {
                // Skip null actors
                continue;
            }

            if (actor.equals(player)) {
                // Update player position
                if (playerView != null && playerView.getActorSprite() != null) {
                    playerView.getActorSprite().setLayoutX(actor.getPosX() + cameraOffsetX);
                    playerView.getActorSprite().setLayoutY(actor.getPosY() + cameraOffsetY);
                }
            } else {
                // Update other actors' positions
                if (actor == mob && mobView != null && mobView.getActorSprite() != null) {
                    mobView.getActorSprite().setLayoutX(actor.getPosX() + cameraOffsetX);
                    mobView.getActorSprite().setLayoutY(actor.getPosY() + cameraOffsetY);
                } else if (actor instanceof AggressiveMob && aggressiveMobView != null && aggressiveMobView.getActorSprite() != null) {
                    aggressiveMobView.getActorSprite().setLayoutX(actor.getPosX() + cameraOffsetX);
                    aggressiveMobView.getActorSprite().setLayoutY(actor.getPosY() + cameraOffsetY);
                } else if (pnjView != null && pnjView.getActorSprite() != null && actor.getName().equals("homps")) {
                    pnjView.getActorSprite().setLayoutX(actor.getPosX() + cameraOffsetX);
                    pnjView.getActorSprite().setLayoutY(actor.getPosY() + cameraOffsetY);
                }
                // Note: Arrow actors are handled by their own view class and don't need special handling here
            }
        }*/

        // TMP - Met a jour le joueur uniquement
       playerView.getActorSprite().setLayoutX(player.getPosX() + currentOffsetX);
       playerView.getActorSprite().setLayoutY(player.getPosY() + currentOffsetY);

        // Update loot positions with camera offset
        if (lootView != null) {
            lootView.updateLootPositions(currentOffsetX, currentOffsetY);
        }
    }

    /**
     * Gets the current camera offset X
     * @return the camera offset X
     */
    /*public double getCameraOffsetX() {
        return cameraOffsetX;
    }*/

    /**
     * Gets the current camera offset Y
     * @return the camera offset Y
     */
    /*public double getCameraOffsetY() {
        return cameraOffsetY;
    }*/

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

    /**
     * Gets the item factory
     * @return the item factory
     */
    /*public ItemFactory getItemFactory() {
        return itemFactory;
    }*/

    /**
     * Checks if the player is near any crafting stations (furnace or crafting table)
     * and updates the crafting manager accordingly
     */
    /*private void checkCraftingStationProximity() {
        if (player == null || tileMap == null || craftingManager == null) {
            return;
        }

        // Get player position in tile coordinates
        int playerTileX = (int) (player.getPosX() / TileMap.format);
        int playerTileY = (int) (player.getPosY() / TileMap.format);

        // Check a 3x3 area around the player for crafting stations
        boolean foundCraftingTable = false;
        boolean foundFurnace = false;

        for (int x = playerTileX - 3; x <= playerTileX + 3; x++) {
            for (int y = playerTileY - 3; y <= playerTileY + 3; y++) {
                // Skip if out of bounds
                if (x < 0 || y < 0 || x >= tileMap.getWidth() || y >= tileMap.getHeight()) {
                    continue;
                }

                // Get the tile at this position
                Tile tile = tileMap.getTile(x, y);
                if (tile != null && tile.getTileEnum() != null) {
                    // Check if it's a crafting table
                    if (tile.getTileEnum() == TilesEnum.CRAFTING_TABLE) {
                        foundCraftingTable = true;
                    }
                    // Check if it's a furnace
                    else if (tile.getTileEnum() == TilesEnum.FURNACE) {
                        foundFurnace = true;
                    }
                }
            }
        }

        // Update the crafting manager
        craftingManager.setNearCraftingTable(foundCraftingTable);
        craftingManager.setNearFurnace(foundFurnace);
    }*/
}
