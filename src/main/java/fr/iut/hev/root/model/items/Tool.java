package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.enums.ItemsEnum;

public class Tool extends Item {

    public Tool(ItemsEnum itemsEnum) {super(itemsEnum);}

    @Override
    public boolean isUsed(MouseItemActionInputHandler eventHandler) {
        return super.isUsed(eventHandler);
    }
}
