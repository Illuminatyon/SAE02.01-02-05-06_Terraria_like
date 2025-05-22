package fr.iut.hev.root.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class InventorySlot {
    //private Item item;
    private ObjectProperty<Item> itemProperty;
    private int quantity;
    private final int position; // peut etre inutile

    public InventorySlot(int position) {
        //item = null;
        itemProperty = new SimpleObjectProperty<Item>();
        quantity = 0;
        this.position = position;
    }

    public void remove() {
        //item = null;
        itemProperty.set(null);
        quantity = 0;
    }

    public Item getItem() {
        //return this.item;
        return this.itemProperty.get();
    }

    public void setItem(Item item) {
        this.itemProperty.set(item);
    }

    public ObjectProperty<Item> itemProperty() {
        return this.itemProperty;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }
}
