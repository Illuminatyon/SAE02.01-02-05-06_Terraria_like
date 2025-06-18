package fr.iut.hev.root.model.enums;

import fr.iut.hev.root.model.items.Item;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public enum RecipesEnum {

    RAW_CHICKEN(ItemsEnum.RAW_CHICKEN,3,new HashMap<>(Map.ofEntries(new AbstractMap.SimpleEntry<>(ItemsEnum.RAW_CHICKEN,1))),RecipeAvailability.INVENTORY),
    STICK(ItemsEnum.STICK,4,new HashMap<>(Map.ofEntries(new AbstractMap.SimpleEntry<>(ItemsEnum.WOOD,2))),RecipeAvailability.INVENTORY),

    CRAFTING_TABLE(ItemsEnum.CRAFTING_TABLE,1,new HashMap<>(Map.ofEntries(new AbstractMap.SimpleEntry<>(ItemsEnum.STICK,2),new AbstractMap.SimpleEntry<>(ItemsEnum.WOOD,2))),RecipeAvailability.INVENTORY),
    FURNACE(ItemsEnum.FURNACE,1,new HashMap<>(Map.ofEntries(new AbstractMap.SimpleEntry<>(ItemsEnum.STONE,5))),RecipeAvailability.INVENTORY),

    WOODEN_PICKAXE(ItemsEnum.WOODEN_PICKAXE,1,new HashMap<>(Map.ofEntries(new AbstractMap.SimpleEntry<>(ItemsEnum.STICK,2),new AbstractMap.SimpleEntry<>(ItemsEnum.WOOD,3))),RecipeAvailability.INVENTORY),
    WOODEN_HAX(ItemsEnum.WOODEN_HAX,1,new HashMap<>(Map.ofEntries(new AbstractMap.SimpleEntry<>(ItemsEnum.STICK,2),new AbstractMap.SimpleEntry<>(ItemsEnum.WOOD,3))),RecipeAvailability.INVENTORY),
    WOODEN_SHOVEL(ItemsEnum.WOODEN_SHOVEL,1,new HashMap<>(Map.ofEntries(new AbstractMap.SimpleEntry<>(ItemsEnum.STICK,2),new AbstractMap.SimpleEntry<>(ItemsEnum.WOOD,1))),RecipeAvailability.INVENTORY),
    HAMMER(ItemsEnum.HAMMER,1,new HashMap<>(Map.ofEntries(new AbstractMap.SimpleEntry<>(ItemsEnum.STICK,2),new AbstractMap.SimpleEntry<>(ItemsEnum.IRON_INGOT,3))),RecipeAvailability.INVENTORY),
    DAGGER(ItemsEnum.DAGGER,1,new HashMap<>(Map.ofEntries(new AbstractMap.SimpleEntry<>(ItemsEnum.STICK,1),new AbstractMap.SimpleEntry<>(ItemsEnum.IRON_INGOT,1))),RecipeAvailability.INVENTORY),

    COOKED_CHICKEN(ItemsEnum.COOKED_CHICKEN,1,new HashMap<>(Map.ofEntries(new AbstractMap.SimpleEntry<>(ItemsEnum.RAW_CHICKEN,1))),RecipeAvailability.FURNACE),

    KATANA(ItemsEnum.KATANA,1,new HashMap<>(Map.ofEntries(new AbstractMap.SimpleEntry<>(ItemsEnum.STICK,2),new AbstractMap.SimpleEntry<>(ItemsEnum.IRON_INGOT,3))),RecipeAvailability.CRAFTING_TABLE),
    BOW(ItemsEnum.BOW,1,new HashMap<>(Map.ofEntries(new AbstractMap.SimpleEntry<>(ItemsEnum.STICK,3),new AbstractMap.SimpleEntry<>(ItemsEnum.FEATHER,2))),RecipeAvailability.CRAFTING_TABLE),

    CACA(ItemsEnum.CACA,3,new HashMap<>(Map.ofEntries(new AbstractMap.SimpleEntry<>(ItemsEnum.DIRT,2))),RecipeAvailability.INVENTORY);


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
    public RecipeAvailability getRecipeAvailability() {return recipeAvailability;}
    public HashMap<ItemsEnum, Integer> getIngredients() {return ingredients;}
    public int getItemCraftedQuantity() {return this.itemCraftedQuantity;}
}
