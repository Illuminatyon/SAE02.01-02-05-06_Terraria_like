package fr.iut.hev.root.model;

import fr.iut.hev.root.model.exception.InsufficientQuantityException;
import fr.iut.hev.root.model.items.Item;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class InventorySlot {
    private ObjectProperty<Item> itemProperty;
    private IntegerProperty quantityProperty;
    private final int index;

    public InventorySlot(int index) {
        this.itemProperty = new SimpleObjectProperty<Item>();
        this.quantityProperty = new SimpleIntegerProperty(0);
        this.index = index;
    }

    public void remove(int quantity) {
        if (quantity > quantityProperty.get()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot remove ");
            sb.append(quantity);
            sb.append(" from ");
            sb.append(this.itemProperty.get().getItemEnum());
            sb.append(" at slot ");
            sb.append(this.index);
            sb.append(": only ");
            sb.append(this.quantityProperty.get());
            sb.append(" available");
            throw new InsufficientQuantityException(sb.toString());
        }

        quantityProperty.set(quantityProperty.get() - quantity);
        if (quantityProperty.get() == 0) {
            itemProperty.set(null);
        }
    }

    public Item getItem() {
        return this.itemProperty.get();
    }

    public void setItem(Item item) {
        this.itemProperty.set(item);
    }

    public ObjectProperty<Item> itemProperty() {
        return this.itemProperty;
    }

    public int getQuantity() {
        return this.quantityProperty.get();
    }

    public void setQuantity(int value) {
        this.quantityProperty.set(value);
    }

    public IntegerProperty quantityProperty() {
        return this.quantityProperty;
    }

    public int getIndex() {
        return this.index;
    }

    public boolean isEmpty() {return quantityProperty.getValue() == 0;}
}
