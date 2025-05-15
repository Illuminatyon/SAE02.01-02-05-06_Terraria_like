package fr.iut.hev.root.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Inventory {
    private ArrayList<InventorySlot> slots;
    private static final int size = 50;
    private int slotsOccupied;

    public Inventory() {
        this.slotsOccupied = 0;
        this.slots = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            slots.add(new InventorySlot(i));
        }
    }

    public HashMap<Item, Integer> add(int slotNumber, Item item, int quantity) {
        if (slots.get(slotNumber).getItem() == null) {
            slots.get(slotNumber).setItem(item);
            slots.get(slotNumber).setQuantity(quantity);
            slotsOccupied++;
            return null;
        } else {
            HashMap<Item, Integer> replacedItem = new HashMap<>();
            replacedItem.put(slots.get(slotNumber).getItem(), slots.get(slotNumber).getQuantity());
            slots.get(slotNumber).setItem(item);
            slots.get(slotNumber).setQuantity(quantity);
            return replacedItem;
        }
    }

    public HashMap<Item, Integer> remove(int slotNumber) {
            if (slots.get(slotNumber).getItem() != null) {
                HashMap<Item, Integer> removedItem = new HashMap<>();
                removedItem.put(slots.get(slotNumber).getItem(), slots.get(slotNumber).getQuantity());
                slots.get(slotNumber).remove();
                return removedItem;
            }

            return null;
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
