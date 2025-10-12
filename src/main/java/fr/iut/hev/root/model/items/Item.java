package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.items.enums.ItemStatsEnum;
import fr.iut.hev.root.model.items.enums.ItemsEnum;

public class Item {

    private ItemsEnum item;
    private double cooldown;

    public Item(ItemsEnum item) {
        this.item = item;
        this.cooldown = item.getCooldown();
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

    public ItemStatsEnum getStats() {
        return item.getStats();
    }

    public double getCooldown() {return this.cooldown;}
}
