package fr.iut.hev.root.model.tests;

import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.InventorySlot;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.items.Item;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
class InventoryTest {
    private final Inventory inventory = new Inventory();
    private final int slotIndex = 0;
    private final Item dirt = new Item(ItemsEnum.DIRT);
    private final Item stone = new Item(ItemsEnum.STONE);

    @Test
    void addToEmptySlot() {
        int quantity = 10;
        InventorySlot slot = inventory.getInventorySlot(slotIndex);

        assertNull(slot.getItem(), "Expected the slot to be empty before adding an item");

        HashMap<Item, Integer> result = inventory.add(slotIndex, dirt, quantity);

        assertNull(result, "Expected no items to be returned as \"replaced item\" when adding a new item to an empty slot");
        assertEquals(dirt, slot.getItem(), "Expected the item in the slot to be the one added");
        assertEquals(quantity, slot.getQuantity(), "Expected the quantity in the slot to match the added quantity");
    }

    @Test
    void testReplaceDifferentItemInSlot() {
        inventory.add(slotIndex, dirt, 5);
        InventorySlot slot = inventory.getInventorySlot(slotIndex);

        HashMap<Item, Integer> result = inventory.add(slotIndex, stone, 3);

        assertNotNull(result, "Expected item to be returned when replacing a different item in a non-empty slot");
        assertTrue(result.containsKey(dirt), "Expected the replaced item to be dirt");
        assertEquals(5, result.get(dirt), "Expected the quantity of the replaced item to be 5");
        assertEquals(stone, slot.getItem(), "Expected the item in the slot to be the new item (stone)");
        assertEquals(3, slot.getQuantity(), "Expected the quantity in the slot to match the new item's quantity");
    }

    @Test
    void testStackSameItemWithinLimit() {
        inventory.add(slotIndex, dirt, 10);
        assertEquals(dirt, inventory.getInventorySlot(slotIndex).getItem(), "Expected the item in the slot to be dirt");
        assertEquals(10, inventory.getInventorySlot(slotIndex).getQuantity(), "Expected the quantity in the slot to be 10");
        HashMap<Item, Integer> result = inventory.add(slotIndex, dirt, 20);
        assertNull(result, "Expected no items to be returned as \"replaced item\" when stacking the same item");
        assertEquals(dirt, inventory.getInventorySlot(slotIndex).getItem(), "Expected the item in the slot to remain dirt");
        assertEquals(30, inventory.getInventorySlot(slotIndex).getQuantity(), "Expected the quantity in the slot to be 30 after stacking");
    }

    @Test
    void testStackSameItemExceedLimit() {
        inventory.add(slotIndex, dirt, 50);
        assertEquals(dirt, inventory.getInventorySlot(slotIndex).getItem(), "Expected the item in the slot to be dirt");
        assertEquals(50, inventory.getInventorySlot(slotIndex).getQuantity(), "Expected the quantity in the slot to be 50");
        HashMap<Item, Integer> result = inventory.add(slotIndex, dirt, 51);
        assertNull(result, "Expected no items to be returned as \"replaced item\" when stacking the same item");
        assertEquals(dirt, inventory.getInventorySlot(slotIndex).getItem(), "Expected the item in the slot to remain dirt");
        assertEquals(dirt.getItemEnum().getLimitStacking(), inventory.getInventorySlot(slotIndex).getQuantity(),
                "Expected the quantity in the slot to be capped at the limit after stacking");
        assertEquals(dirt, inventory.getInventorySlot(slotIndex + 1).getItem());
        assertEquals(64, inventory.getInventorySlot(0).getQuantity());
    }

    @Test
    void testAddItemToFullInventory() {
        for (int i = 0; i < 50; i++) {
            inventory.add(i, new Item(ItemsEnum.DIRT), 1);
        }
        assertThrows(IndexOutOfBoundsException.class, () -> inventory.add(50, new Item(ItemsEnum.STONE), 1),
                "Expected an exception when trying to add an item to a full inventory");
    }

    @Test
    void testRemoveItemFromSlot() {
        inventory.add(slotIndex, dirt, 10);
        assertNotNull(inventory.getInventorySlot(slotIndex).getItem(), "Expected the slot to contain an item before removing");
        HashMap<Item, Integer> removedItem = inventory.remove(slotIndex, 5);
        assertNotNull(removedItem, "Expected items to be returned when removing from a non-empty slot");
        assertEquals(dirt, removedItem.keySet().iterator().next(), "Expected the removed item to be dirt");
        assertEquals(5, removedItem.get(dirt), "Expected the quantity of the removed item to be 5");
        assertEquals(5, inventory.getInventorySlot(slotIndex).getQuantity(), "Expected the quantity in the slot to be reduced to 5 after removal");
    }

