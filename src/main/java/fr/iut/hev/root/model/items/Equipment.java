package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.enums.ItemsEnum;

public abstract class Equipment extends Item {

    private int durability;

    public Equipment(ItemsEnum itemsEnum) {
        super(itemsEnum);
        this.durability = itemsEnum.getStats().getDurability();
    }

    public abstract boolean isUsed(MouseItemActionInputHandler mouseItemActionInputHandler);
    public void consumeDurability() {this.durability}
}
