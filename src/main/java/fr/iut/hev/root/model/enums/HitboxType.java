package fr.iut.hev.root.model.enums;

/**
 * Enum defining different types of hitboxes.
 * This allows entities to have different hitboxes for different purposes.
 */
public enum HitboxType {
    /**
     * Hitbox used for attacks. When this hitbox intersects with a VULNERABLE hitbox,
     * damage can be applied.
     */
    ATTACK,
    
    /**
     * Hitbox representing the vulnerable area of an entity. When this hitbox is hit
     * by an ATTACK hitbox, the entity takes damage.
     */
    VULNERABLE,
    
    /**
     * Hitbox used for interaction with objects or entities.
     * This is used for non-combat interactions like picking up items.
     */
    INTERACTION
}