    @Test
    void testRemoveItemFromEmptySlot() {
        assertNull(inventory.getInventorySlot(slotIndex).getItem(), "Expected the slot to be empty before removing");
        HashMap<Item, Integer> removedItem = inventory.remove(slotIndex, 5);
        assertNull(removedItem, "Expected no items to be returned when removing from an empty slot");
        assertNull(inventory.getInventorySlot(slotIndex).getItem(), "Expected the slot to remain empty after attempting to remove from it");
    }

    @Test
    void testRemoveItemWithExcessiveQuantity() {
        inventory.add(slotIndex, dirt, 10);
        assertNotNull(inventory.getInventorySlot(slotIndex).getItem(), "Expected the slot to contain an item before removing");
        HashMap<Item, Integer> removedItem = inventory.remove(slotIndex, 15);
        assertNotNull(removedItem, "Expected items to be returned when removing more than available quantity");
        assertEquals(dirt, removedItem.keySet().iterator().next(), "Expected the removed item to be dirt");
        assertEquals(10, removedItem.get(dirt), "Expected the quantity of the removed item to be 10 (all available)");
        assertEquals(0, inventory.getInventorySlot(slotIndex).getQuantity(), "Expected the slot to be empty after removing all items");
    }

    @Test
    void testGetAvailableRoomForItem() {
        inventory.add(slotIndex, dirt, 10);
        int availableRoom = inventory.getAvailableRoomForItem(dirt);
        assertEquals(54, availableRoom, "Expected 54 slots available for dirt after adding 10 (64 - 10)");

        inventory.add(1, new Item(ItemsEnum.DIRT), 50);
        availableRoom = inventory.getAvailableRoomForItem(dirt);
        assertEquals(4, availableRoom, "Expected only 4 slots available for dirt after adding 50 (64 - 50 - 10)");
    }

    @Test
    void testAddItemInInvalidSlot() {
        assertThrows(IndexOutOfBoundsException.class, () -> inventory.add(100, new Item(ItemsEnum.DIRT), 1),
                "Expected an exception when trying to add an item to an invalid slot index");
    }

    @Test
    void testRemoveItemFromInvalidSlot() {
        assertThrows(IndexOutOfBoundsException.class, () -> inventory.remove(100, 1),
                "Expected an exception when trying to remove an item from an invalid slot index");
    }

    @Test
    void testGetInventorySlotInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> inventory.getInventorySlot(100),
                "Expected an exception when trying to access an invalid slot index");
    }

    @Test
    void testGetAvailableRoomForItemInvalidItem() {
        Item invalidItem = new Item(ItemsEnum.WOOD); // Assuming WOOD is not in the inventory
        int availableRoom = inventory.getAvailableRoomForItem(invalidItem);
        assertEquals(64, availableRoom, "Expected 64 slots available for an item not in the inventory");
    }

    @Test
    void testRemoveItemFromSlotWithInvalidQuantity() {
        inventory.add(slotIndex, dirt, 10);
        assertNotNull(inventory.getInventorySlot(slotIndex).getItem(), "Expected the slot to contain an item before removing");
        HashMap<Item, Integer> removedItem = inventory.remove(slotIndex, -5);
        assertNull(removedItem, "Expected no items to be returned when trying to remove a negative quantity");
        assertEquals(10, inventory.getInventorySlot(slotIndex).getQuantity(), "Expected the quantity in the slot to remain unchanged after invalid removal attempt");
    }

    @Test
    void testRemoveItemFromEmptySlotWithInvalidQuantity() {
        assertNull(inventory.getInventorySlot(slotIndex).getItem(), "Expected the slot to be empty before removing");
        HashMap<Item, Integer> removedItem = inventory.remove(slotIndex, -5);
        assertNull(removedItem, "Expected no items to be returned when trying to remove a negative quantity from an empty slot");
        assertNull(inventory.getInventorySlot(slotIndex).getItem(), "Expected the slot to remain empty after invalid removal attempt");
    }

    @Test
    void testGetAvailableRoomForItemWithInvalidItem() {
        Item invalidItem = new Item(ItemsEnum.WOOD); // Assuming WOOD is not in the inventory
        int availableRoom = inventory.getAvailableRoomForItem(invalidItem);
        assertEquals(64, availableRoom, "Expected 64 slots available for an item not in the inventory");
    }
}