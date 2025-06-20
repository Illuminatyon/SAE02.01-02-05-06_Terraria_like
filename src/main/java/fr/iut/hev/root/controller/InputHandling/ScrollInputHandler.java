package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.Inventory;
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
    private IntegerProperty indexHotbarProperty;

    public ScrollInputHandler(Inventory inventory,HotbarView hotbarView,InventoryView inventoryView) {
        this.inventory = inventory;
        this.hotbarView = hotbarView;
        this.inventoryView = inventoryView;
        this.directionProperty = new SimpleIntegerProperty(0);
        this.indexHotbarProperty = new SimpleIntegerProperty(0);
        this.onHandItemProperty = new SimpleObjectProperty<>(inventory.getInventorySlot(getIndexHotbar()).getItem());
        this.quantityProperty = new SimpleIntegerProperty(inventory.getInventorySlot(getIndexHotbar()).getQuantity());
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

        hotbarView.resetHighlight(getIndexHotbar());
        setIndexHotbar(indexHotbarIncrementation(increment));
        updateOnHandItem();
        updateQuantity();
        hotbarView.setHighlight(getIndexHotbar());
    }

    public int getDirection() {return this.directionProperty.getValue();}
    public void setDirection(int directionProperty) {this.directionProperty.setValue(directionProperty);}
    public IntegerProperty directionProperty() {return this.directionProperty;}
    private int indexHotbarIncrementation(int increment) {
        int indexValue;
        if (getIndexHotbar() != 9 && getIndexHotbar() != 0)
            indexValue = getIndexHotbar() + increment;
        else if (getIndexHotbar() == 9) {
            if (increment < 0)
                indexValue = getIndexHotbar() + increment;
            else
                indexValue = 0;
        }
        else {
            if (increment > 0)
                indexValue = getIndexHotbar() + increment;
            else
                indexValue = 9;
        }
        return indexValue;
    }

    public void updateOnHandItem() {
        setOnHandItem(inventory.getInventorySlot(getIndexHotbar()).getItem());
    }

    public void updateQuantity() {
        setQuantity(inventory.getInventorySlot(getIndexHotbar()).getQuantity());
    }

    public Item getOnHandItem() {return this.onHandItemProperty.getValue();}
    public void setOnHandItem(Item item) {this.onHandItemProperty.setValue(item);}
    public ObjectProperty<Item> onHandItemProperty() {return this.onHandItemProperty;}

    public int getQuantity() {return this.quantityProperty.getValue();}
    public void setQuantity(int quantity) {this.quantityProperty.setValue(quantity);}
    public IntegerProperty quantityProperty() {return this.quantityProperty;}

    public int getIndexHotbar() {return this.indexHotbarProperty.getValue();}
    public void setIndexHotbar(int indexHotbar) {this.indexHotbarProperty.setValue(indexHotbar);}
    public IntegerProperty IndexHotbarProperty() {return this.indexHotbarProperty;}
}
