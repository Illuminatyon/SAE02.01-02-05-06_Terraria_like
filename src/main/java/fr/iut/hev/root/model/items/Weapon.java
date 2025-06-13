package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.enums.ItemsEnum;

public class Weapon extends Item {

    private int damage;

    public Weapon(ItemsEnum itemsEnum) {
        super(itemsEnum);
        this.damage = getStats().getItemMainStat();
    }

    @Override
    public boolean isUsed(MouseItemActionInputHandler eventHandler) {
        return super.isUsed(eventHandler);
    }
}
