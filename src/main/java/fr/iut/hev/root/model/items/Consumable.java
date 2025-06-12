package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.ItemsEnum;

public class Consumable extends Item{

    public Consumable(ItemsEnum itemsEnum) {
        super(itemsEnum);
    }

    public boolean isUsed(MouseItemActionInputHandler eventHandler){
        Player player = eventHandler.getPlayer();
        System.out.println("is used");
        if (player.getHealth() < 10) {
            if (player.getHealth() + getStats().getItemMainStat() >= 10)
                player.setHealth(10);
            else
                player.setHealth(player.getHealth() + getStats().getItemMainStat());
            player.getInventory().remove(player.getIndexItemInHand(),1);
            System.out.println(player.getHealth());
            return true;
        }
        else {
            System.out.println("false");
            return false;
        }
    }
}
