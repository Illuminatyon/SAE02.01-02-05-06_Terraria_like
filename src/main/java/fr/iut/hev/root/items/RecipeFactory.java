package fr.iut.hev.root.items;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RecipeFactory {
    public static Recipe fromJson(JSONObject json) {
        String result = json.getString("result");
        int resultQuantity = json.optInt("resultQuantity", 1); // par défaut 1 si non présent

        JSONObject ingredientsJson = json.getJSONObject("ingredients");
        Map<String, Integer> ingredients = new HashMap<>();
        for (String key : ingredientsJson.keySet()) {
            ingredients.put(key, ingredientsJson.getInt(key));
        }

        return new Recipe(result, resultQuantity, ingredients);
    }
}
