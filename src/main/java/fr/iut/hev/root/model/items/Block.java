package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.enums.ItemsEnum;

public class Block extends Item {

    public Block(ItemsEnum itemsEnum) {super(itemsEnum);}

    @Override
    public boolean isUsed(MouseItemActionInputHandler eventHandler) {
        return super.isUsed(eventHandler);
    }
}
