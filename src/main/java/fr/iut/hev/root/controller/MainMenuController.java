package fr.iut.hev.root.controller;

import fr.iut.hev.root.model.*;
import fr.iut.hev.root.model.entities.Actor;
import fr.iut.hev.root.model.entities.Mob;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import fr.iut.hev.root.model.items.ItemFactory;
import fr.iut.hev.root.view.MainMenuUIComponents;
import fr.iut.hev.root.view.MainMenuView;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainMenuController implements Initializable {
    //private SetProperty<World> worldsSetProperty = new SimpleSetProperty<>(FXCollections.observableSet(new HashSet<>()));
    //private Set<World> worlds;
    private List<World> worlds;
    private MainMenuView mainMenuView;
    private World currentEditingWorld;

    // Global
    @FXML private AnchorPane root;

    // Into global
    @FXML private VBox mainMenuContainer, settingsMenuContainer;
    @FXML private StackPane worldsMenuContainer;

    // Into main menu
    @FXML private Button mainBtnPlay, mainBtnSettings, mainBtnQuit;

    // Into worlds menu
    @FXML private VBox worldsMenuContainerSelection, worldsMenuContainerCreate, worldsMenuContainerEdit;

    // Into worlds menu selection
    @FXML private VBox worldsContainer;
    @FXML private Button worldsBtnBack, worldsBtnNewWorld;

    // Into worlds menu creation
    @FXML private TextField worldsCreateTextField;
    @FXML private Button worldsCreateBtnBack, worldsCreateBtn;

    // Into worlds menu edition
    @FXML private TextField worldsEditTextField;
    @FXML private Button worldsEditBtnBack, worldsEditBtn;

    // Into settings menu
    @FXML private Button settingsBtnBack, settingsBtnResetDefault;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MainMenuUIComponents uiComponents = new MainMenuUIComponents();
        uiComponents.root = root;
        uiComponents.mainMenuContainer = mainMenuContainer;
        uiComponents.settingsMenuContainer = settingsMenuContainer;
        uiComponents.worldsMenuContainer = worldsMenuContainer;
        uiComponents.mainBtnPlay = mainBtnPlay;
        uiComponents.mainBtnSettings = mainBtnSettings;
        uiComponents.mainBtnQuit = mainBtnQuit;
        uiComponents.worldsMenuContainerSelection = worldsMenuContainerSelection;
        uiComponents.worldsMenuContainerCreate = worldsMenuContainerCreate;
        uiComponents.worldsMenuContainerEdit = worldsMenuContainerEdit;
        uiComponents.worldsContainer = worldsContainer;
        uiComponents.worldsBtnBack = worldsBtnBack;
        uiComponents.worldsBtnNewWorld = worldsBtnNewWorld;
        uiComponents.worldsCreateTextField = worldsCreateTextField;
        uiComponents.worldsCreateBtnBack = worldsCreateBtnBack;
        uiComponents.worldsCreateBtn = worldsCreateBtn;
        uiComponents.worldsEditTextField = worldsEditTextField;
        uiComponents.worldsEditBtnBack = worldsEditBtnBack;
        uiComponents.worldsEditBtn = worldsEditBtn;
        uiComponents.settingsBtnBack = settingsBtnBack;
        uiComponents.settingsBtnResetDefault = settingsBtnResetDefault;

        mainMenuView = new MainMenuView(uiComponents);

        // TODO: Load all things OR load everything needed in each respective class instead of here
        worlds = new ArrayList<>();

        mainBtnPlay.setOnAction(e -> mainMenuView.openWorldsMenu());
        mainBtnSettings.setOnAction(e -> mainMenuView.openSettingsMenu());
        mainBtnQuit.setOnAction(e -> quitGame());

        worldsMenuContainer.alignmentProperty().addListener((obs, oldV, newV) -> {

        });

        worldsBtnBack.setOnAction(e -> mainMenuView.openMainMenu());
        worldsBtnNewWorld.setOnAction(e -> mainMenuView.openWorldCreator());
        worldsCreateBtnBack.setOnAction(e -> mainMenuView.openWorldsMenu());
        worldsCreateBtn.setOnAction(e -> {
            try {
                createWorld(worldsCreateTextField.getText());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        worldsEditBtnBack.setOnAction(e -> mainMenuView.openWorldsMenu());
        worldsEditBtn.setOnAction(e -> editWorld(worldsEditTextField.getText()));

        settingsBtnBack.setOnAction(e -> mainMenuView.openMainMenu());
    }

    private boolean worldNameExists(String worldName) {
        return worlds.stream().anyMatch(w -> w.getName().equalsIgnoreCase(worldName));
    }

    private void quitGame() {
        Platform.exit();
    }

    private void playOnWorld(World world) {
        // TODO: Show the loading screen
        // TODO: Load the world using the JSON save
        world.setLastPlayed(System.currentTimeMillis());
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fr/iut/hev/root/view/globalView.fxml"));
            Parent worldRoot = fxmlLoader.load();
            GlobalController globalController = fxmlLoader.getController();
            globalController.setWorld(world);
            Stage stage = (Stage) root.getScene().getWindow();
            Scene scene = new Scene(worldRoot, stage.getWidth(), stage.getHeight());
            double x = stage.getX();
            double y = stage.getY();
            stage.setScene(scene);
            stage.setX(x);
            stage.setY(y);
            stage.setTitle("Playing on " + world.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String removeStartingAndTrailingSpaces(String input) {
        return input.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
    }

    private void createWorldView(World world) {
        worlds.add(world);
        Set<Button> worldManagementButtons = mainMenuView.createWorldHBox(world);

        for (Button btn : worldManagementButtons) {
            if (btn.getId().startsWith("worldBtnPlay")) {
                btn.setOnAction(e -> playOnWorld(world));
            } else if (btn.getId().startsWith("worldBtnEdit")) {
                btn.setOnAction(e -> {
                    currentEditingWorld = world;
                    mainMenuView.openWorldEditor(world);
                });
            } else if (btn.getId().startsWith("worldBtnDelete")) {
                btn.setOnAction(e -> deleteWorld(world));
            }
        }
    }

    private void createWorld(String worldName) throws IOException {
        if (worldName == null || worldName.isBlank()) {
            worldName = generateUniqueWorldName("My world");
        } else {
            worldName = removeStartingAndTrailingSpaces(worldName);
            if (worldNameExists(worldName)) {
                worldName = generateUniqueWorldName(worldName);
            }
        }

        ItemFactory itemFactory = new ItemFactory();
        HitboxManager hitboxManager = new HitboxManager();
        TileMap tileMap = new TileMap(1920, 1080, itemFactory);
        Player player = new Player(0, -25, 32, 64, tileMap, 2, 10,3, ActorEnum.PLAYER, hitboxManager);
        //player.initAfterDeserialization(tileMap, player.getPosX(), player.getPosY());
        ArrayList<Actor> aliveActors = new ArrayList<>();
        World newWorld = new World(worldName, tileMap, player, aliveActors, hitboxManager, itemFactory);

        createWorldView(newWorld);
        mainMenuView.openWorldsMenu();
    }

    private void editWorld(String newWorldName) {
        newWorldName = removeStartingAndTrailingSpaces(newWorldName);
        if (newWorldName.isBlank() || currentEditingWorld.getName().equals(newWorldName)) { // Keep the name before editing
            newWorldName = currentEditingWorld.getName();
        } else if (currentEditingWorld.getName().equalsIgnoreCase(newWorldName)) { // Use the name typed with changing case only
            newWorldName = newWorldName;
        } else if (worldNameExists(newWorldName)) { // If the name already exists, generate a new one
            newWorldName = generateUniqueWorldName(newWorldName);
        }

        currentEditingWorld.setName(newWorldName);
        mainMenuView.openWorldsMenu();
        System.out.println(currentEditingWorld.getName());
        currentEditingWorld = null;
    }

    private void deleteWorld(World world) {
        worlds.remove(world);
        mainMenuView.deleteWorldHBox(world);
    }

    private String generateUniqueWorldName(String baseName) {
        int i = 1;
        String proposedName = baseName;

        while (worldNameExists(proposedName)) {
            proposedName = baseName + " " + i;
            i++;
        }

        return proposedName;
    }
}
