package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.hitbox.HitboxManager;

import static fr.iut.hev.root.model.TileMap.format;

public class Weapon extends Item {

    private int damage;
    private HitboxManager hitboxManager;

    public Weapon(ItemsEnum itemsEnum,HitboxManager hitboxManager) {
        super(itemsEnum);
        this.hitboxManager = hitboxManager;
        this.damage = getStats().getItemMainStat();
    }

    public int getDamage() {
        return this.damage;
    }

    @Override
    public boolean isUsed(MouseItemActionInputHandler eventHandler) {
        int x = (int)eventHandler.getX() / format;
        int y = (int)eventHandler.getY() / format;
        Player player = eventHandler.getPlayer();

        // Check if the target position is within the player's reach
        // For weapons, we use the player's default reach which can be configured per weapon type
        if (!player.isWithinReach(x, y)) {
            return false;
        }

        // TODO: Implement weapon attack logic here
        // For now, just return true to indicate the weapon was used successfully
        return true;
    }

    public HitboxManager getHitboxManager() {
        return hitboxManager;
    }
}
