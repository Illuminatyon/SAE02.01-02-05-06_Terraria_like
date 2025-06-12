package fr.iut.hev.root.model.enums;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public enum RecipesEnum {

    RAW_CHICKEN(ItemsEnum.RAW_CHICKEN,3,new HashMap<>(Map.ofEntries(new AbstractMap.SimpleEntry<>(ItemsEnum.RAW_CHICKEN,1))),RecipeAvailability.INVENTORY);

    private ItemsEnum craftResult;
    private int itemCraftedQuantity;
    private HashMap<ItemsEnum, Integer> ingredients;
    private RecipeAvailability recipeAvailability;

    RecipesEnum(ItemsEnum craftResult,int itemCraftedQuantity,HashMap<ItemsEnum, Integer> ingredients,RecipeAvailability recipeAvailability) {
        this.craftResult = craftResult;
        this.itemCraftedQuantity = itemCraftedQuantity;
        this.ingredients = ingredients;
        this.recipeAvailability = recipeAvailability;
    }

    public ItemsEnum getCraftResult() {return craftResult;}
    public RecipeAvailability getRawChicken() {return recipeAvailability;}
    public HashMap<ItemsEnum, Integer> getIngredients() {return ingredients;}
}
