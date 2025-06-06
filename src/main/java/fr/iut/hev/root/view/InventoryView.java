package fr.iut.hev.root.view;

import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.InventorySlot;
import fr.iut.hev.root.model.items.Item;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import fr.iut.hev.root.model.CraftingManager;
import fr.iut.hev.root.model.Recipe;
import fr.iut.hev.root.model.enums.Items;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;


import java.util.HashMap;
import java.util.Map;

public class InventoryView {
    private VBox craftingPanel;
    private final Inventory inventory;
    private final GridPane hotbar;
    private final GridPane expandedInventory;
    private boolean inventoryOpened;
    private AnchorPane hudAnchorPane;
    private static int slotIndex = 0;
    private Pane backgroundMousePane;

    public InventoryView(Inventory inventory, GridPane hotbar, GridPane expandedInventory,AnchorPane hudAnchorPane) {
        this.inventory = inventory;
        this.hotbar = hotbar;
        this.expandedInventory = expandedInventory;
        this.inventoryOpened = false;
        this.hudAnchorPane = hudAnchorPane;
        initInventory();
        initCraftingPanel();
    }

    private void initCraftingPanel() {
        craftingPanel = new VBox(10);
        craftingPanel.setLayoutX(600); // Ajuste la position selon ton UI
        craftingPanel.setLayoutY(100);
        craftingPanel.setStyle("-fx-background-color: rgba(30,30,30,0.8); -fx-padding: 10;");
        craftingPanel.setVisible(false); // Masqué tant que l'inventaire est fermé

        Label title = new Label("Crafting Recipes");
        title.setTextFill(Color.WHITE);

        // Conteneur pour les boutons de crafting (sera rempli dynamiquement)
        VBox craftButtonsContainer = new VBox(5);

        // ScrollPane contenant les boutons de craft
        ScrollPane scrollPane = new ScrollPane(craftButtonsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);
        scrollPane.setStyle("-fx-background-color: transparent;");

        // Ajout du titre et du scroll dans le panneau
        craftingPanel.getChildren().addAll(title, scrollPane);

        // Ajout du panneau au HUD principal
        hudAnchorPane.getChildren().add(craftingPanel);
    }


    private void initInventory() {
        initGrid(hotbar, hotbar.getColumnCount(), 1);
        initGrid(expandedInventory, expandedInventory.getColumnCount(), expandedInventory.getRowCount());
        initHold();

        inventory.getSlots().forEach(slot -> {
            slot.itemProperty().addListener((obs, oldVal, newVal) -> updateSlot(slot));
            slot.quantityProperty().addListener((obs, oldVal, newVal) -> updateSlot(slot)); // TODO: utiliser un bind ici
        });
        hotbar.setMouseTransparent(true);
        expandedInventory.setMouseTransparent(true);
        expandedInventory.setVisible(false);

    }

