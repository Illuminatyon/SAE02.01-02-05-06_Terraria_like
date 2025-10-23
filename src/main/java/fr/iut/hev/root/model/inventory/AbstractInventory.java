package fr.iut.hev.root.model.inventory;

import fr.iut.hev.root.model.items.enums.ItemsEnum;
import fr.iut.hev.root.model.items.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * Classe abstraite représentant un inventaire générique.
 * Utilise le pattern Template Method pour permettre différents types d'inventaires
 * avec des comportements personnalisés.
 */
public abstract class AbstractInventory {
    protected final ArrayList<InventorySlot> slots;
    protected final int size;
    protected int slotsOccupied;

    protected AbstractInventory(int size) {
        this.size = size;
        this.slotsOccupied = 0;
        this.slots = new ArrayList<>(size);
        initializeSlots();
    }

    private void initializeSlots() {
        for (int i = 0; i < size; i++) {
            slots.add(new InventorySlot(i));
        }
    }

    public final HashMap<Item, Integer> addToSlot(int slotIndex, Item item, int quantity) {
        if (!isValidSlotIndex(slotIndex) || item == null) {
            return null;
        }

        if (!canAcceptItem(item)) {
            onItemRejected(item, quantity);
            return null;
        }

        InventorySlot slot = slots.get(slotIndex);
        HashMap<Item, Integer> result;

        if (slot.isEmpty()) {
            result = addToEmptySlot(slot, item, quantity);
        } else {
            result = addToOccupiedSlot(slot, item, quantity);
        }

        if (result == null || result.isEmpty()) {
            onItemAdded(item, quantity);
        }

        return result;
    }

    public final void add(Item item, int quantity) {
        if (isFull() || item == null) {
            return;
        }

        if (!canAcceptItem(item)) {
            onItemRejected(item, quantity);
            return;
        }

        Optional<Integer> slotIndex = findBestSlotForItem(item);
        slotIndex.ifPresent(index -> addToSlot(index, item, quantity));
    }

    public final void addFromCraft(Item item, int quantity) {
        if (item == null || !canAcceptItem(item)) {
            return;
        }

        int remainingQuantity = fillExistingStacks(item, quantity);
        if (remainingQuantity > 0) {
            fillEmptySlots(item, remainingQuantity);
        }
    }

    public final HashMap<Item, Integer> removeFromSlot(int slotIndex, int quantity) {
        if (!isValidSlotIndex(slotIndex)) {
            return null;
        }

        InventorySlot slot = slots.get(slotIndex);
        if (slot.isEmpty()) {
            return null;
        }

        HashMap<Item, Integer> removedItem = new HashMap<>();
        removedItem.put(slot.getItem(), quantity);
        slot.remove(quantity);

        if (slot.isEmpty()) {
            slotsOccupied--;
        }

        onItemRemoved(slot.getItem(), quantity);
        return removedItem;
    }

    public final void remove(ItemsEnum itemType, int quantity) {
        if (itemType == null || quantity <= 0) {
            return;
        }

        int totalAvailable = getItemCount(itemType);
        int toRemove = Math.min(quantity, totalAvailable);

        removeItemsFromSlots(itemType, toRemove);
    }

    protected abstract boolean canAcceptItem(Item item);

    protected void onItemAdded(Item item, int quantity) {
    }

    protected void onItemRemoved(Item item, int quantity) {
    }

    protected void onItemRejected(Item item, int quantity) {
    }

    protected void onInventoryFull() {
    }

    private HashMap<Item, Integer> addToEmptySlot(InventorySlot slot, Item item, int quantity) {
        slot.setItem(item);
        slot.setQuantity(quantity);
        slotsOccupied++;

        if (isFull()) {
            onInventoryFull();
        }

        return null;
    }

    private HashMap<Item, Integer> addToOccupiedSlot(InventorySlot slot, Item item, int quantity) {
        if (isDifferentItem(slot, item)) {
            return replaceItemInSlot(slot, item, quantity);
        }

        if (isSameItemWithSpaceAvailable(slot, item)) {
            return stackItemInSlot(slot, item, quantity);
        }

        return createExcessMap(item, quantity);
    }

    private boolean isDifferentItem(InventorySlot slot, Item item) {
        return slot.getItem().getItemEnum() != item.getItemEnum();
    }

    private boolean isSameItemWithSpaceAvailable(InventorySlot slot, Item item) {
        return slot.getQuantity() < item.getItemEnum().getLimitStacking();
    }

    private HashMap<Item, Integer> replaceItemInSlot(InventorySlot slot, Item item, int quantity) {
        HashMap<Item, Integer> replacedItem = new HashMap<>();
        replacedItem.put(slot.getItem(), slot.getQuantity());
        slot.setItem(item);
        slot.setQuantity(quantity);
        return replacedItem;
    }

    private HashMap<Item, Integer> stackItemInSlot(InventorySlot slot, Item item, int quantity) {
        int stackLimit = item.getItemEnum().getLimitStacking();
        int newTotal = slot.getQuantity() + quantity;

        if (newTotal > stackLimit) {
            int excess = newTotal - stackLimit;
            slot.setQuantity(stackLimit);
            return createExcessMap(item, excess);
        }

        slot.setQuantity(newTotal);
        return null;
    }

