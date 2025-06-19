package fr.iut.hev.root.controller;

import fr.iut.hev.root.controller.InputHandling.KeyInputHandler;
import fr.iut.hev.root.controller.InputHandling.MouseInventoryInputHandler;
import fr.iut.hev.root.controller.InputHandling.ScrollInputHandler;
import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.controller.Listeners.DeathListener;
import fr.iut.hev.root.model.CraftingManager;
import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.Tile;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.entities.*;
import fr.iut.hev.root.model.enums.*;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import fr.iut.hev.root.model.items.ItemFactory;
import fr.iut.hev.root.model.utilities.Cooldown;
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

    // Variables for the scrolling camera
    private double cameraOffsetX = 0;
    private double cameraOffsetY = 0;

    private GlobalView globalView;
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

    @FXML
    private TilePane backgroundTileMap;

    @FXML
    private TilePane landTileMap;

    @FXML
    private HBox heartsHbox;

    @FXML
    private AnchorPane globalPane;

    @FXML
    private AnchorPane entitiesPane;

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

        lootView = new LootView(entitiesPane);
        cooldownManager = new CooldownManager();
        itemFactory = new ItemFactory(hitboxManager);
        hitboxManager = new HitboxManager();

        initItemEnums();
        initMap();
        initActors();

        updateCameraPosition();

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.017),
                (ev -> {
                    for (int i = aliveActors.size() - 1; i >= 0; i--) {
                        if (i < aliveActors.size()) {
                            Actor currentActor = aliveActors.get(i);
                            if (currentActor != null) {
                                currentActor.updatePosition();
                                hitboxManager.updateHitboxPositions(currentActor);
                            } else {
                                aliveActors.remove(i);
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

                    if (playerLightCircle != null) {
                        double playerCenterX = playerView.getActorSprite().getLayoutX() + playerView.getActorSprite().getTranslateX() + playerView.getActorSprite().getFitWidth() / 2;
                        double playerCenterY = playerView.getActorSprite().getLayoutY() + playerView.getActorSprite().getTranslateY() + playerView.getActorSprite().getFitHeight() / 2;
                        playerLightCircle.updateCenter(playerCenterX, playerCenterY);
                    }

                    updateCameraPosition();
                    checkCraftingStationProximity();
                    checkPnjDialogue();

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

        hitboxManager.createHitbox(player, HitboxType.VULNERABLE);


        hudView = new HUDView(player.getHealth(),heartsHbox);
        playerView = new PlayerView(player,tileMap, entitiesPane);
        craftView = new CraftView(craftListView,craftingManager.getRecipesAvailable(),craftButton,recipeDisplay);
        inventoryView = new InventoryView(inventory, player.getArmorInventory(), hotbarInventory, expandedInventory, hudAnchorPane, craftView);
        hotbarView = new HotbarView(hotbarInventory);


        inventory.add(0,itemFactory.createItem(ItemsEnum.RAW_CHICKEN),100);
        inventory.add(1,itemFactory.createItem(ItemsEnum.RAW_CHICKEN),1);
        inventory.add(2,itemFactory.createItem(ItemsEnum.IRON_CHESTPLATE),1);
        inventory.add(3,itemFactory.createItem(ItemsEnum.DIRT),100);
        inventory.add(4,itemFactory.createItem(ItemsEnum.FURNACE),100);
        inventory.add(5, itemFactory.createItem(ItemsEnum.KATANA), 1);
        inventory.add(6,itemFactory.createItem(ItemsEnum.DAGGER), 1);
        inventory.add(7,itemFactory.createItem(ItemsEnum.BOW), 1);
        inventory.add(8,itemFactory.createItem(ItemsEnum.ARROW), 64);
        inventory.add(9,itemFactory.createItem(ItemsEnum.WOODEN_PICKAXE),1);
        inventory.add(10,itemFactory.createItem(ItemsEnum.WOODEN_SHOVEL),1);

        player.healthProperty().addListener(((obs, old, t1) -> hudView.updateHealth(t1)));
        player.healthProperty().addListener(new DeathListener(player,playerView,aliveActors,itemFactory));
        craftingManager.selectedRecipeProperty().bind(craftView.selectedRecipeProperty());
        craftButton.setOnAction(actionEvent -> {
            craftingManager.crafts();
        });

        keyboardHandler = new KeyInputHandler(player,inventoryView,craftView);

        double playerCenterX = 0;
        double playerCenterY = 0;
        playerLightCircle = new MouseCursorCircleView(globalPane, playerCenterX, playerCenterY, player.getReach()*32, 10);
        playerLightCircle.setCursorVisible(false);

        mouseInventoryHandler = new MouseInventoryInputHandler(inventory,inventoryView);
        mouseInventoryHandler.setArmorInventory(player.getArmorInventory());
        scrollHotbarHandler = new ScrollInputHandler(inventory,hotbarView,inventoryView);
        mouseItemActionHandler = new MouseItemActionInputHandler(inventoryView,player,globalView,tileMap,this);

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
        this.mob = new Mob(0, 0, 32, 32, tileMap, 2, 2, 15, 3, ActorEnum.POULET, hitboxManager);
        this.mobView = new MobView(mob, tileMap, entitiesPane);
        mob.healthProperty().addListener(new DeathListener(mob, mobView, aliveActors,itemFactory));
        aliveActors.add(mob);
        hitboxManager.createHitbox(mob,HitboxType.VULNERABLE);
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
                0, 0, 40, 54, tileMap, 5, 1, 15, 10, ActorEnum.ZOMBIE, player, 20, 1500, aliveActors, entitiesPane, 1, this.hitboxManager // Use actorsPane instead of globalPane
        );
        this.aggressiveMobView = new MobView(aggressiveMob, tileMap, entitiesPane);
        aggressiveMob.healthProperty().addListener(new DeathListener(aggressiveMob, aggressiveMobView, aliveActors,itemFactory));
        aliveActors.add(aggressiveMob);
    }
    private void initPnj() {
        Pnj homps = new Pnj(100, 0,32, 64, tileMap, 2, 2, 10, 3, ActorEnum.HOMPS, this.hitboxManager);
        this.pnjView = new PnjView(homps, tileMap, entitiesPane);
        homps.healthProperty().addListener(new DeathListener(homps, pnjView, aliveActors,itemFactory));
        aliveActors.add(homps);
        dialogueCD = new Cooldown(0);
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
     * Updates the camera position to center on the player
     */
    private void updateCameraPosition() {
        if (player == null || landTileMap == null || backgroundTileMap == null) {
            return;
        }

        double screenWidth = globalPane.getWidth();
        double screenHeight = globalPane.getHeight();

        double playerCenterX = player.getPosX() + player.getWidth() / 2;
        double playerCenterY = player.getPosY() + player.getHeight() / 2;

        cameraOffsetX = (screenWidth / 2) - playerCenterX;
        cameraOffsetY = (screenHeight / 2) - playerCenterY;

        landTileMap.setTranslateX(cameraOffsetX);
        landTileMap.setTranslateY(cameraOffsetY);
        backgroundTileMap.setTranslateX(cameraOffsetX);
        backgroundTileMap.setTranslateY(cameraOffsetY);

        for (Actor actor : aliveActors) {
            if (actor == null) {
                continue;
            }

            if (actor.equals(player)) {
                if (playerView != null && playerView.getActorSprite() != null) {
                    playerView.getActorSprite().setLayoutX(actor.getPosX() + cameraOffsetX);
                    playerView.getActorSprite().setLayoutY(actor.getPosY() + cameraOffsetY);
                }
            } else {
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
            }
        }
        if (lootView != null) {
            lootView.updateLootPositions(cameraOffsetX, cameraOffsetY);
        }

        if (pnjView != null && pnjView.getActorSprite() != null && pnjView.getPhrase() != null) {
            double pnjX = pnjView.getActorSprite().getLayoutX();
            double pnjY = pnjView.getActorSprite().getLayoutY();
            pnjView.getPhrase().setLayoutX(pnjX + 30);
            pnjView.getPhrase().setLayoutY(pnjY - 30);
        }
    }

    /**
     * Gets the current camera offset X
     * @return the camera offset X
     */
    public double getCameraOffsetX() {
        return cameraOffsetX;
    }

    /**
     * Gets the current camera offset Y
     * @return the camera offset Y
     */
    public double getCameraOffsetY() {
        return cameraOffsetY;
    }

    /**
     * Gets the list of alive actors
     * @return the list of alive actors
     */
    public ArrayList<Actor> getAliveActors() {
        return aliveActors;
    }

    /**
     * Gets the actors pane
     * @return the actors pane
     */
    public AnchorPane getEntitiesPane() {
        return entitiesPane;
    }

    /**
     * Gets the item factory
     * @return the item factory
     */
    public ItemFactory getItemFactory() {
        return itemFactory;
    }

    /**
     * Checks if the player is near any crafting stations (furnace or crafting table)
     * and updates the crafting manager accordingly
     */
    private void checkCraftingStationProximity() {
        if (player == null || tileMap == null || craftingManager == null) {
            return;
        }

        int playerTileX = (int) (player.getPosX() / TileMap.format);
        int playerTileY = (int) (player.getPosY() / TileMap.format);

        boolean foundCraftingTable = false;
        boolean foundFurnace = false;

        for (int x = playerTileX - 3; x <= playerTileX + 3; x++) {
            for (int y = playerTileY - 3; y <= playerTileY + 3; y++) {
                if (x < 0 || y < 0 || x >= tileMap.getWidth() || y >= tileMap.getHeight()) {
                    continue;
                }

                Tile tile = tileMap.getTile(x, y);
                if (tile != null && tile.getTileEnum() != null) {
                    if (tile.getTileEnum() == TilesEnum.CRAFTING_TABLE) {
                        foundCraftingTable = true;
                    }
                    else if (tile.getTileEnum() == TilesEnum.FURNACE) {
                        foundFurnace = true;
                    }
                }
            }
        }

        craftingManager.setNearCraftingTable(foundCraftingTable);
        craftingManager.setNearFurnace(foundFurnace);
    }
}
