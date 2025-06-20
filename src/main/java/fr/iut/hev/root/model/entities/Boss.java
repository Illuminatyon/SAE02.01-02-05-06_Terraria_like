package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import javafx.scene.layout.Pane;

import java.util.List;

/**
 * Boss class that extends AggressiveMob
 * This class represents a boss enemy in the game
 */
public class Boss extends AggressiveMob {
    
    /**
     * Constructor for the Boss class
     * @param posX initial X position
     * @param posY initial Y position
     * @param width width of the boss
     * @param height height of the boss
     * @param tileMap reference to the game's tile map
     * @param health initial health of the boss
     * @param moveSpeed movement speed of the boss
     * @param jumpForce jump force of the boss
     * @param reach attack reach of the boss
     * @param type the actor enum type (should be ActorEnum.BOSS)
     * @param player the player target
     * @param aggroDistance distance at which the boss becomes aggressive
     * @param attackCooldown cooldown between attacks
     * @param aliveActors list of all alive actors in the game
     * @param globalPane the pane containing all entities
     * @param damage damage dealt by the boss
     * @param hitboxManager the hitbox manager for collision detection
     */
    public Boss(
            int posX, int posY, int width, int height,
            TileMap tileMap, int health, int moveSpeed, int jumpForce, int reach,
            ActorEnum type, Player player,
            int aggroDistance, int attackCooldown,
            List<Actor> aliveActors, Pane globalPane,
            int damage,
            HitboxManager hitboxManager
    ) {
        super(posX, posY, width, height, tileMap, health, moveSpeed, jumpForce, reach, 
              type, player, aggroDistance, attackCooldown, aliveActors, globalPane, 
              damage, hitboxManager);
    }
    
    // Boss-specific methods can be added here if needed
    // For now, we're just extending AggressiveMob without adding any new functionality
}