    private HashMap<Item, Integer> createExcessMap(Item item, int quantity) {
        HashMap<Item, Integer> excess = new HashMap<>();
        excess.put(item, quantity);
        return excess;
    }

    protected Optional<Integer> findBestSlotForItem(Item item) {
        Optional<Integer> partialStackSlot = findPartialStackSlot(item);
        if (partialStackSlot.isPresent()) {
            return partialStackSlot;
        }
        return findFirstEmptySlot();
    }

    private Optional<Integer> findPartialStackSlot(Item item) {
        for (int i = 0; i < size; i++) {
            InventorySlot slot = slots.get(i);
            if (isPartialStackOfItem(slot, item)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    private boolean isPartialStackOfItem(InventorySlot slot, Item item) {
        return !slot.isEmpty()
                && slot.getItem().getItemEnum() == item.getItemEnum()
                && slot.getQuantity() < item.getItemEnum().getLimitStacking();
    }

    private Optional<Integer> findFirstEmptySlot() {
        for (int i = 0; i < size; i++) {
            if (slots.get(i).isEmpty()) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    private int fillExistingStacks(Item item, int quantity) {
        int remaining = quantity;

        for (InventorySlot slot : slots) {
            if (remaining <= 0) {
                return 0;
            }

            if (isPartialStackOfItem(slot, item)) {
                remaining = fillSlotWithItem(slot, item, remaining);
            }
        }

        return remaining;
    }

    private int fillSlotWithItem(InventorySlot slot, Item item, int quantity) {
        int stackLimit = item.getItemEnum().getLimitStacking();
        int spaceAvailable = stackLimit - slot.getQuantity();

        if (spaceAvailable >= quantity) {
            slot.setQuantity(slot.getQuantity() + quantity);
            return 0;
        }

        slot.setQuantity(stackLimit);
        return quantity - spaceAvailable;
    }

    private void fillEmptySlots(Item item, int quantity) {
        int remaining = quantity;
        int stackLimit = item.getItemEnum().getLimitStacking();

        for (InventorySlot slot : slots) {
            if (remaining <= 0) {
                return;
            }

            if (slot.isEmpty()) {
                int quantityToAdd = Math.min(remaining, stackLimit);
                slot.setItem(item);
                slot.setQuantity(quantityToAdd);
                remaining -= quantityToAdd;
                slotsOccupied++;
            }
        }
    }

    private void removeItemsFromSlots(ItemsEnum itemType, int quantityToRemove) {
        int remaining = quantityToRemove;

        for (InventorySlot slot : slots) {
            if (remaining <= 0) {
                return;
            }

            if (slotContainsItem(slot, itemType)) {
                remaining = removeFromSlotUntilEmpty(slot, remaining);
            }
        }
    }

    private boolean slotContainsItem(InventorySlot slot, ItemsEnum itemType) {
        return !slot.isEmpty() && slot.getItem().getItemEnum() == itemType;
    }

    private int removeFromSlotUntilEmpty(InventorySlot slot, int quantityToRemove) {
        int slotQuantity = slot.getQuantity();
        if (quantityToRemove >= slotQuantity) {
            slot.remove(slotQuantity);
            return quantityToRemove - slotQuantity;
        }
        slot.remove(quantityToRemove);
        return 0;
    }

    public int getItemCount(ItemsEnum itemType) {
        if (itemType == null) {
            return 0;
        }

        return slots.stream()
                .filter(slot -> !slot.isEmpty() && slot.getItem().getItemEnum() == itemType)
                .mapToInt(InventorySlot::getQuantity)
                .sum();
    }

    public int getAvailableRoomForItem(Item item) {
        if (item == null) {
            return 0;
        }
        int availableRoom = 0;
        int stackLimit = item.getItemEnum().getLimitStacking();

        for (InventorySlot slot : slots) {
            if (!slot.isEmpty() && slot.getItem().getItemEnum() == item.getItemEnum()) {
                availableRoom += stackLimit - slot.getQuantity();
            }
        }

        return availableRoom;
    }

    public boolean hasEnoughItems(ItemsEnum itemType, int requiredQuantity) {
        return getItemCount(itemType) >= requiredQuantity;
    }

    protected boolean isValidSlotIndex(int index) {
        return index >= 0 && index < size;
    }

    public ArrayList<InventorySlot> getSlots() {
        return this.slots;
    }

    public InventorySlot getInventorySlot(int slotIndex) {
        if (!isValidSlotIndex(slotIndex)) {
            throw new IllegalArgumentException("Invalid slot index: " + slotIndex);
        }
        return this.slots.get(slotIndex);
    }

    public int getSize() {
        return this.size;
    }

    public int getSlotsOccupied() {
        return this.slotsOccupied;
    }

    public boolean isFull() {
        return slotsOccupied >= size;
    }

    @Deprecated
    public HashMap<Item, Integer> remove(int slotIndex, int quantity) {
        return removeFromSlot(slotIndex, quantity);
    }

    @Deprecated
    public int getItemIteration(ItemsEnum itemsEnum) {
        return getItemCount(itemsEnum);
    }
}