package fr.iut.hev.root.model;

import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.enums.RecipeAvailability;
import fr.iut.hev.root.model.enums.RecipesEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Map;

public class CraftingManager {

    private ObservableList<RecipesEnum> recipesAvailable;
    private Inventory inventory;

    public CraftingManager(Inventory inventory) {
        this.inventory = inventory;
        this.recipesAvailable = FXCollections.observableArrayList();
        initCraftingManager();
    }

    private void initCraftingManager() {
        for (RecipesEnum recipesEnum : RecipesEnum.values()) {
            if (recipesEnum.getRecipeAvailability() == RecipeAvailability.INVENTORY)
                recipesAvailable.add(recipesEnum);
        }
    }

    private boolean isCraftPossible(RecipesEnum recipesEnum) {
        for (Map.Entry<ItemsEnum, Integer> ingredient : recipesEnum.getIngredients().entrySet()) {
            if (ingredient != inventory.getItemIteration(ingredient.getKey()))
                return false;
        }
        return true;
    }

    public ObservableList<RecipesEnum> getRecipesAvailable() {return this.recipesAvailable;}
}
