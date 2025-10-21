package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.inventory.Inventory;
import fr.iut.hev.root.model.items.Item;
import fr.iut.hev.root.view.HotbarView;
import fr.iut.hev.root.view.InventoryView;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.input.ScrollEvent;
/**
 * Gestionnaire des entrées de défilement (molette de souris).
 * Cette classe est responsable de la capture et du traitement des événements de défilement,
 * permettant au joueur de naviguer dans sa barre d'accès rapide (hotbar).
 */
public class ScrollInputHandler implements EventHandler<ScrollEvent> {

    private InventoryView inventoryView;
    private HotbarView hotbarView;
    private Inventory inventory;
    private IntegerProperty directionProperty;
    private ObjectProperty<Item> onHandItemProperty;
    private IntegerProperty quantityProperty;
    private int indexHotbar;

    public ScrollInputHandler(Inventory inventory, HotbarView hotbarView, InventoryView inventoryView) {
        this.inventory = inventory;
        this.hotbarView = hotbarView;
        this.inventoryView = inventoryView;
        this.directionProperty = new SimpleIntegerProperty(0);
        this.indexHotbar = 0;
        this.onHandItemProperty = new SimpleObjectProperty<>(inventory.getInventorySlot(indexHotbar).getItem());
        this.quantityProperty = new SimpleIntegerProperty(inventory.getInventorySlot(indexHotbar).getQuantity());
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
        this.indexHotbar = indexHotbarIncrementation(increment);
        updateOnHandItem();
        updateQuantity();
        hotbarView.setHighlight(indexHotbar);
    }

    public int getDirection() {return this.directionProperty.getValue();}
    public void setDirection(int directionProperty) {this.directionProperty.setValue(directionProperty);}
    public IntegerProperty directionProperty() {return this.directionProperty;}
    private int indexHotbarIncrementation(int increment) {
        int newIndex;
        if (indexHotbar != 9 && indexHotbar != 0)
            newIndex = indexHotbar + increment;
        else if (indexHotbar == 9) {
            if (increment < 0)
                newIndex = indexHotbar + increment;
            else
                newIndex = 0;
        }
        else {
            if (increment > 0)
                newIndex = indexHotbar + increment;
            else
                newIndex = 9;
        }
        return newIndex;
    }

    public void updateOnHandItem() {
        setOnHandItem(inventory.getInventorySlot(indexHotbar).getItem());
    }
    public void updateQuantity() {
        setQuantity(inventory.getInventorySlot(indexHotbar).getQuantity());
    }
    public void setOnHandItem(Item item) {this.onHandItemProperty.setValue(item);}
    public ObjectProperty<Item> onHandItemProperty() {return this.onHandItemProperty;}
    public void setQuantity(int quantity) {this.quantityProperty.setValue(quantity);}
    public IntegerProperty quantityProperty() {return this.quantityProperty;}
}
