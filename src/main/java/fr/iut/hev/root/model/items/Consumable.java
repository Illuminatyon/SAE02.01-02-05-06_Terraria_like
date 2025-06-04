package fr.iut.hev.root.model.items;

import fr.iut.hev.root.model.enums.ConsumableStats;
import fr.iut.hev.root.model.enums.Items;

public class Consumable extends Item{

    private ConsumableStats stats;

    public Consumable(Items items,ConsumableStats stats) {
        super(items);
        this.stats = stats;
    }

    public void isUsed(){

    }
}
