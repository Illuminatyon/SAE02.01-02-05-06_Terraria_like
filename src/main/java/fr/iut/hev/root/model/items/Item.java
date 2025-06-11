package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.Recipe;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.CraftingManager;
import fr.iut.hev.root.model.enums.StatEnum;

import java.util.Optional;

public class Item {

    private ItemsEnum item;
    private StatEnum stats;

    public Item(ItemsEnum item) {
        this.item = item;
        this.stats = item.getStats();
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
    public StatEnum getStats() {return this.stats;}
}
