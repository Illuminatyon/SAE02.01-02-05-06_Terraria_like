package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.enums.ItemsEnum;

public class Weapon extends Equipment {

    private int damage;

    public Weapon(ItemsEnum itemsEnum) {
        super(itemsEnum);
        this.damage = getStats().getItemMainStat();
    }

    @Override
    public boolean isUsed(MouseItemActionInputHandler eventHandler) {
        return false;
    }
}
