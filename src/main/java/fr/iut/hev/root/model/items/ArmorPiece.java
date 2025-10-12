package fr.iut.hev.root.model.items; // TODO : supprimer

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.items.enums.ItemsEnum;

public class ArmorPiece extends Item {

    private int absorbedDamage;

    public ArmorPiece(ItemsEnum itemsEnum) {
        super(itemsEnum);
        this.absorbedDamage = itemsEnum.getStats().getItemMainStat();

    }

    @Override
    public boolean isUsed(MouseItemActionInputHandler eventHandler) {
        return super.isUsed(eventHandler);
    }
}
