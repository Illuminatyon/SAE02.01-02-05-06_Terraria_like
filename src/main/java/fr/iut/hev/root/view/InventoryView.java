package fr.iut.hev.root.view;

import fr.iut.hev.root.model.ArmorInventory;
import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.InventorySlot;
import fr.iut.hev.root.model.items.Item;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


import java.util.HashMap;

public class InventoryView {
    private final Inventory inventory;
    private final ArmorInventory armorInventory;
    private final GridPane hotbar;
    private final GridPane expandedInventory;
    private boolean inventoryOpened;
    private AnchorPane hudAnchorPane;
    private static int slotIndex = 0;
    private Pane backgroundMousePane;
    private CraftView craftView;

    public InventoryView(Inventory inventory, ArmorInventory armorInventory, GridPane hotbar, GridPane expandedInventory, AnchorPane hudAnchorPane, CraftView craftView) {
        this.inventory = inventory;
        this.armorInventory = armorInventory;
        this.hotbar = hotbar;
        this.expandedInventory = expandedInventory;
        this.inventoryOpened = false;
        this.hudAnchorPane = hudAnchorPane;
        this.craftView = craftView;
        initInventory();
    }

    private void initInventory() {
        // Reset slotIndex to ensure consistent indexing
        slotIndex = 0;

        initGrid(hotbar, hotbar.getColumnCount(), 1);
        // Add 4 more slots to the expanded inventory (add a new row with 4 slots)
        initGrid(expandedInventory, expandedInventory.getColumnCount(), expandedInventory.getRowCount());
        // Add 4 more slots in a new row for armor
        addExtraSlots(expandedInventory, 4);
        initHold();

        inventory.getSlots().forEach(slot -> {
            slot.itemProperty().addListener((obs, oldVal, newVal) -> updateSlot(slot));
            slot.quantityProperty().addListener((obs, oldVal, newVal) -> updateSlot(slot)); // TODO: utiliser un bind ici
        });
        hotbar.setMouseTransparent(true);
        expandedInventory.setMouseTransparent(true);
        expandedInventory.setVisible(false);
    }

    private void addExtraSlots(GridPane grid, int numSlots) {
        int rowIndex = grid.getRowCount(); // Get the current number of rows
        // Use the armor inventory slots instead of creating new slots
        for (int i = 0; i < numSlots; i++) {
            Pane cell = createCell();

            // Add visual indicators for armor slot types
            String armorType = "";
            String armorLabel = "";
            switch (i) {
                case 0:
                    armorType = "helmet";
                    armorLabel = "Casque";
                    break;
                case 1:
                    armorType = "chestplate";
                    armorLabel = "Plastron";
                    break;
                case 2:
                    armorType = "leggings";
                    armorLabel = "Jambières";
                    break;
                case 3:
                    armorType = "boots";
                    armorLabel = "Bottes";
                    break;
            }

            // Add a style class to indicate the armor type
            cell.getStyleClass().add("armor-slot");
            cell.getStyleClass().add(armorType + "-slot");

            // Add a label to indicate the armor type
            Label typeLabel = new Label(armorLabel);
            typeLabel.getStyleClass().add("armor-label");
            typeLabel.setMouseTransparent(true);

            // Position the label at the bottom of the cell
            typeLabel.setLayoutX(0);
            typeLabel.setLayoutY(30);
            typeLabel.setPrefWidth(50); // Match the cell width
            typeLabel.setAlignment(javafx.geometry.Pos.CENTER);

            cell.getChildren().add(typeLabel);

            // Set the ID to ARMOR_SLOT_START_INDEX + i to match MouseInventoryInputHandler
            cell.setId(Integer.toString(50 + i));
            for (Node child : cell.getChildren()) {
                if (child instanceof ImageView) {
                    ((ImageView) child).setId(Integer.toString(50 + i));
                }
            }

            grid.add(cell, i, rowIndex); // Add cells to a new row

            // Associate this cell with the corresponding armor slot
            InventorySlot armorSlot = armorInventory.getArmorSlot(i);
            if (armorSlot != null) {
                // Add listeners to update the UI when the armor slot changes
                armorSlot.itemProperty().addListener((obs, oldVal, newVal) -> updateSlot(armorSlot));
                armorSlot.quantityProperty().addListener((obs, oldVal, newVal) -> updateSlot(armorSlot));
            }

            // Don't increment slotIndex here since we're using fixed indices for armor slots
        }
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

        Pane pane = new Pane(imageView, label);
        pane.setId(Integer.toString(slotIndex));
        pane.getStyleClass().add("pane");
        return pane;
    }

