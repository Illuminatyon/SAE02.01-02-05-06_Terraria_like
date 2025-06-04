package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.items.Item;
import fr.iut.hev.root.view.HotbarView;
import fr.iut.hev.root.view.InventoryView;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.input.ScrollEvent;

public class ScrollInputHandler implements EventHandler<ScrollEvent> {

    private InventoryView inventoryView;
    private HotbarView hotbarView;
    private Inventory inventory;
    private IntegerProperty directionProperty;
    private Item onHandItem;
    private int indexHotbar;

    public ScrollInputHandler(Inventory inventory,HotbarView hotbarView,InventoryView inventoryView) {
        this.inventory = inventory;
        this.hotbarView = hotbarView;
        this.inventoryView = inventoryView;
        this.directionProperty = new SimpleIntegerProperty(0);
        this.indexHotbar = 0;
        updateOnHandItem();
    }

    @Override
    public void handle(ScrollEvent scrollEvent) {
        if (!inventoryView.getInventoryOpened()) {
            setDirection((int)scrollEvent.getDeltaY());
        }
    }

    public void updateHotbar() {
        int increment = 0;
        if (getDirection() > 0)
            increment = -1;
        else if (getDirection() < 0)
            increment = 1;

        hotbarView.resetHighlight(indexHotbar);
        indexHotbar = indexHotbarIncrementation(increment);
        updateOnHandItem();
        hotbarView.setHighlight(indexHotbar);
    }

    public int getDirection() {return this.directionProperty.getValue();}
    public void setDirection(int directionProperty) {this.directionProperty.setValue(directionProperty);}
    public IntegerProperty directionProperty() {return this.directionProperty;}
    private int indexHotbarIncrementation(int increment) {
        int indexValue;
        if (indexHotbar != 9 && indexHotbar != 0)
            indexValue = indexHotbar + increment;
        else if (indexHotbar == 9) {
            if (increment < 0)
                indexValue = indexHotbar + increment;
            else
                indexValue = 0;
        }
        else {
            if (increment > 0)
                indexValue = indexHotbar + increment;
            else
                indexValue = 9;
        }
        return indexValue;
    }

    private void updateOnHandItem() {
        this.onHandItem = inventory.getInventorySlot(indexHotbar).getItem();
    }
}
