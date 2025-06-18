package fr.iut.hev.root.model;

import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.enums.RecipeAvailability;
import fr.iut.hev.root.model.enums.RecipesEnum;
import fr.iut.hev.root.model.items.ItemFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Map;

public class CraftingManager {

    private ObservableList<RecipesEnum> recipesAvailable;
    private Inventory inventory;
    private ObjectProperty<RecipesEnum> selectedRecipeProperty;
    private ItemFactory itemFactory;
    private boolean nearCraftingTable;
    private boolean nearFurnace;

    public CraftingManager(Inventory inventory,ItemFactory itemFactory) {
        this.inventory = inventory;
        this.recipesAvailable = FXCollections.observableArrayList();
        this.selectedRecipeProperty = new SimpleObjectProperty<>(null);
        this.itemFactory = itemFactory;
        this.nearCraftingTable = false;
        this.nearFurnace = false;
        initCraftingManager();
    }

    private void initCraftingManager() {
        updateAvailableRecipes();
    }

    /**
     * Updates the list of available recipes based on the player's proximity to crafting stations
     */
    public void updateAvailableRecipes() {
        recipesAvailable.clear();

        // Always add inventory recipes
        for (RecipesEnum recipesEnum : RecipesEnum.values()) {
            if (recipesEnum.getRecipeAvailability() == RecipeAvailability.INVENTORY) {
                recipesAvailable.add(recipesEnum);
            }
        }

        // Add crafting table recipes if near a crafting table
        if (nearCraftingTable) {
            for (RecipesEnum recipesEnum : RecipesEnum.values()) {
                if (recipesEnum.getRecipeAvailability() == RecipeAvailability.CRAFTING_TABLE) {
                    recipesAvailable.add(recipesEnum);
                }
            }
        }

        // Add furnace recipes if near a furnace
        if (nearFurnace) {
            for (RecipesEnum recipesEnum : RecipesEnum.values()) {
                if (recipesEnum.getRecipeAvailability() == RecipeAvailability.FURNACE) {
                    recipesAvailable.add(recipesEnum);
                }
            }
        }
    }

    private boolean craftPossible(RecipesEnum recipesEnum) {
        for (Map.Entry<ItemsEnum, Integer> ingredient : recipesEnum.getIngredients().entrySet()) {
            if (ingredient.getValue() > inventory.getItemIteration(ingredient.getKey()))
                return false;
        }
        return true;
    }

    public void crafts() {
        if (craftPossible(getSelectedRecipe())) {
            destroysIngredientsFromInventory();
            inventory.addFromCraft(itemFactory.createItem(getSelectedRecipe().getCraftResult()),getSelectedRecipe().getItemCraftedQuantity());
        }
    }

    public void destroysIngredientsFromInventory() {
        for (Map.Entry<ItemsEnum, Integer> ingredients : getSelectedRecipe().getIngredients().entrySet()) {
            inventory.remove(ingredients.getKey(),ingredients.getValue());
        }
    }

    public ObservableList<RecipesEnum> getRecipesAvailable() {
        return this.recipesAvailable;
    }

    public RecipesEnum getSelectedRecipe() {return this.selectedRecipeProperty.getValue();}
    public void setSelectedRecipe(RecipesEnum recipe) {this.selectedRecipeProperty.setValue(recipe);}
    public ObjectProperty<RecipesEnum> selectedRecipeProperty() {return this.selectedRecipeProperty;}

    public boolean isNearCraftingTable() {
        return nearCraftingTable;
    }

    public void setNearCraftingTable(boolean nearCraftingTable) {
        this.nearCraftingTable = nearCraftingTable;
        updateAvailableRecipes();
    }

    public boolean isNearFurnace() {
        return nearFurnace;
    }

    public void setNearFurnace(boolean nearFurnace) {
        this.nearFurnace = nearFurnace;
        updateAvailableRecipes();
    }
}
