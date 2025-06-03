package fr.iut.hev.root.items;

import java.util.Map;

public class Recipe {

    private String resultItemId;
    private int resultQuantity;
    private Map<String, Integer> ingredients;

    public Recipe(String resultItemId, int resultQuantity, Map<String, Integer> ingredients) {
        this.resultItemId = resultItemId;
        this.resultQuantity = resultQuantity;
        this.ingredients = ingredients;
    }

    public String getResultItemId() {
        return resultItemId;
    }

    public int getResultQuantity() {
        return resultQuantity;
    }

    public Map<String, Integer> getIngredients() {
        return ingredients;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "resultItemId='" + resultItemId + '\'' +
                ", resultQuantity=" + resultQuantity +
                ", ingredients=" + ingredients +
                '}';
    }
}