    private void updateSlot(InventorySlot slot) {
        // For armor slots, we need to use the ARMOR_SLOT_START_INDEX
        int slotIndex = slot.getIndex();
        boolean isArmorSlot = false;

        // Check if this is an armor slot from ArmorInventory
        if (armorInventory.getArmorSlots().contains(slot)) {
            // Convert to the UI index (ARMOR_SLOT_START_INDEX + armor slot index)
            slotIndex = 50 + armorInventory.getArmorSlots().indexOf(slot);
            isArmorSlot = true;
        }

        Node cell = findCell(slotIndex);
        if (cell instanceof Pane pane) {
            for (Node child : pane.getChildren()) {
                if (child instanceof ImageView imageView) {
                    Image image = getImageFromSlot(slot);
                    imageView.setImage(image);
                } else if (child instanceof Label label) {
                    // Skip updating armor type labels (which have the armor-label style class)
                    if (isArmorSlot && label.getStyleClass().contains("armor-label")) {
                        // Don't change the armor type label
                        continue;
                    }
                    label.setText(getQuantityTextFromSlot(slot));
                }
            }
        }
    }

    private Image getImageFromSlot(InventorySlot slot) {
        if (slot.getItem() == null) return null;
        String name = slot.getItem().getItemEnum().getName();
        String path;

        // Special case for IRON_CHESTPLATE to use chestplate.png
        if (slot.getItem().getItemEnum() == fr.iut.hev.root.model.enums.ItemsEnum.IRON_CHESTPLATE) {
            path = "/fr/iut/hev/root/img/items/chestplate.png";
        } else {
            path = "/fr/iut/hev/root/img/items/" + name + ".png";
        }

        try {
            java.net.URL resourceUrl = getClass().getResource(path);
            if (resourceUrl != null) {
                return new Image(resourceUrl.toExternalForm());
            } else {
                System.err.println("Resource not found: " + path);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + path + " - " + e.getMessage());
            return null;
        }
    }

    private Image getImageFromHold(HashMap<Item,Integer> onHold) {
        if (onHold == null)
            return null;

        Item item = onHold.keySet().iterator().next();
        String path;

        // Special case for IRON_CHESTPLATE to use chestplate.png
        if (item.getItemEnum() == fr.iut.hev.root.model.enums.ItemsEnum.IRON_CHESTPLATE) {
            path = "/fr/iut/hev/root/img/items/chestplate.png";
        } else {
            path = "/fr/iut/hev/root/img/items/" + item.getItemEnum().getName() + ".png";
        }

        try {
            java.net.URL resourceUrl = getClass().getResource(path);
            if (resourceUrl != null) {
                return new Image(resourceUrl.toExternalForm());
            } else {
                System.err.println("Resource not found: " + path);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + path + " - " + e.getMessage());
            return null;
        }
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
        // Check if this is an armor slot (using the same logic as MouseInventoryInputHandler)
        if (index >= 50) {
            // This is an armor slot
            int armorSlotIndex = index - 50;
            // Find the armor slot in the expandedInventory
            // Armor slots are in the last row of the expandedInventory
            int rowIndex = expandedInventory.getRowCount() - 1;
            int columnIndex = armorSlotIndex;

            // Calculate the index in the expandedInventory's children list
            int childIndex = rowIndex * expandedInventory.getColumnCount() + columnIndex;

            // Make sure the index is valid
            if (childIndex < expandedInventory.getChildren().size()) {
                return expandedInventory.getChildren().get(childIndex);
            } else {
                System.err.println("Invalid armor slot index: " + index);
                return null;
            }
        } else if (index < hotbar.getColumnCount()) {
            // This is a hotbar slot
            return hotbar.getChildren().get(index);
        } else {
            // This is a regular inventory slot
            int adjustedIndex = index - hotbar.getColumnCount();
            return expandedInventory.getChildren().get(adjustedIndex);
        }
    }

    public boolean getInventoryOpened() {return this.inventoryOpened;}

    public void setInventoryVisible() {
        inventoryOpened = !inventoryOpened;
        if (inventoryOpened == false)
            craftView.setCraftGUIVisible(inventoryOpened);
        expandedInventory.setVisible(inventoryOpened);
        expandedInventory.setMouseTransparent(!inventoryOpened);
        hotbar.setMouseTransparent(!inventoryOpened);
    }

    public void initHold() {
        Label label = new Label();

        ImageView imageView = new ImageView();
        imageView.setFitWidth(50);
        imageView.setFitWidth(50);

        backgroundMousePane = new Pane(new ImageView(),label);
        backgroundMousePane.setPrefHeight(50);
        backgroundMousePane.setPrefWidth(50);
        hudAnchorPane.getChildren().add(backgroundMousePane);
    }

    public void updateOnHoldPane(HashMap<Item,Integer> onHold) {
        Pane pane = (Pane) hudAnchorPane.getChildren().get(5);
        for (Node child : pane.getChildren()) {
            if (child instanceof Label) {
                ((Label) child).setText(getQuantityTextFromHold(onHold));
            } else if (child instanceof ImageView) {
                Image image = getImageFromHold(onHold);
                ((ImageView) child).setImage(image);
                ((ImageView) child).setFitWidth(50);
                ((ImageView) child).setFitHeight(50);
            }
        }
    }

    public void updateOnHoldPosition(double x,double y) {
        for (Node node : hudAnchorPane.getChildren()) {
            System.out.println(node);
        }
        Pane pane = (Pane) hudAnchorPane.getChildren().get(5);
        pane.setTranslateX(x+1);
        pane.setTranslateY(y+1);
    }
}
