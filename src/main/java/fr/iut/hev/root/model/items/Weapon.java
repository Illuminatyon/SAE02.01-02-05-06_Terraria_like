package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.entities.Entity;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.hitbox.Hitbox;
import fr.iut.hev.root.model.hitbox.HitboxManager;

import java.util.List;

public class Weapon extends Item {

    private int damage;
    private static HitboxManager hitboxManager;

    public Weapon(ItemsEnum itemsEnum) {
        super(itemsEnum);
        this.damage = getStats().getItemMainStat();
    }

    /**
     * Sets the hitbox manager for all weapons.
     * This should be called once during game initialization.
     * 
     * @param manager The hitbox manager to use
     */
    public static void setHitboxManager(HitboxManager manager) {
        hitboxManager = manager;
    }

    @Override
    public boolean isUsed(MouseItemActionInputHandler eventHandler) {
        if (hitboxManager == null) {
            return false;
        }

        Player player = eventHandler.getPlayer();

        // Calculate attack hitbox position based on player's direction and position
        double offsetX = player.getLookDirection() * player.getWidth();
        double offsetY = 0; // Center of the player

        // Create a weapon attack hitbox
        Hitbox attackHitbox = hitboxManager.createWeaponAttackHitbox(
            player,
            offsetX,
            offsetY,
            player.getWidth(), // Width of the attack hitbox
            player.getHeight() // Height of the attack hitbox
        );

        // Check for collisions and apply damage
        List<Entity> hitEntities = hitboxManager.checkAttackCollisions(player, damage);

        // Remove the attack hitbox after the attack
        hitboxManager.removeHitboxes(player);

        // Create a new vulnerable hitbox for the player
        hitboxManager.createDefaultVulnerableHitbox(player);

        // Return true if any entity was hit, false otherwise
        return !hitEntities.isEmpty();
    }

    /**
     * Gets the damage value of this weapon.
     * 
     * @return The damage value
     */
    public int getDamage() {
        return damage;
    }
}