    private void initGrid(GridPane grid, int columns, int rows) {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                Pane cell = createCell();
                grid.add(cell, x, y);
                slotIndex++;
            }
        }
    }

    private Pane createCell() {
        ImageView imageView = new ImageView();
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        imageView.setId(Integer.toString(slotIndex));

        Label label = new Label();
        label.setTextFill(Color.WHITE);

        Pane pane = new Pane(imageView, label);
        pane.setBackground(Background.fill(Color.rgb(0, 0, 0, 0.25)));
        pane.setId(Integer.toString(slotIndex));
        return pane;
    }

    private void updateSlot(InventorySlot slot) {
        Node cell = findCell(slot.getIndex());
        if (cell instanceof Pane pane) {
            for (Node child : pane.getChildren()) {
                if (child instanceof ImageView imageView) {
                    imageView.setImage(getImageFromSlot(slot));
                } else if (child instanceof Label label) {
                    label.setText(getQuantityTextFromSlot(slot));
                }
            }
        }
    }

    private Image getImageFromSlot(InventorySlot slot) {
        if (slot.getItem() == null) return null;
        String name = slot.getItem().getItem().getName();
        String path = "/fr/iut/hev/root/img/items/" + name + ".png";
        return new Image(getClass().getResource(path).toExternalForm());
    }

    private Image getImageFromHold(HashMap<Item,Integer> onHold) {
        if (onHold == null)
            return null;
        String path = "/fr/iut/hev/root/img/items/" + onHold.keySet().iterator().next().getItem().getName() + ".png";
        return new Image(getClass().getResource(path).toExternalForm());
    }

    private String getQuantityTextFromSlot(InventorySlot slot) {
        String txt;
        if (/*slot.getItem().getItem().getMaxQuantity() == 1 ||*/ slot.getItem() == null) {
            txt = "";
        } else {
            txt = Integer.toString(slot.getQuantity());
        }
        return txt;
    }

    private String getQuantityTextFromHold(HashMap<Item,Integer> onHold) {
        String txt;
        if (onHold == null)
            txt = "";
        else
            txt = Integer.toString(onHold.get(onHold.keySet().iterator().next()));
        return txt;
    }

    private Node findCell(int index) {
        if (index < hotbar.getColumnCount()) {
            return hotbar.getChildren().get(index);
        }
        int adjustedIndex = index - hotbar.getColumnCount();
        return expandedInventory.getChildren().get(adjustedIndex);
    }

    public boolean getInventoryOpened() {return this.inventoryOpened;}

    public void setInventoryVisible() {
        inventoryOpened = !inventoryOpened;

        expandedInventory.setVisible(inventoryOpened);
        expandedInventory.setMouseTransparent(!inventoryOpened);
        hotbar.setMouseTransparent(!inventoryOpened);
        craftingPanel.setVisible(inventoryOpened); // Affiche / cache le panneau de craft

        if (inventoryOpened) {
            updateCraftingPanel(); // Recharge dynamiquement les recettes craftables
        }
    }

    private void updateCraftingPanel() {
        craftingPanel.getChildren().clear();

        Label title = new Label("Crafting Recipes");
        title.setTextFill(Color.WHITE);

        VBox craftButtonsContainer = new VBox(5);

        for (Recipe recipe : CraftingManager.getAllRecipes()) {
            if (CraftingManager.canCraft(recipe, inventory)) { // <-- Ici, passe inventory directement
                Button craftButton = new Button("Craft: " + recipe.getResult().getName());
                craftButton.setMaxWidth(Double.MAX_VALUE);
                craftButton.setOnAction(e -> {
                    if (CraftingManager.canCraft(recipe, inventory)) {
                        CraftingManager.craft(recipe, inventory);
                        updateCraftingPanel();
                    }
                });
                craftButtonsContainer.getChildren().add(craftButton);
            }
        }

        if (craftButtonsContainer.getChildren().isEmpty()) {
            Label noCrafts = new Label("Aucun craft possible.");
            noCrafts.setTextFill(Color.GRAY);
            craftButtonsContainer.getChildren().add(noCrafts);
        }

        ScrollPane scrollPane = new ScrollPane(craftButtonsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);
        scrollPane.setStyle("-fx-background-color: transparent;");

        craftingPanel.getChildren().addAll(title, scrollPane);
    }

    public void initHold() {
        Label label = new Label();
        label.setTextFill(Color.WHITE);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(50);
        imageView.setFitWidth(50);

        backgroundMousePane = new Pane(new ImageView(),label);
        backgroundMousePane.setPrefHeight(50);
        backgroundMousePane.setPrefWidth(50);
        hudAnchorPane.getChildren().add(backgroundMousePane);
    }

    public void updateOnHoldPane(HashMap<Item,Integer> onHold) {
        Pane pane = (Pane) hudAnchorPane.getChildren().get(2);
        for (Node child : pane.getChildren()) {
            if (child instanceof Label)
                ((Label) child).setText(getQuantityTextFromHold(onHold));
            else if (child instanceof ImageView)
                ((ImageView) child).setImage(getImageFromHold(onHold));
        }
    }

    public void updateOnHoldPosition(double x,double y) {
        Pane pane = (Pane) hudAnchorPane.getChildren().get(2);
        pane.setTranslateX(x+1);
        pane.setTranslateY(y+1);
    }
}