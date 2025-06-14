package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.ItemTypes;
import fr.iut.hev.root.model.items.Item;
import fr.iut.hev.root.view.InventoryView;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class MouseItemActionInputHandler implements EventHandler<MouseEvent> {

    private InventoryView inventoryView;
    private Player player;
    private Inventory inventory;
    private ObjectProperty<Item> onHandItemProperty;
    private IntegerProperty quantityProperty;
    private BooleanProperty modifiedQuantityProperty;

    public MouseItemActionInputHandler(InventoryView inventoryView,Player player,Inventory inventory) {
        this.inventoryView = inventoryView;
        this.onHandItemProperty = new SimpleObjectProperty<>(inventory.getInventorySlot(0).getItem());
        this.quantityProperty = new SimpleIntegerProperty(inventory.getInventorySlot(0).getQuantity());
        this.modifiedQuantityProperty = new SimpleBooleanProperty(false);
        this.player = player;
        this.inventory = inventory;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (!inventoryView.getInventoryOpened()) {
            if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    onLeftClick();
                }
                else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                    onRightClick();
                }
            }
        }
    }

    public void onLeftClick() {
        if (getOnHandItem() != null && checkOnHandItemIsConsumable()) {
            setModifiedQuantity(getOnHandItem().isUsed(player,inventory) && !getModifiedQuantity());
        }
    }

    public void onRightClick() {

    }

    public boolean checkOnHandItemIsConsumable() {
        return getOnHandItem().getItem().getItemType().equals(ItemTypes.CONSUMABLE);
    }

    public Item getOnHandItem() {return this.onHandItemProperty.getValue();}
    public void setOnHandItem(Item item) {this.onHandItemProperty.setValue(item);}
    public ObjectProperty<Item> onHandItemProperty() {return this.onHandItemProperty;}

    public int getQuantity() {return this.quantityProperty.getValue();}
    public void setQuantity(int quantity) {this.quantityProperty.setValue(quantity);}
    public IntegerProperty quantityProperty() {return this.quantityProperty;}

    public boolean getModifiedQuantity() {return this.modifiedQuantityProperty.getValue();}
    public void setModifiedQuantity(boolean modifiedQuantityProperty) {this.modifiedQuantityProperty.setValue(modifiedQuantityProperty);}
    public BooleanProperty modifiedQuantityProperty() {return this.modifiedQuantityProperty;}
}
