package fr.iut.hev.root.controller;

import fr.iut.hev.root.controller.InputHandling.*;
import fr.iut.hev.root.controller.Listeners.DeathListener;
import fr.iut.hev.root.model.*;
import fr.iut.hev.root.model.entities.*;
import fr.iut.hev.root.model.enums.*;
import fr.iut.hev.root.model.exception.MapLoadingException;
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
    private World world;
    private Camera camera;
    private Timeline gameLoop;
    // private Player player; // dans world
    // private TileMap tileMap; // dans world
    //private MouseInventoryInputHandler mouseInventoryHandler;
    //private ScrollInputHandler scrollHotbarHandler;
    //private KeyInputHandler keyboardHandler;
    //private MouseItemActionInputHandler mouseItemActionHandler;
    private InputHandler inputHandler;
    // private ArrayList<Actor> aliveActors; // dans world
    //private Inventory inventory; // dans player
    private CooldownManager cooldownManager; // controller
    //private CraftingManager craftingManager; // peut etre dans le joueur
    //private HitboxManager hitboxManager; // world ou controller
    //public static Mob mob ;
    private static Set<Mob> mobs; // N'a rien a faire dans le controller
    //private ItemFactory itemFactory;

    private GlobalView globalView; // TODO: Rename to MapView instead for more clarity
    private HeartsView heartsView;
    private PlayerView playerView; // A voir si on modifie
    private MouseCursorCircleView playerLightCircle; // hmmmm
    private InventoryView inventoryView;
    private HotbarView hotbarView;
    private ArrayList<MobView> mobView; // A voir si on modifie
    private PnjView pnjView; // A voir si on modifie
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
        /*try {
            //initWorld();
        } catch (IOException e) {
            handleInitializeError(e);
        }*/
        world = World.getInstance();
        //world.initWorld();
        initItemEnums(); // Laisser la ou bouger dans world ?
        initGameLoop(); // Laisser ici
        initViews(); // Obligatoire
        initPlayerViewsAndMobs(); // A changer absolument
    }

    private void handleInitializeError(IOException e){
        throw new MapLoadingException("Erreur lors de l'initialisation du jeu", e);
    }

    private void initGameLoop(){
        gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        cooldownManager = new CooldownManager();

        KeyFrame fk = new KeyFrame(
                Duration.seconds(0.017),
                ev -> updateGameLoop()
        );
    }

    // TODO : potentiellement le move dans la vue du coup
    private void initViews(){
        globalView = new GlobalView(world.getTileMap(), landTileMap, backgroundTileMap);
        lootView = new LootView(entitiesPane);
    }

    private void initPlayerViewsAndMobs() { // Les mobs crée sont des tests
        initPlayer();
        System.out.println("crashed ?");
        createAggressiveMob(ActorEnum.ZOMBIE);
        createMob(ActorEnum.POULET);
        createNPC(ActorEnum.HOMPS);
        System.out.println("recrashed .");
    }


    private void updateGameLoop() {
        world.getPlayer().update();
        updateAliveMobs(); // TODO : C'est le world qui est censé gérer ca
        updateLoots();
        handleMouseInput();
        updatePlayerLight();
        camera.update();
        cooldownManager.allCooldownsTick();
    }


    // TODO : Mettre dans World
    private void updateAliveMobs() {
        for (int i = world.getAliveMobs().size() - 1; i >= 0; i--) { // TODO : Chercher pourquoi on a choisi de faire le parcours à l'envers
            Actor currentActor = world.getAliveMobs().get(i);
            if (currentActor != null) {
                currentActor.updatePosition();
            } else {
                world.getAliveMobs().remove(i);
            }
        }
    }

    private void updateLoots() {
        for (Loot loot : Loot.lootOnMapProperty) {
            loot.updatePosition();
        }
    }

    private void handleMouseInput() {
        if (inputHandler.getMouseItemActionInputHandler().getMouseClickIsPressed()) {
            inputHandler.getMouseItemActionInputHandler().onClickPressedLoop();
        }
        if (inputHandler.getMouseItemActionInputHandler().getMouseClickIsReleased()) {
            inputHandler.getMouseItemActionInputHandler().onClickReleasedLoop();
        }
    }

    private void updatePlayerLight() {
        if (playerLightCircle != null) {
            double playerCenterX = playerView.getActorSprite().getLayoutX()
                    + playerView.getActorSprite().getTranslateX()
-
                    + playerView.getActorSprite().getFitWidth() / 2;

            double playerCenterY = playerView.getActorSprite().getLayoutY()
                    + playerView.getActorSprite().getTranslateY()
                    + playerView.getActorSprite().getFitHeight() / 2;
            playerLightCircle.updateCenter(playerCenterX, playerCenterY);
        }
    }

    /*private void initWorld() throws IOException {
        hitboxManager = new HitboxManager();
        itemFactory = ItemFactory.getInstance();
        itemFactory.setHitboxManager(hitboxManager);
        tileMap = new TileMap(3840, 1440, itemFactory);
        player = new Player(0, 0, 32, 64, tileMap, 2, 10, 3, ActorEnum.PLAYER, hitboxManager);
        aliveActors = new ArrayList<>();
        world = new World("Default World", tileMap, player, aliveActors, hitboxManager, itemFactory);
    }*/

    private void initPlayer() {
        //DONE: déplacer ça dans une initialisation de player dans Word
        //ItemFactory itemFactory = world.getItemFactory(); // DONE: A deplacer
        //craftingManager = new CraftingManager(inventory,itemFactory);//DONE: déplacer le crafting manager dans player sachant qu'il faut faire le refactor de la playerview avant étant donné qu'il est impliqué dans la playerview
        //player = world.getPlayer(); //DONE: bizarre j'ai l'impression qu'on l'a déjà initialisé dans initWorld()
        //inventory = player.getInventory();
        //hitboxManager.createHitbox(player, HitboxType.VULNERABLE);
        //DONE: ptet aller chercher le joueur par la variable player

        camera = new Camera(world.getPlayer(), landTileMap, backgroundTileMap, globalPane, lootView, 0.1);
        playerView.camOffsetXProperty().bind(camera.currentCamXProperty());
        playerView.camOffsetYProperty().bind(camera.currentCamYProperty());

        //DONE: bouger ça dans une méthode ou quelque chose consacré à l'initialisation de la vue
        playerView = new PlayerView(world.getTileMap(), entitiesPane, heartsHbox, craftListView, craftButton, recipeDisplay, hotbarInventory, expandedInventory, hudAnchorPane);

        //heartsView = new HeartsView(player.healthProperty(), heartsHbox); //DONE: regrouper l'initialisation des vues dans une seule méthode
        //craftView = new CraftView(craftListView,craftingManager.getRecipesAvailable(),craftButton,recipeDisplay);
        //inventoryView = new InventoryView(inventory, hotbarInventory, expandedInventory,hudAnchorPane,craftView);
        //hotbarView = new HotbarView(hotbarInventory);

        //TODO: de la vue aussi
        craftingManager.selectedRecipeProperty().bind(craftView.selectedRecipeProperty());
        craftButton.setOnAction(actionEvent -> {
            craftingManager.crafts();
        });
        //TODO: de la vue aussi mais qui a besoin que les eventshandler soient initialisé | à foutre dans l'inventoryView
        inputHandler.getMouseInventoryInputHandler().onHoldProperty().addListener((observableValue, o, t1) ->
                inventoryView.updateOnHoldPane(inputHandler.getMouseInventoryInputHandler().getOnHold()));
        inputHandler.getMouseInventoryInputHandler().xProperty().addListener((observableValue, number, t1) ->
                inventoryView.updateOnHoldPosition(inputHandler.getMouseInventoryInputHandler().getX(), inputHandler.getMouseInventoryInputHandler().getY()));
        inputHandler.getMouseInventoryInputHandler().yProperty().addListener((observableValue, number, t1) ->
                inventoryView.updateOnHoldPosition(inputHandler.getMouseInventoryInputHandler().getX(), inputHandler.getMouseInventoryInputHandler().getY()));

        // TODO : Peut être déplacer dans le joueur directement
        /*inventory.add(0,itemFactory.createItem(ItemsEnum.RAW_CHICKEN),100); //TODO: injection par défaut à terme potentiellement retirer si le jeu devient complet
        inventory.add(1,itemFactory.createItem(ItemsEnum.WOOD),100);
        inventory.add(3,itemFactory.createItem(ItemsEnum.DIRT),100);
        inventory.add(4,itemFactory.createItem(ItemsEnum.FURNACE),100);
        inventory.add(5, itemFactory.createItem(ItemsEnum.KATANA), 1);
        inventory.add(6,itemFactory.createItem(ItemsEnum.DAGGER), 1);
        inventory.add(7,itemFactory.createItem(ItemsEnum.BOW), 1);
        inventory.add(8,itemFactory.createItem(ItemsEnum.ARROW), 64);
        inventory.add(9,itemFactory.createItem(ItemsEnum.WOODEN_PICKAXE),1);
        inventory.add(10,itemFactory.createItem(ItemsEnum.WOODEN_SHOVEL),1);*/

        //TODO: on le bouge pas tant qu'il est pas fix
        //player.healthProperty().addListener(((obs, old, t1) -> hudView.updateHealth(t1)));
        player.healthProperty().addListener(new DeathListener(player, playerView, world.getAliveMobs(), itemFactory)); //TODO: il faut une réorganisation claire de tous les bind, listener tout en tenant compte de l'ordre d'initialisation
        // Utiliser un bind pour le deathlistener

        // DONE : On les gardes ici, mais on va essayer de décomposer la création des Handlers avec des méthodes voir une classe à part entière
        inputHandler = new InputHandler(inventoryView,craftView,camera,globalView,hotbarView);
        inputHandler.initInputHandler(landTileMap,hudAnchorPane);
        //keyboardHandler = new KeyInputHandler(inventoryView,craftView); //DONE: ptet réorganiser aussi les input handler
        //mouseInventoryHandler = new MouseInventoryInputHandler(inventory,inventoryView);
        //scrollHotbarHandler = new ScrollInputHandler(inventory,hotbarView,inventoryView);
        //mouseItemActionHandler = new MouseItemActionInputHandler(world, camera, inventoryView, globalView);

        double playerCenterX = 0;
        double playerCenterY = 0; // TODO : A revoir parce que je ne sais pas si y'a encore des problèmes avec la reach
        // TODO : mais normalement tout était good je pense
        playerLightCircle = new MouseCursorCircleView(globalPane, playerCenterX, playerCenterY, player.getReach()*32, 10);
        playerLightCircle.setCursorVisible(false);

        //DONE: il faut essayer de fix cet histoire d'item in hand mais ptet à bouger dans une méthode
        //player.itemInHandProperty().bindBidirectional(scrollHotbarHandler.onHandItemProperty());
        //player.quantityOfItemInHandProperty().bindBidirectional(scrollHotbarHandler.quantityProperty());
        //player.indexItemInHandProperty().bind(scrollHotbarHandler.IndexHotbarProperty());
        //player.itemInHandProperty().addListener((observableValue, item, t1) -> mouseItemActionHandler.updateCooldown());

        //scrollHotbarHandler.directionProperty().addListener((observableValue, number, t1) -> {
        //    if (scrollHotbarHandler.getDirection() != 0)
        //        scrollHotbarHandler.updateHotbar();
        //});

        //DONE: potentiellement y mettre dans la classe des eventhandler
//        Platform.runLater(() -> { //TODO: même chose dans la réorganisation des input handler
//            landTileMap.getScene().addEventHandler(KeyEvent.ANY,keyboardHandler);
//            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED,mouseItemActionHandler);
//            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_RELEASED,mouseItemActionHandler);
//            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED,mouseItemActionHandler);
//            hudAnchorPane.addEventHandler(MouseEvent.MOUSE_PRESSED,mouseInventoryHandler);
//            hudAnchorPane.addEventHandler(MouseEvent.MOUSE_MOVED,mouseInventoryHandler);
//            landTileMap.getScene().addEventHandler(ScrollEvent.SCROLL,scrollHotbarHandler);
//        });
    }



    private void createNPCView(Mob mob) {

        this.mobView.add(new MobView(mob,world.getTileMap(), entitiesPane));
        mobView.getLast().camOffsetXProperty().bind(camera.currentCamXProperty());
        mobView.getLast().camOffsetYProperty().bind(camera.currentCamYProperty());
        mob.healthProperty().addListener(new DeathListener(mob, mobView.getLast(), world.getAliveMobs(), world.getItemFactory()));
    }
