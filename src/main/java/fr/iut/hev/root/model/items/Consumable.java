package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.ItemsEnum;

public class Consumable extends Item{

    private int restoredHealth;
    private int cooldown;

    public Consumable(ItemsEnum itemsEnum) {
        super(itemsEnum);
        this.restoredHealth = itemsEnum.getStats().getItemMainStat();
        this.cooldown = itemsEnum.getStats().getCoolDownStat();
    }

    @Override
    public boolean isUsed(MouseItemActionInputHandler eventHandler){
        Player player = eventHandler.getPlayer();
        if (player.getHealth() < 10) {
            if (player.getHealth() + getRestoredHealth() >= 10)
                player.setHealth(10);
            else
                player.setHealth(player.getHealth() + getRestoredHealth());
            player.getInventory().remove(player.getIndexItemInHand(),1);
            return true;
        }
        else {
            return false;
        }
    }

    public int getRestoredHealth() {return this.restoredHealth;}
    public int getCooldown() {return this.cooldown;}
}
