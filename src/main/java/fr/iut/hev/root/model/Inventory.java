package fr.iut.hev.root.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.HashMap;

public class Inventory {
    private ArrayList<InventorySlot> slots;
    //private ArrayList<ObjectProperty<InventorySlot>> slots;
    //private ObjectProperty<ArrayList<InventorySlot>> slotsProperty;
    private final int size;
    private int slotsOccupied;

    public Inventory() {
        this.slotsOccupied = 0;
        this.size = 50;
        this.slots = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            slots.add(new InventorySlot(i));
            //slots.add(new SimpleObjectProperty<>(new InventorySlot(i)));
        }
    }

    public HashMap<Item, Integer> add(int slotNumber, Item item, int quantity) {
        if (slots.get(slotNumber).getItem() == null) {
        //if (slots.get(slotNumber).get().getItem() == null) {
            slots.get(slotNumber).setItem(item);
            //slots.get(slotNumber).get().setItem(item);
            slots.get(slotNumber).setQuantity(quantity);
            //slots.get(slotNumber).get().setQuantity(quantity);
            slotsOccupied++;
            return null;
        } else {
            HashMap<Item, Integer> replacedItem = new HashMap<>();
            replacedItem.put(slots.get(slotNumber).getItem(), slots.get(slotNumber).getQuantity());
            //replacedItem.put(slots.get(slotNumber).get().getItem(), slots.get(slotNumber).get().getQuantity());
            slots.get(slotNumber).setItem(item);
            //slots.get(slotNumber).get().setItem(item);
            slots.get(slotNumber).setQuantity(quantity);
            //slots.get(slotNumber).get().setQuantity(quantity);
            return replacedItem;
        }
    }

    public InventorySlot getInventorySlot(int slot) {
        return this.slots.get(slot);
    }

    /*public HashMap<Item, Integer> remove(int slotNumber) {
            if (slots.get(slotNumber).getItem() != null) {
                HashMap<Item, Integer> removedItem = new HashMap<>();
                removedItem.put(slots.get(slotNumber).getItem(), slots.get(slotNumber).getQuantity());
                slots.get(slotNumber).remove();
                return removedItem;
            }

            return null;
    }*/

    /*public ObjectProperty<InventorySlot> slotProperty(int slotNumber) {
        return this.slots.get(slotNumber);
    }*/

    public int getSize() {
        return this.size;
    }















    // A revoir pour les quantités
    /*public void remove(InventorySlot inventorySlot, int quantity) {
        if (slots.contains(inventorySlot)) {
            inventorySlot.setQuantity(inventorySlot.getQuantity() - quantity);
            if (inventorySlot.getQuantity() <= 0) {
                slots.remove(inventorySlot);
            }
        }
    }*/
}
