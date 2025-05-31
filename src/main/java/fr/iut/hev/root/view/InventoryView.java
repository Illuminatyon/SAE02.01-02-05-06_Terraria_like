package fr.iut.hev.root.view;

import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.InventorySlot;
import fr.iut.hev.root.model.Item;
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
    }

    private void initInventory() {
        initGrid(hotbar, hotbar.getColumnCount(), 1);
        initGrid(expandedInventory, expandedInventory.getColumnCount(), expandedInventory.getRowCount());
        initHold();

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
        expandedInventory.setVisible(!inventoryOpened);
        expandedInventory.setMouseTransparent(inventoryOpened);
        hotbar.setMouseTransparent(inventoryOpened);
        inventoryOpened = !inventoryOpened;
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