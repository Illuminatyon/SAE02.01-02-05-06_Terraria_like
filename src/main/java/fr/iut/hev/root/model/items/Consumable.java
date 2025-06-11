package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.ConsumableStatsEnum;
import fr.iut.hev.root.model.enums.ItemsEnum;

public class Consumable extends Item{

    public Consumable(ItemsEnum itemsEnum) {
        super(itemsEnum);
    }

    public boolean isUsed(MouseItemActionInputHandler eventHandler){
        Player player = eventHandler.getPlayer();
        if (player.getHealth() < 10) {
            if (player.getHealth() + getStats().getHealthRestored() >= 10)
                player.setHealth(10);
            else
                player.setHealth(player.getHealth() + stats.getHealthRestored());
            player.getInventory().remove(player.getIndexItemInHand(),1);
            return true;
        }
        else
            return false;
    }
}
