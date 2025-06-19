package fr.iut.hev.root.model;

import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import fr.iut.hev.root.model.items.ArmorPiece;
import fr.iut.hev.root.model.items.Item;
import fr.iut.hev.root.model.items.ItemFactory;

public class ArmorTypeTest {
    public static void main(String[] args) {
        // Create a HitboxManager (needed for ItemFactory)
        HitboxManager hitboxManager = new HitboxManager();
        
        // Create an ItemFactory
        ItemFactory itemFactory = new ItemFactory(hitboxManager);
        
        // Create an ArmorInventory
        ArmorInventory armorInventory = new ArmorInventory();
        
        // Create armor pieces
        Item helmet = itemFactory.createItem(ItemsEnum.IRON_HELMET);
        Item chestplate = itemFactory.createItem(ItemsEnum.IRON_CHESTPLATE);
        Item leggings = itemFactory.createItem(ItemsEnum.IRON_LEGGINGS);
        Item boots = itemFactory.createItem(ItemsEnum.IRON_BOOTS);
        
        // Test placing armor pieces in correct slots
        System.out.println("Testing placing armor pieces in correct slots:");
        System.out.println("Helmet in slot 0: " + (armorInventory.add(0, helmet, 1) == null ? "Success" : "Failed"));
        System.out.println("Chestplate in slot 1: " + (armorInventory.add(1, chestplate, 1) == null ? "Success" : "Failed"));
        System.out.println("Leggings in slot 2: " + (armorInventory.add(2, leggings, 1) == null ? "Success" : "Failed"));
        System.out.println("Boots in slot 3: " + (armorInventory.add(3, boots, 1) == null ? "Success" : "Failed"));
        
        // Test placing armor pieces in incorrect slots
        System.out.println("\nTesting placing armor pieces in incorrect slots:");
        System.out.println("Helmet in slot 1: " + (armorInventory.add(1, helmet, 1) == null ? "Failed (should not be allowed)" : "Success (correctly rejected)"));
        System.out.println("Chestplate in slot 2: " + (armorInventory.add(2, chestplate, 1) == null ? "Failed (should not be allowed)" : "Success (correctly rejected)"));
        System.out.println("Leggings in slot 3: " + (armorInventory.add(3, leggings, 1) == null ? "Failed (should not be allowed)" : "Success (correctly rejected)"));
        System.out.println("Boots in slot 0: " + (armorInventory.add(0, boots, 1) == null ? "Failed (should not be allowed)" : "Success (correctly rejected)"));
        
        // Print armor type information
        System.out.println("\nArmor type information:");
        System.out.println("Helmet armor type: " + ((ArmorPiece)helmet).getArmorType());
        System.out.println("Chestplate armor type: " + ((ArmorPiece)chestplate).getArmorType());
        System.out.println("Leggings armor type: " + ((ArmorPiece)leggings).getArmorType());
        System.out.println("Boots armor type: " + ((ArmorPiece)boots).getArmorType());
    }
}