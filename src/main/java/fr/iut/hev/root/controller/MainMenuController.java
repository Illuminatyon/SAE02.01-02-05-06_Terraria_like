package fr.iut.hev.root.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    @FXML private AnchorPane root;
    @FXML private VBox mainMenuContainer, worldsMenuContainer, settingsMenuContainer;
    @FXML private Button mainBtnPlay, mainBtnSettings, mainBtnQuit;
    @FXML private Button worldsBtnBack;
    @FXML private Button settingsBtnBack;

    private List<Button> buttons;
    private int selectedIndex = -1;
    private boolean navigationClavier = false;
    private boolean sourisDejaBougee = false;
    private boolean repriseNavigationClavier = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainMenuContainer.setVisible(true);
        worldsMenuContainer.setVisible(false);
        settingsMenuContainer.setVisible(false);

        buttons = List.of(mainBtnPlay, mainBtnSettings, mainBtnQuit);

        buttons.forEach(b -> b.setFocusTraversable(false)); // A quoi ca sert ca ?

        mainBtnPlay.setOnAction(e -> openWorldsMenu());
        mainBtnSettings.setOnAction(e -> openSettingsMenu());
        mainBtnQuit.setOnAction(e -> quitGame());

        worldsBtnBack.setOnAction(e -> openMainMenu());
        settingsBtnBack.setOnAction(e -> openMainMenu());
        //btnQuit.setOnAction(e -> openQuitPrompt());

        // Détection de mouvement souris — reset navigation clavier
        mainMenuContainer.setOnMouseMoved(e -> { // Replace with root maybe
            if (navigationClavier) {
                sourisDejaBougee = true;
                navigationClavier = false;
                clearButtonStyles(); // on efface juste l'effet visuel
                // ✅ on garde selectedIndex tel quel
            }
        });

        // Navigation clavier
        mainMenuContainer.setOnKeyPressed(e -> {
            if (e.getCode().isArrowKey() || e.getCode() == KeyCode.TAB) {
                if (!navigationClavier) {
                    navigationClavier = true;
                    sourisDejaBougee = false;
                    repriseNavigationClavier = true;

                    // ✅ Forcer une sélection si aucune précédente
                    if (selectedIndex == -1) {
                        selectedIndex = 0;
                    }
                }

                if (repriseNavigationClavier) {
                    updateSelection(); // ✅ Affiche immédiatement le bouton
                    repriseNavigationClavier = false;
                } else {
                    if (e.getCode() == KeyCode.UP || (e.getCode() == KeyCode.TAB && e.isShiftDown())) {
                        selectedIndex = (selectedIndex - 1 + buttons.size()) % buttons.size();
                    } else if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.TAB) {
                        selectedIndex = (selectedIndex + 1) % buttons.size();
                    }
                    updateSelection();
                }

                e.consume();
            }

            if (e.getCode() == KeyCode.ENTER && navigationClavier && selectedIndex != -1) {
                switch (selectedIndex) {
                    case 0 -> openWorldsMenu();
                    case 1 -> openSettingsMenu();
                    case 2 -> quitGame();
                }
            }
        });

        // Focus initial sur VBox pour capter les touches
        Platform.runLater(() -> mainMenuContainer.requestFocus());
    }

    private void updateSelection() {
        clearButtonStyles();
        if (selectedIndex >= 0 && selectedIndex < buttons.size()) {
            Button selectedButton = buttons.get(selectedIndex);
            selectedButton.getStyleClass().add("selected");
        }
    }

    private void clearButtonStyles() {
        for (Button b : buttons) {
            b.getStyleClass().remove("selected");
        }
    }

    private void openWorldsMenu() {
        System.out.println("worlds");
        mainMenuContainer.setVisible(false);
        worldsMenuContainer.setVisible(true);
    }

    private void openSettingsMenu() {
        System.out.println("settings");
        mainMenuContainer.setVisible(false);
        settingsMenuContainer.setVisible(true);
    }

    private void quitGame() {
        Platform.exit();
    }

    private void openQuitPrompt() {

    }

    private void openMainMenu() {
        worldsMenuContainer.setVisible(false);
        settingsMenuContainer.setVisible(false);
        mainMenuContainer.setVisible(true);
    }
}
