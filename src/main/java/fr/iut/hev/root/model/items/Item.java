package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.Recipe;
import fr.iut.hev.root.model.enums.ItemStatsEnum;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.CraftingManager;

import java.util.Optional;

public class Item {

    private ItemsEnum item;

    public Item(ItemsEnum item) {
        this.item = item;
    }

    public ItemsEnum getItemEnum() {
        return this.item;
    }

    public void setItemEnum(ItemsEnum item) {
        this.item = item;
    }

    public boolean isUsed(MouseItemActionInputHandler eventHandler) {
        return false;
    }

    public boolean isCraftable() {
        return CraftingManager.getRecipeFor(item).isPresent();
    }

    public Optional<Recipe> getCraftingRecipe() {
        return CraftingManager.getRecipeFor(item);
    }

    public ItemStatsEnum getStats() {
        return item.getStats();
    }
}
