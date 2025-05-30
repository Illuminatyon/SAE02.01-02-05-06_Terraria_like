package fr.iut.hev.root.view;

import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.InventorySlot;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class InventoryView {
    private final Inventory inventory;
    private final GridPane hotbar;
    private final GridPane expandedInventory;
    private boolean inventoryOpened;
    private static int slotIndex = 0;

    public InventoryView(Inventory inventory, GridPane hotbar, GridPane expandedInventory) {
        this.inventory = inventory;
        this.hotbar = hotbar;
        this.expandedInventory = expandedInventory;
        this.inventoryOpened = false;
        initInventory();
    }

    private void initInventory() {
        initGrid(hotbar, hotbar.getColumnCount(), 1);
        initGrid(expandedInventory, expandedInventory.getColumnCount(), expandedInventory.getRowCount());

        inventory.getSlots().forEach(slot -> {
            slot.itemProperty().addListener((obs, oldVal, newVal) -> updateSlot(slot));
            slot.quantityProperty().addListener((obs, oldVal, newVal) -> updateSlot(slot));
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
                    imageView.setImage(getImage(slot));
                } else if (child instanceof Label label) {
                    label.setText(getQuantityText(slot));
                }
            }
        }
    }

    private Image getImage(InventorySlot slot) {
        if (slot.getItem() == null) return null;
        String name = slot.getItem().getItem().getName();
        String path = "/fr/iut/hev/root/img/items/" + name + ".png";
        return new Image(getClass().getResource(path).toExternalForm());
    }

    private String getQuantityText(InventorySlot slot) {
        String txt;
        if (/*slot.getItem().getItem().getMaxQuantity() == 1 ||*/ slot.getItem() == null) {
            txt = "";
        } else {
            txt = Integer.toString(slot.getQuantity());
        }
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
        expandedInventory.setVisible(!inventoryOpened);
        expandedInventory.setMouseTransparent(inventoryOpened);
        hotbar.setMouseTransparent(inventoryOpened);
        inventoryOpened = !inventoryOpened;
    }
}