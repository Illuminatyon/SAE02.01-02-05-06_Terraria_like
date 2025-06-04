package fr.iut.hev.root.controller;

import fr.iut.hev.root.items.CraftingSystem;
import fr.iut.hev.root.items.Recipe;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.Map;

public class CraftingMenuController {

    private CraftingSystem craftingSystem;
    private Map<String, Integer> inventory;

    @FXML
    private VBox recipeList;

    public void initialize(CraftingSystem craftingSystem, Map<String, Integer> inventory) {
        this.craftingSystem = craftingSystem;
        this.inventory = inventory;
        displayCraftableRecipes();
    }

    public void displayCraftableRecipes() {
        recipeList.getChildren().clear();
        for (Recipe recipe : craftingSystem.getAvailableRecipes(inventory)) {
            Button craftButton = new Button("Craft: " + recipe.getResultItemId());
            craftButton.setOnAction(e -> {
                boolean success = craftingSystem.craft(recipe, inventory);
                System.out.println("Craft " + recipe.getResultItemId() + ": " + (success ? "OK" : "FAILED"));
                displayCraftableRecipes(); // refresh after crafting
            });
            recipeList.getChildren().add(craftButton);
        }
    }
}
