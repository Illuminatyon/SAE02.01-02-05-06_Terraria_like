package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.enums.BlockTypesEnum;
import fr.iut.hev.root.model.enums.ItemsEnum;

public class Tool extends Item {

    private int miningSpeed;
    private BlockTypesEnum efficientBlockAgainst;

    public Tool(ItemsEnum itemsEnum) {
        super(itemsEnum);
        this.miningSpeed = getStats().getMiningSpeed();
        this.efficientBlockAgainst = getStats().getEfficientBlockAgainst();
    }

    @Override
    public boolean isUsed(MouseItemActionInputHandler eventHandler) {
        return super.isUsed(eventHandler);
    }
}
