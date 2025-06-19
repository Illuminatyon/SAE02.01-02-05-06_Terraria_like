package fr.iut.hev.root.model;

import fr.iut.hev.root.model.enums.ArmorTypesEnum;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.items.ArmorPiece;
import fr.iut.hev.root.model.items.Item;

import java.util.ArrayList;
import java.util.HashMap;

public class ArmorInventory {
    private ArrayList<InventorySlot> armorSlots;
    private final int size;
    private fr.iut.hev.root.model.entities.Player player;

    public ArmorInventory() {
        this.size = 4; // Helmet, Chestplate, Leggings, Boots
        this.armorSlots = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            armorSlots.add(new InventorySlot(i));
        }
        this.player = null;
    }

    public void setPlayer(fr.iut.hev.root.model.entities.Player player) {
        this.player = player;
    }

    public HashMap<Item, Integer> add(int slotIndex, Item item, int quantity) {
        if (slotIndex < 0 || slotIndex >= size) {
            return null;
        }

        // Check if the item is an armor piece
        if (item.getItemEnum().getItemType() != fr.iut.hev.root.model.enums.ItemTypesEnum.ARMOR_PIECE) {
            return null;
        }

        // Check if the armor piece is appropriate for the slot
        boolean isValidSlot = false;

        if (item instanceof ArmorPiece) {
            ArmorPiece armorPiece = (ArmorPiece) item;
            ArmorTypesEnum armorType = armorPiece.getArmorType();

            // Check if the armor type matches the slot index
            if (armorType != null && armorType.getSlotIndex() == slotIndex) {
                isValidSlot = true;
            }
        }

        if (!isValidSlot) {
            return null;
        }

        // Add the armor piece to the slot
        HashMap<Item, Integer> replacedItem = null;
        if (armorSlots.get(slotIndex).getItem() != null) {
            replacedItem = new HashMap<>();
            replacedItem.put(armorSlots.get(slotIndex).getItem(), armorSlots.get(slotIndex).getQuantity());
        }

        armorSlots.get(slotIndex).setItem(item);
        armorSlots.get(slotIndex).setQuantity(quantity);

        // Update player stats if player is set
        if (player != null) {
            player.updateHealthWithArmorBonus();
        }

        return replacedItem;
    }

    public HashMap<Item, Integer> remove(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= size || armorSlots.get(slotIndex).getItem() == null) {
            return null;
        }

        HashMap<Item, Integer> removedItem = new HashMap<>();
        removedItem.put(armorSlots.get(slotIndex).getItem(), armorSlots.get(slotIndex).getQuantity());

        armorSlots.get(slotIndex).setItem(null);
        armorSlots.get(slotIndex).setQuantity(0);

        // Update player stats if player is set
        if (player != null) {
            player.updateHealthWithArmorBonus();
        }

        return removedItem;
    }

    public ArrayList<InventorySlot> getArmorSlots() {
        return this.armorSlots;
    }

    public InventorySlot getArmorSlot(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= size) {
            return null;
        }
        return this.armorSlots.get(slotIndex);
    }

    public int getSize() {
        return this.size;
    }

    public int getTotalArmorValue() {
        int totalArmorValue = 0;

        for (InventorySlot slot : armorSlots) {
            if (slot.getItem() != null) {
                totalArmorValue += slot.getItem().getStats().getItemMainStat();
            }
        }

        return totalArmorValue;
    }
}
