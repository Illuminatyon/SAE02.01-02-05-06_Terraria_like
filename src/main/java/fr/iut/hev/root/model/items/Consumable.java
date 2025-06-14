package fr.iut.hev.root.model.items;

import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.ConsumableStats;
import fr.iut.hev.root.model.enums.Items;
import javafx.beans.property.IntegerProperty;

public class Consumable extends Item{

    private ConsumableStats stats;

    public Consumable(Items items,ConsumableStats stats) {
        super(items);
        this.stats = stats;
    }

    public boolean isUsed(Player player, Inventory inventory){
        if (player.getHealth() < 10) {
            if (player.getHealth() + stats.getHealthRestored() >= 10)
                player.setHealth(10);
            else
                player.setHealth(player.getHealth() + stats.getHealthRestored());
            return true;
        }
        else
            return false;
    }
}
