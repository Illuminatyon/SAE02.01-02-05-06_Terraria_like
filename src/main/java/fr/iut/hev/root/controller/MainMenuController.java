package fr.iut.hev.root.controller;

import fr.iut.hev.root.model.World;
import fr.iut.hev.root.view.MainMenuUIComponents;
import fr.iut.hev.root.view.MainMenuView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.*;

public class MainMenuController implements Initializable {
    private Set<World> worlds = new HashSet<>();
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

        // Load all things OR load everything needed in each respective class instead of here
        //mainMenuView.loadWorlds();

        mainBtnPlay.setOnAction(e -> mainMenuView.openWorldsMenu());
        mainBtnSettings.setOnAction(e -> mainMenuView.openSettingsMenu());
        mainBtnQuit.setOnAction(e -> quitGame());

        worldsMenuContainer.alignmentProperty().addListener((obs, oldV, newV) -> {

        });

        worldsBtnBack.setOnAction(e -> mainMenuView.openMainMenu());
        worldsBtnNewWorld.setOnAction(e -> mainMenuView.openWorldCreator());
        worldsCreateBtnBack.setOnAction(e -> mainMenuView.openWorldsMenu());
        worldsCreateBtn.setOnAction(e -> createWorld(worldsCreateTextField.getText()));
        worldsEditBtnBack.setOnAction(e -> mainMenuView.openWorldsMenu());
        worldsEditBtn.setOnAction(e -> editWorld(worldsEditTextField.getText()));

        settingsBtnBack.setOnAction(e -> mainMenuView.openMainMenu());
    }

    private boolean worldNameExists(String worldName, boolean ignoreCase) {
        if (ignoreCase) {
            return worlds.stream().anyMatch(w -> w.getName().equalsIgnoreCase(worldName));
        } else {
            return worlds.stream().anyMatch(w -> w.getName().equals(worldName));
        }
    }

    private void quitGame() {
        Platform.exit();
    }

    private void playOnWorld(World world) {
        // TODO: Show the loading screen
        // TODO: Load the world using the JSON save
        System.out.println("Playing on world: " + world.getName());
    }

    private String removeStartingAndTrailingSpaces(String input) {
        return input.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
    }

    private void createWorld(String worldName) {
        // Create a JSON World
        if (worldName == null || worldName.isBlank()) {
            worldName = generateUniqueWorldName("My world");
        } else {
            worldName = removeStartingAndTrailingSpaces(worldName);
            if (worldNameExists(worldName, true)) {
                worldName = generateUniqueWorldName(worldName);
            }
        }

        World newWorld = new World(worldName);
        worlds.add(newWorld);
        Set<Button> worldManagementButtons = mainMenuView.createWorldHBox(newWorld);

        for (Button btn : worldManagementButtons) {
            if (btn.getId().startsWith("worldBtnPlay")) {
                btn.setOnAction(e -> playOnWorld(newWorld));
            } else if (btn.getId().startsWith("worldBtnEdit")) {
                btn.setOnAction(e -> {
                    currentEditingWorld = newWorld;
                    mainMenuView.openWorldEditor(newWorld);
                });
            } else if (btn.getId().startsWith("worldBtnDelete")) {
                btn.setOnAction(e -> deleteWorld(newWorld));
            }
        }

        mainMenuView.openWorldsMenu();
    }

    private void editWorld(String newWorldName) {
        newWorldName = removeStartingAndTrailingSpaces(newWorldName);
        if (newWorldName.isBlank() || currentEditingWorld.getName().equals(newWorldName)) { // Keep the name before editing
            newWorldName = currentEditingWorld.getName();
        } else if (currentEditingWorld.getName().equalsIgnoreCase(newWorldName)) { // Use the name typed with changing case only
            newWorldName = newWorldName;
        } else if (worldNameExists(newWorldName, true)) { // If the name already exists, generate a new one
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

        while (worldNameExists(proposedName, true)) {
            proposedName = baseName + " " + i;
            i++;
        }

        return proposedName;
    }
}
