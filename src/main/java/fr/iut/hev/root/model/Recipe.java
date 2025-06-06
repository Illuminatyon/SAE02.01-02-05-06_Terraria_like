package fr.iut.hev.root.model;

import fr.iut.hev.root.model.enums.Items;
import java.util.Map;

public class Recipe {
    private final Items result;
    private final int amount;
    private final Map<Items, Integer> ingredients;

    public Recipe(Items result, int amount, Map<Items, Integer> ingredients) {
        this.result = result;
        this.amount = amount;
        this.ingredients = ingredients;
    }

    public Items getResult() {
        return result;
    }

    public int getAmount() {
        return amount;
    }

    public Map<Items, Integer> getIngredients() {
        return ingredients;
    }

    public String getQuantity() {
        return amount > 1 ? " (" + amount + ")" : "";
    }
}
