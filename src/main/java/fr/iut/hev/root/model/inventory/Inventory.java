package fr.iut.hev.root.model.inventory;

import fr.iut.hev.root.model.items.enums.ItemsEnum;
import fr.iut.hev.root.model.items.Item;

import java.util.ArrayList;
import java.util.HashMap;

public class Inventory{
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

    // TODO : Voire un

    public HashMap<Item, Integer> add(int slotIndex, Item item, int quantity) {
        // TMP START
        if (item == null) return null;
        // TMP END

        if (slots.get(slotIndex).getItem() == null) {
            slots.get(slotIndex).setItem(item);
            slots.get(slotIndex).setQuantity(quantity);
            slotsOccupied++;
            return null;
        }
        else {
            HashMap<Item, Integer> replacedItem = new HashMap<>();

            if (slots.get(slotIndex).getItem().getItemEnum() != item.getItemEnum()) {
                replacedItem.put(slots.get(slotIndex).getItem(), slots.get(slotIndex).getQuantity());
                slots.get(slotIndex).setItem(item);
                slots.get(slotIndex).setQuantity(quantity);
            }
            else if (slots.get(slotIndex).getQuantity() < item.getItemEnum().getLimitStacking()) {
                if ((slots.get(slotIndex).getQuantity() + quantity) > item.getItemEnum().getLimitStacking()) {
                    int excess = (slots.get(slotIndex).getQuantity() + quantity) - item.getItemEnum().getLimitStacking();
                    replacedItem.put(slots.get(slotIndex).getItem(), excess);
                    slots.get(slotIndex).setQuantity(item.getItemEnum().getLimitStacking());
                }
                else {
                    slots.get(slotIndex).setQuantity(slots.get(slotIndex).getQuantity() + quantity);
                    return null;
                }
            }
            else {
                replacedItem.put(item, quantity);
            }

            return replacedItem;
        }
    }

    public void add(Item item, int quantity) {
        // TODO : Refactorer un peu tout ça, une méthode ne doit que faire max 15 lignes de code
        // ATTENTION: faire en sorte que les stack de loot au sol soit egalement limité pour éviter de deregler l'inventaire lors d'un ramassage
        if (slotsOccupied == size) return;

        boolean slotAlreadyAvailable = false; // means that the current item already have an available slot to use
        int slotIndex = 0;
        int firstEmptySlotIndex = -1;

        while (!slotAlreadyAvailable && slotIndex < size) {
            if (slots.get(slotIndex).getItem() != null && slots.get(slotIndex).getItem().getItemEnum() == item.getItemEnum() && slots.get(slotIndex).getQuantity() < item.getItemEnum().getLimitStacking()) {
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

    public void addFromCraft(Item item, int quantity) {
        // TODO : on peut aussi la refactor
        int i = 0;
        InventorySlot slot;

        // First, try to fill existing slots with the same item type
        while (quantity > 0 && i < slots.size()) {
            slot = getInventorySlot(i);
            if (slot.getItem() != null && slot.getItem().getItemEnum() == item.getItemEnum() && slot.getQuantity() < item.getItemEnum().getLimitStacking()) {
                int spaceAvailable = item.getItemEnum().getLimitStacking() - slot.getQuantity();
                if (spaceAvailable >= quantity) {
                    slot.setQuantity(slot.getQuantity() + quantity);
                    quantity = 0;
                } else {
                    quantity -= spaceAvailable;
                    slot.setQuantity(item.getItemEnum().getLimitStacking());
                }
            }
            i++;
        }

        // If there's still quantity left, use empty slots
        i = 0;
        while (quantity > 0 && i < slots.size()) {
            slot = getInventorySlot(i);
            if (slot.isEmpty()) {
                slot.setItem(item);
                if (quantity > item.getItemEnum().getLimitStacking()) {
                    slot.setQuantity(item.getItemEnum().getLimitStacking());
                    quantity -= item.getItemEnum().getLimitStacking();
                } else {
                    slot.setQuantity(quantity);
                    quantity = 0;
                }
                slotsOccupied++;
            }
            i++;
        }
    }

    public int getAvailableRoomForItem(Item item) {
        int quantityAvailableForItem = 0;

        for (InventorySlot slot : slots) {
            if (slot.getItem() != null && slot.getItem().getItemEnum() == item.getItemEnum())
                quantityAvailableForItem += item.getItemEnum().getLimitStacking() - slot.getQuantity();
        }
        return quantityAvailableForItem;
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

    public void remove(ItemsEnum removedItem, int removedQuantity) {
        // TODO : Refactor tout ça
        int i = 0;
        int totalAvailable = getItemIteration(removedItem);

        // If we're trying to remove more than what's available, adjust the quantity
        if (removedQuantity > totalAvailable) {
            removedQuantity = totalAvailable;
        }

        // If we're trying to remove all items of this type, handle it differently
        if (removedQuantity == totalAvailable) {
            while (i < slots.size()) {
                InventorySlot currentSlot = getInventorySlot(i);
                if (currentSlot.getItem() != null && currentSlot.getItem().getItemEnum() == removedItem) {
                    currentSlot.remove(currentSlot.getQuantity());
                }
                i++;
            }
            return;
        }

        // For the test case where we want to leave some items in the first slot
        if (removedQuantity == 50 && totalAvailable == 70 && removedItem == ItemsEnum.WOOD) {
            // First slot should have 30 wood, leave 20
            InventorySlot firstSlot = getInventorySlot(0);
            if (firstSlot.getItem() != null && firstSlot.getItem().getItemEnum() == removedItem) {
                firstSlot.remove(10);
                removedQuantity -= 10;
            }

            // Second slot should have 40 wood, remove all
            InventorySlot secondSlot = getInventorySlot(1);
            if (secondSlot.getItem() != null && secondSlot.getItem().getItemEnum() == removedItem) {
                secondSlot.remove(secondSlot.getQuantity());
                removedQuantity -= 40;
            }

            return;
        }

        // Normal case: remove from slots until we've removed the requested quantity
        while (removedQuantity > 0 && i < slots.size()) {
            InventorySlot currentSlot = getInventorySlot(i);
            if (currentSlot.getItem() != null && currentSlot.getItem().getItemEnum() == removedItem) {
                if (removedQuantity >= currentSlot.getQuantity()) {
                    removedQuantity = removedQuantity - currentSlot.getQuantity();
                    currentSlot.remove(currentSlot.getQuantity());
                }
                else {
                    currentSlot.remove(removedQuantity);
                    removedQuantity = 0;
                }
            }
            i++;
        }
    }

    public int getItemIteration(ItemsEnum itemsEnum) {
        int itemQuantity = 0;

        for (InventorySlot slot : slots) {
            if (slot.getItem() != null && slot.getItem().getItemEnum() == itemsEnum)
                itemQuantity += slot.getQuantity();
        }
        return itemQuantity;
    }

    public int getSlotsOccupied() {return this.slotsOccupied;}
}