/*
    private void createMob(ActorEnum mobActorEnum) { //TODO: essayer ptet de regrouper tous les créateur de mob/pnj en une méthode pour éviter la duplication
        Mob mob = new Mob(0, 0, 32, 32, tileMap, 2, 2, 15, 3, mobActorEnum, hitboxManager);
        mobView = new MobView(mob, tileMap, );
        mobView.camOffsetXProperty().bind(camera.currentCamXProperty());
        mobView.camOffsetYProperty().bind(camera.currentCamYProperty());
        mob.healthProperty().addListener(new DeathListener(mob, mobView, aliveActors, world.getItemFactory()));
        hitboxManager.createHitbox(mob, HitboxType.VULNERABLE);
        world.getAliveMobs().add(mob); // TODO : faire en sorte de faire déjà tout ça avec des design pattern templates
    }

    // Methode en com dans world
    private void createAggressiveMob(ActorEnum aggressiveMobActorEnum) {
        AggressiveMob aggressiveMob = new AggressiveMob(
                0, 0, 32, 54, tileMap, 5, 1, 15, 10, aggressiveMobActorEnum, player, 20, 1500, aliveActors, entitiesPane, 1, this.hitboxManager // Use actorsPane instead of globalPane
        );
        this.aggressiveMobView = new MobView(aggressiveMob, tileMap, entitiesPane);
        aggressiveMobView.camOffsetXProperty().bind(camera.currentCamXProperty());
        aggressiveMobView.camOffsetYProperty().bind(camera.currentCamYProperty());
        aggressiveMob.healthProperty().addListener(new DeathListener(aggressiveMob, aggressiveMobView, aliveActors,world.getItemFactory()));
        world.getAliveMobs().add(aggressiveMob); // TODO : faire en sorte de faire déjà tout ça avec des design pattern templates
    }
*/
    // Methode en com dans world
    private void createNPC(ActorEnum npcActorEnum) {// TODO : il passera dans le mobviewconstruct/world quand on aura reparé les dialogues
        Pnj npc = new Pnj(0, 0,32, 64, tileMap, 2, 2, 10, 3, npcActorEnum, this.hitboxManager);
        this.pnjView = new PnjView(npc, tileMap, entitiesPane);
        pnjView.camOffsetXProperty().bind(camera.currentCamXProperty());
        pnjView.camOffsetYProperty().bind(camera.currentCamYProperty());
        npc.healthProperty().addListener(new DeathListener(npc, pnjView, aliveActors,world.getItemFactory()));
        dialogueCD = new Cooldown(0);
        aliveActors.add(npc); // TODO : même bordel ici
    }

    /**
     * Checks if the player is near a PNJ and triggers dialogue if needed
     */
    private void checkPnjDialogue() { //TODO: fix les dialogues avec Old Marc (problème de collision et de manière de trigger le dialogue si je dis pas de conneries)
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
