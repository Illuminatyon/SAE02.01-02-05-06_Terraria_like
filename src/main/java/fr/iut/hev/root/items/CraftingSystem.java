package fr.iut.hev.root.items;

import java.util.*;

public class CraftingSystem {
    private Map<String, Item> itemMap; // id -> Item
    private List<Recipe> recipes;

    public CraftingSystem(List<Item> items, List<Recipe> recipes) {
        this.itemMap = new HashMap<>();
        for (Item item : items) {
            itemMap.put(item.getId(), item);
        }
        this.recipes = recipes;
    }

    public List<Recipe> getAvailableRecipes(Map<String, Integer> inventory) {
        List<Recipe> available = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (canCraft(recipe, inventory)) {
                available.add(recipe);
            }
        }
        return available;
    }

    public boolean canCraft(Recipe recipe, Map<String, Integer> inventory) {
        for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
            int have = inventory.getOrDefault(entry.getKey(), 0);
            if (have < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    public boolean craft(Recipe recipe, Map<String, Integer> inventory) {
        if (!canCraft(recipe, inventory)) return false;

        // Retirer les ingrédients
        for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
            inventory.put(entry.getKey(), inventory.get(entry.getKey()) - entry.getValue());
        }

        // Ajouter l'objet crafté
        inventory.put(recipe.getResultItemId(),
                inventory.getOrDefault(recipe.getResultItemId(), 0) + recipe.getResultQuantity());

        return true;
    }

    public Item getItemById(String id) {
        return itemMap.get(id);
    }
}
