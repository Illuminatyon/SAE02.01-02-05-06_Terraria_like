package fr.iut.hev.root.model.craft;

import fr.iut.hev.root.model.inventory.Inventory;
import fr.iut.hev.root.model.items.enums.ItemsEnum;
import fr.iut.hev.root.model.items.ItemFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Map;

public class CraftingManager {

    /**
     * A class in charge of managing <b>the crafting system</b>.
     * <p>
     *     It stores all the recipes that are available to the player and interact with the inventory for the creation
     *     of the items crafted by the player. It uses the ItemFactory to create items.
     * </p>
     */
    private ObservableList<RecipesEnum> recipesAvailable;
    private Inventory inventory;
    private ObjectProperty<RecipesEnum> selectedRecipeProperty;
    private ItemFactory itemFactory;

    public CraftingManager(Inventory inventory) {
        this.inventory = inventory;
        this.recipesAvailable = FXCollections.observableArrayList();
        this.selectedRecipeProperty = new SimpleObjectProperty<>(null);
        this.itemFactory = ItemFactory.getInstance();
        initCraftingManager();
    }

    /**
     * <p>Looks through all recipes among RecipesEnum and check if it's available for the inventory. If so, includes
     * the recipe to the available recipes for the player.</p>
     */
    private void initCraftingManager() {
        for (RecipesEnum recipesEnum : RecipesEnum.values()) {
            if (recipesEnum.getRecipeAvailability() == RecipeAvailability.INVENTORY) {
                recipesAvailable.add(recipesEnum);
            }
        }
    }

    /**
     * Checks if the player has the required items of the recipe in order to craft its item.
     * @param recipesEnum The crafted item's recipe
     * @return True if the player has enough items, false otherwise
     */
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
    public ObjectProperty<RecipesEnum> selectedRecipeProperty() {return this.selectedRecipeProperty;}
}
