package fr.iut.hev.root.model.items;

import fr.iut.hev.root.model.Recipe;
import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.Items;
import fr.iut.hev.root.model.CraftingManager;
import javafx.beans.property.IntegerProperty;
import javafx.scene.input.MouseEvent;

import java.util.Optional;

public class Item {

    private Items item;

    public Item(Items item) {
        this.item = item;
    }

    public Items getItem() {
        return this.item;
    }

    public void setItem(Items item) {
        this.item = item;
    }

    public boolean isUsed(Player player, Inventory inventory) {
        System.out.println("is used");
        return true;
    }

    public boolean isCraftable() {
        return CraftingManager.getRecipeFor(item).isPresent();
    }

    public Optional<Recipe> getCraftingRecipe() {
        return CraftingManager.getRecipeFor(item);
    }
}
