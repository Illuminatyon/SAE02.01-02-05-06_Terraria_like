package fr.iut.hev.root.view;

import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.InventorySlot;
import fr.iut.hev.root.model.enums.Items;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class InventoryView {
    private Inventory inventory;
    private GridPane hotbar;
    private GridPane expandedInventory;

    public InventoryView(Inventory inventory, GridPane hotbar, GridPane expandedInventory) {
        this.inventory = inventory;
        this.hotbar = hotbar;
        this.expandedInventory = expandedInventory;
    }

    public void initInventory() {
        // Hotbar
        for (int i = 0; i < hotbar.getColumnCount(); i++) {
            ImageView cell = new ImageView();
            cell.setFitHeight(50);
            cell.setFitWidth(50);
            hotbar.getChildren().add(cell);
        }

        // Expanded inventory
        for (int i = 0; i < expandedInventory.getRowCount(); i++) {
            for (int j = 0; j < expandedInventory.getColumnCount(); j++) {
                expandedInventory.getChildren().add(new ImageView());
            }
        }
    }

    public void listenInventory(int slotNumber) {
        for (int i = 0; i < inventory.getSize(); i++) {
            //this.inventory.slotProperty(slotNumber).addListener(obs -> {
            this.inventory.getInventorySlot(slotNumber).itemProperty().addListener(obs -> {
                /*if (slotNumber < 9) {
                    for (Node cell : hotbar.getChildren()) {
                        if (slotNumber == GridPane.getColumnIndex(cell)) {
                            cell.
                        }
                    }
                } else {

                }*/
                System.out.println("test");
            });
        }
        /*if (slotX == 0) {

        } else {

        }*/
    }

    public Image getItemImage(Items item) {
        //String path = "/fr/iut/hev/root/img/item/".concat(item.getName()).concat(".png");
        String path = "/fr/iut/hev/root/img/tile/grass.png";
        return new Image(getClass().getResource(path).toExternalForm());
    }
}
