package fr.iut.hev.root.model;

import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import fr.iut.hev.root.model.items.Item;
import fr.iut.hev.root.model.items.ItemFactory;

public class ArmorHealthBonusTest {
    public static void main(String[] args) {
        // Create a HitboxManager (needed for ItemFactory)
        HitboxManager hitboxManager = new HitboxManager();
        
        // Create an ItemFactory
        ItemFactory itemFactory = new ItemFactory(hitboxManager);
        
        // Create a TileMap (needed for Player)
        TileMap tileMap = new TileMap(1920, 1056, itemFactory);
        
        // Create a Player
        Player player = new Player(100, 100, 32, 64, tileMap, 5, 10, 5, ActorEnum.PLAYER);
        
        // Create armor pieces
        Item helmet = itemFactory.createItem(ItemsEnum.IRON_HELMET);
        Item chestplate = itemFactory.createItem(ItemsEnum.IRON_CHESTPLATE);
        Item leggings = itemFactory.createItem(ItemsEnum.IRON_LEGGINGS);
        Item boots = itemFactory.createItem(ItemsEnum.IRON_BOOTS);
        
        // Test health bonus functionality
        System.out.println("Testing armor health bonus functionality:");
        
        // Check initial max health
        System.out.println("Initial max health: " + player.getMaxHealth());
        
        // Add helmet and check max health
        player.getArmorInventory().add(0, helmet, 1);
        System.out.println("Max health with helmet: " + player.getMaxHealth());
        
        // Add chestplate and check max health
        player.getArmorInventory().add(1, chestplate, 1);
        System.out.println("Max health with helmet and chestplate: " + player.getMaxHealth());
        
        // Add leggings and check max health
        player.getArmorInventory().add(2, leggings, 1);
        System.out.println("Max health with helmet, chestplate, and leggings: " + player.getMaxHealth());
        
        // Add boots and check max health
        player.getArmorInventory().add(3, boots, 1);
        System.out.println("Max health with full armor set: " + player.getMaxHealth());
        
        // Remove helmet and check max health
        player.getArmorInventory().remove(0);
        System.out.println("Max health after removing helmet: " + player.getMaxHealth());
        
        // Remove all armor and check max health
        player.getArmorInventory().remove(1);
        player.getArmorInventory().remove(2);
        player.getArmorInventory().remove(3);
        System.out.println("Max health after removing all armor: " + player.getMaxHealth());
    }
}