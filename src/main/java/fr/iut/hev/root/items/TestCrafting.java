package fr.iut.hev.root.items;

import java.util.*;

public class TestCrafting {
    public static void main(String[] args) {
        // Créer quelques items
        List<Item> items = List.of(
                new Item("1", "Wood", "Material"),
                new Item("2", "Stone", "Material"),
                new Item("3", "Axe", "Tool")
        );

        // Créer des recettes
        Map<String, Integer> axeIngredients = new HashMap<>();
        axeIngredients.put("1", 3); // 3 Wood
        axeIngredients.put("2", 2); // 2 Stone
        Recipe axeRecipe = new Recipe("3", 1, axeIngredients);

        List<Recipe> recipes = List.of(axeRecipe);

        // Créer le système de crafting
        CraftingSystem craftingSystem = new CraftingSystem(items, recipes);

        // Créer un inventaire avec suffisamment de ressources
        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("1", 5);
        inventory.put("2", 5);

        // Vérifier ce qu'on peut crafter
        List<Recipe> available = craftingSystem.getAvailableRecipes(inventory);
        System.out.println("Recettes disponibles : " + available);

        // Essayer de crafter une hache (axe)
        boolean success = craftingSystem.craft(axeRecipe, inventory);
        System.out.println("Crafting Axe: " + (success ? "Réussi" : "Échoué"));

        // Afficher l'inventaire après crafting
        System.out.println("Inventaire après crafting : " + inventory);
    }
}
