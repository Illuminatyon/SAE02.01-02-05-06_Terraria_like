package fr.iut.hev.root.model;

import fr.iut.hev.root.model.enums.ItemsEnum;
import java.util.Map;

public class Recipe {
    private final ItemsEnum result;
    private final int amount;
    private final Map<ItemsEnum, Integer> ingredients;

    public Recipe(ItemsEnum result, int amount, Map<ItemsEnum, Integer> ingredients) {
        this.result = result;
        this.amount = amount;
        this.ingredients = ingredients;
    }

    public ItemsEnum getResult() {
        return result;
    }

    public int getAmount() {
        return amount;
    }

    public Map<ItemsEnum, Integer> getIngredients() {
        return ingredients;
    }

    public String getQuantity() {
        return amount > 1 ? " (" + amount + ")" : "";
    }
}
