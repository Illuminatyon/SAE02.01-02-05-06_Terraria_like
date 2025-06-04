package fr.iut.hev.root.model.items;

import fr.iut.hev.root.model.enums.Items;

import java.util.*;

public class CraftingManager {
    private static final List<Recipe> recipes = new ArrayList<>();

    static {
        // Exemple de recettes
        recipes.add(new Recipe(Items.STICK, 4, Map.of(Items.WOOD, 2)));
        recipes.add(new Recipe(Items.PIOCHE, 1, Map.of(Items.WOOD, 1, Items.STONE, 3)));
        recipes.add(new Recipe(Items.HAMMER, 1, Map.of(Items.STICK, 1, Items.IRON_INGOT, 2)));
    }

    public static List<Recipe> getRecipes() {
        return recipes;
    }

    public static Optional<Recipe> getRecipeFor(Items targetItem) {
        return recipes.stream().filter(r -> r.getResult() == targetItem).findFirst();
    }

    public static boolean canCraft(Recipe recipe, Map<Items, Integer> inventory) {
        for (Map.Entry<Items, Integer> entry : recipe.getIngredients().entrySet()) {
            if (inventory.getOrDefault(entry.getKey(), 0) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    public static boolean craft(Recipe recipe, Map<Items, Integer> inventory) {
        if (!canCraft(recipe, inventory)) return false;

        // Consommer les ressources
        for (Map.Entry<Items, Integer> entry : recipe.getIngredients().entrySet()) {
            inventory.put(entry.getKey(), inventory.get(entry.getKey()) - entry.getValue());
        }

        // Ajouter l'objet crafté
        inventory.put(recipe.getResult(), inventory.getOrDefault(recipe.getResult(), 0) + recipe.getAmount());
        return true;
    }
}
