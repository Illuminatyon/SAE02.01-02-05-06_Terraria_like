package fr.iut.hev.root.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Inventory {
    private ArrayList<InventorySlot> slots;
    private final int size;
    private int slotsOccupied;

    public Inventory() {
        this.slotsOccupied = 0;
        this.size = 50;
        this.slots = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            slots.add(new InventorySlot(i));
        }
    }

    public HashMap<Item, Integer> add(int slotIndex, Item item, int quantity) {
        if (slots.get(slotIndex).getItem() == null) {
            slots.get(slotIndex).setItem(item);
            slots.get(slotIndex).setQuantity(quantity);
            slotsOccupied++;
            return null;
        } else {
            HashMap<Item, Integer> replacedItem = new HashMap<>();
            replacedItem.put(slots.get(slotIndex).getItem(), slots.get(slotIndex).getQuantity());
            slots.get(slotIndex).setItem(item);
            slots.get(slotIndex).setQuantity(quantity);
            return replacedItem;
        }
    }

    public ArrayList<InventorySlot> getSlots() {
        return this.slots;
    }

    public InventorySlot getInventorySlot(int slotIndex) {
        return this.slots.get(slotIndex);
    }

    public int getSize() {
        return this.size;
    }

    public HashMap<Item, Integer> remove(int slotIndex, int quantity) {
        if (slots.get(slotIndex).getItem() == null) {
            return null;
        }
        HashMap<Item, Integer> removedItem = new HashMap<>();
        removedItem.put(slots.get(slotIndex).getItem(), quantity);
        slots.get(slotIndex).remove(quantity);
        if (slots.get(slotIndex).getQuantity() == 0) {
            slotsOccupied--;
        }
        return removedItem;
    }

    public int getSlotsOccupied() {return this.slotsOccupied;}
}
