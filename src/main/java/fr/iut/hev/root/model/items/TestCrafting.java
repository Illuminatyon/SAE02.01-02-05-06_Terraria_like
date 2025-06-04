package fr.iut.hev.root.model.items;

import fr.iut.hev.root.model.enums.Items;
import fr.iut.hev.root.model.items.CraftingManager;
import fr.iut.hev.root.model.items.Recipe;

import java.util.HashMap;
import java.util.Map;

public class TestCrafting {
    public static void main(String[] args) {
        // Simule un inventaire de base
        Map<Items, Integer> inventory = new HashMap<>();
        inventory.put(Items.WOOD, 10);
        inventory.put(Items.STONE, 3);

        // Affiche l'inventaire de départ
        System.out.println("=== INVENTAIRE INITIAL ===");
        printInventory(inventory);

        // On tente de crafter une pioche
        Items itemToCraft = Items.PIOCHE;
        Recipe recipe = CraftingManager.getRecipeFor(itemToCraft).orElse(null);

        if (recipe == null) {
            System.out.println("❌ Aucune recette trouvée pour " + itemToCraft.getName());
            return;
        }

        System.out.println("\n--- Tentative de craft : " + itemToCraft.getName() + " ---");
        if (CraftingManager.canCraft(recipe, inventory)) {
            System.out.println("✅ Les ressources sont suffisantes. Craft en cours...");
            CraftingManager.craft(recipe, inventory);
        } else {
            System.out.println("❌ Pas assez de ressources pour crafter " + itemToCraft.getName());
        }

        // Affiche l'inventaire final
        System.out.println("\n=== INVENTAIRE APRÈS CRAFT ===");
        printInventory(inventory);
    }

    private static void printInventory(Map<Items, Integer> inventory) {
        for (Map.Entry<Items, Integer> entry : inventory.entrySet()) {
            System.out.println("- " + entry.getKey().getName() + " : " + entry.getValue());
        }
    }
}
