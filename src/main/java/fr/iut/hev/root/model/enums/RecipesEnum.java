package fr.iut.hev.root.model.enums;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public enum RecipesEnum {

    RAW_CHICKEN(ItemsEnum.RAW_CHICKEN,3,new HashMap<>(Map.ofEntries(new AbstractMap.SimpleEntry<>(ItemsEnum.RAW_CHICKEN,1))));

    private ItemsEnum craftResult;
    private int itemCraftedQuantity;
    private HashMap<ItemsEnum, Integer> ingredients;

    RecipesEnum(ItemsEnum craftResult,int itemCraftedQuantity,HashMap<ItemsEnum, Integer> ingredients) {
        this.craftResult = craftResult;
        this.itemCraftedQuantity = itemCraftedQuantity;
        this.ingredients = ingredients;
    }
}
