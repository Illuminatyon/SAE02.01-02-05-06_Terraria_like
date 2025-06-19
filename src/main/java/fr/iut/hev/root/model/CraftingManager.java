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

    public CraftingManager(Inventory inventory,ItemFactory itemFactory) {
        this.inventory = inventory;
        this.recipesAvailable = FXCollections.observableArrayList();
        this.selectedRecipeProperty = new SimpleObjectProperty<>(null);
        this.itemFactory = itemFactory;
        initCraftingManager();
    }

    private void initCraftingManager() {
        for (RecipesEnum recipesEnum : RecipesEnum.values()) {
            if (recipesEnum.getRecipeAvailability() == RecipeAvailability.INVENTORY) {
                recipesAvailable.add(recipesEnum);
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
    public void addRecipe(RecipesEnum recipesEnum) {recipesAvailable.add(recipesEnum);}
}
