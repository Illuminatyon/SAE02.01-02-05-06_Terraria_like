package fr.iut.hev.root.model.items;

import fr.iut.hev.root.model.enums.Items;

import java.util.Optional;

public abstract class Item {

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

    public abstract void isUsed();

    /*public boolean isCraftable() {
        return CraftingManager.getRecipeFor(item).isPresent();
    }

    public Optional<Recipe> getCraftingRecipe() {
        return CraftingManager.getRecipeFor(item);
    }*/
}
