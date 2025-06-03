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
            //return null;
        } else {
            HashMap<Item, Integer> replacedItem = new HashMap<>();

            if (slots.get(slotIndex).getItem().getItem() != item.getItem()) {
                replacedItem.put(slots.get(slotIndex).getItem(), slots.get(slotIndex).getQuantity());
                slots.get(slotIndex).setItem(item);
                slots.get(slotIndex).setQuantity(quantity);
            }
            else if (slots.get(slotIndex).getQuantity() < item.getItem().getLimitStacking()) {
                if ((slots.get(slotIndex).getQuantity() + quantity) > item.getItem().getLimitStacking()) {
                    replacedItem.put(item,quantity - (item.getItem().getLimitStacking() - slots.get(slotIndex).getQuantity()));
                    slots.get(slotIndex).setQuantity(item.getItem().getLimitStacking());
                }
                else {
                    slots.get(slotIndex).setQuantity(slots.get(slotIndex).getQuantity() + quantity);
                    return null;
                }
            }
            else {
                replacedItem.put(item,quantity);
            }

            return replacedItem;
        }
        return null;
    }

    public void add(Item item, int quantity) {
        // ATTENTION: faire en sorte que les stack de loot au sol soit egalement limité pour éviter de deregler l'inventaire lors d'un ramassage
        if (slotsOccupied == size) return;

        boolean slotAlreadyAvailable = false; // means that the current item already have an available slot to use
        int slotIndex = 0;
        int firstEmptySlotIndex = -1;

        while (!slotAlreadyAvailable && slotIndex < size) {
            if (slots.get(slotIndex).getItem() != null && slots.get(slotIndex).getItem().getItem() == item.getItem() && slots.get(slotIndex).getQuantity() < item.getItem().getLimitStacking()) {
                slotAlreadyAvailable = true;
            } else {
                if (firstEmptySlotIndex == -1 && slots.get(slotIndex).getItem() == null) {
                    firstEmptySlotIndex = slotIndex;
                }
                slotIndex++;
            }
        }

        if (slotAlreadyAvailable) {
            add(slotIndex, item, quantity);
        } else {
            add(firstEmptySlotIndex, item, quantity);
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
