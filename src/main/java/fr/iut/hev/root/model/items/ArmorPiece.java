package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.enums.ArmorTypesEnum;
import fr.iut.hev.root.model.enums.ItemsEnum;

public class ArmorPiece extends Item {

    private int absorbedDamage;
    private ArmorTypesEnum armorType;

    public ArmorPiece(ItemsEnum itemsEnum, ArmorTypesEnum armorType) {
        super(itemsEnum);
        this.absorbedDamage = itemsEnum.getStats().getItemMainStat();
        this.armorType = armorType;
    }

    public ArmorTypesEnum getArmorType() {
        return armorType;
    }

    @Override
    public boolean isUsed(MouseItemActionInputHandler eventHandler) {
        return super.isUsed(eventHandler);
    }
}
