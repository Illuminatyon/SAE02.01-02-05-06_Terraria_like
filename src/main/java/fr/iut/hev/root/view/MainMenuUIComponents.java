package fr.iut.hev.root.view;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainMenuUIComponents {
    // Global
    public AnchorPane root;

    // Into global
    public VBox mainMenuContainer, settingsMenuContainer;
    public StackPane worldsMenuContainer;

    // Into main menu
    public Button mainBtnPlay, mainBtnSettings, mainBtnQuit;

    // Into worlds menu
    public VBox worldsMenuContainerSelection, worldsMenuContainerCreate, worldsMenuContainerEdit;

    // Into worlds menu selection
    public VBox worldsContainer;
    public Button worldsBtnBack, worldsBtnNewWorld;

    // Into worlds menu creation
    public TextField worldsCreateTextField;
    public Button worldsCreateBtnBack, worldsCreateBtn;

    // Into worlds menu edition
    public TextField worldsEditTextField;
    public Button worldsEditBtnBack, worldsEditBtn;

    // Into settings menu
    public Button settingsBtnBack, settingsBtnResetDefault;
}
