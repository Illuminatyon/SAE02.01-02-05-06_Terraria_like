package fr.iut.hev.root.view;

import fr.iut.hev.root.model.World;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MainMenuView {
    private MainMenuUIComponents uiComponents;
    private HashMap<World, HBox> worldsHBox;
    private HBox worldsSelectHBoxTemplate;

    public MainMenuView(MainMenuUIComponents uiComponents) {
        worldsHBox = new HashMap<>();

        // Either load worlds here or in the controller
        //loadWorlds();

        this.uiComponents = uiComponents;
        loadWorlds();
        openMainMenu();
    }

    private void loadWorlds() {
        Pane templatesRoot;

        try {
            templatesRoot = FXMLLoader.load(getClass().getResource("/fr/iut/hev/root/view/menu_templates.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Node node : templatesRoot.getChildren()) {
            switch (node.getId()) {
                case "worldsSelectHBoxTemplate" -> worldsSelectHBoxTemplate = (HBox) node;
            }
        }
    }

    public void openMainMenu() {
        this.uiComponents.worldsMenuContainer.setVisible(false);
        this.uiComponents.settingsMenuContainer.setVisible(false);
        this.uiComponents.mainMenuContainer.setVisible(true);
    }

    public void openWorldsMenu() {
        this.uiComponents.mainMenuContainer.setVisible(false);
        this.uiComponents.worldsMenuContainerCreate.setVisible(false);
        this.uiComponents.worldsMenuContainerEdit.setVisible(false);
        // Show all available worlds
        // Load all worlds before arriving here and store them somewhere in the code so it's fast while the game hasn't been closed
        this.uiComponents.worldsMenuContainerSelection.setVisible(true);
        this.uiComponents.worldsMenuContainer.setVisible(true);
    }

    public void openWorldCreator() { // Also editor
        this.uiComponents.worldsMenuContainerSelection.setVisible(false);
        this.uiComponents.worldsCreateTextField.setText("");
        this.uiComponents.worldsMenuContainerCreate.setVisible(true);
    }

    public void openWorldEditor(World worldToEdit) {
        this.uiComponents.worldsEditTextField.setText(worldToEdit.getName());
        this.uiComponents.worldsMenuContainerSelection.setVisible(false);
        this.uiComponents.worldsMenuContainerEdit.setVisible(true);
    }

    public void openSettingsMenu() {
        this.uiComponents.mainMenuContainer.setVisible(false);
        this.uiComponents.settingsMenuContainer.setVisible(true);
    }

    public void openQuitPrompt() {

    }

    private HBox cloneNode(HBox original, int idCount) {
        HBox clone = new HBox();
        ArrayList<Node> children = new ArrayList<>(original.getChildren());

        clone.setAlignment(original.getAlignment());
        clone.setPadding(original.getPadding());
        clone.setSpacing(original.getSpacing());
        clone.setStyle(original.getStyle());

        for (Node child : children) {
            Node childClone = cloneSingleNode(child, idCount);
            clone.getChildren().add(childClone);
        }
        return clone;
    }

    private Node cloneSingleNode(Node original, int idCount) {
        if (original instanceof Button originalButton) {
            Button clone = new Button(originalButton.getText());
            clone.setStyle(originalButton.getStyle());
            clone.getStyleClass().addAll(originalButton.getStyleClass());
            HBox.setHgrow(clone, HBox.getHgrow(originalButton));
            clone.setId(originalButton.getId() + idCount);
            // Copy actions etc. si besoin
            return clone;
        } else if (original instanceof Label originalLabel) {
            Label clone = new Label(originalLabel.getText());
            clone.setStyle(originalLabel.getStyle());
            clone.getStyleClass().addAll(originalLabel.getStyleClass());
            clone.setFont(originalLabel.getFont());
            clone.setTextFill(originalLabel.getTextFill());
            HBox.setHgrow(clone, HBox.getHgrow(originalLabel));
            clone.setMaxWidth(originalLabel.getMaxWidth());
            clone.setId(originalLabel.getId() + idCount);
            return clone;
        } else if (original instanceof ImageView originalImageView) {
            ImageView clone = new ImageView(originalImageView.getImage());
            clone.setFitWidth(originalImageView.getFitWidth());
            clone.setFitHeight(originalImageView.getFitHeight());
            clone.setPreserveRatio(originalImageView.isPreserveRatio());
            HBox.setHgrow(clone, HBox.getHgrow(originalImageView));
            clone.setId(originalImageView.getId() + idCount);
            return clone;
        } else {
            throw new IllegalArgumentException("Unsupported node type: " + original.getClass());
        }
    }

    /**
     * Creates a new HBox for the given world and adds it to the worlds container.
     * The HBox contains buttons and labels for managing the world.
     * The buttons are returned in order to allow setting actions on them later.
     *
     * @param world The world for which to create the HBox
     * @return A set of buttons created for the world management
     * @author Akram BARRA
     */
    public Set<Button> createWorldHBox(World world) {
        Set<Button> buttons = new HashSet<>();
        HBox clone = (HBox) cloneNode(worldsSelectHBoxTemplate, this.uiComponents.worldsContainer.getChildren().size() + 1); // Id count pas necessaire je pense

        for (Node node : clone.getChildren()) {
            if (node instanceof Label) {
                ((Label) node).textProperty().bind(world.nameProperty());
            } else if (node instanceof Button) {
                buttons.add((Button) node);
            }
        }

        worldsHBox.put(world, clone);
        this.uiComponents.worldsContainer.getChildren().add(clone);
        // Show the world creation loading screen
        // Once the world successfully created, show back the worlds menu
        //openWorldsMenu();

        return buttons;
    }

    public void deleteWorldHBox(World world) {
        this.uiComponents.worldsContainer.getChildren().remove(worldsHBox.get(world));
        worldsHBox.remove(world);
    }
}
