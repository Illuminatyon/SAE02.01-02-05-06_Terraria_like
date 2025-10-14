package fr.iut.hev.root.model.physics.hitbox;

import fr.iut.hev.root.model.entities.actor.Actor;
import fr.iut.hev.root.model.entities.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager class for hitboxes.
 * This class is responsible for creating, updating, and checking collisions between hitboxes.
 */
public class HitboxManager {
    private static HitboxManager hitboxManager = null;

    private Map<Entity, List<Hitbox>> entityHitboxes;

    /**
     * Creates a new hitbox manager.
     */
    private HitboxManager() {
        this.entityHitboxes = new HashMap<>();
    }

    public static HitboxManager getInstance(){
        if(hitboxManager == null){
            hitboxManager = new HitboxManager();
        }
        return hitboxManager;
    }

    /**
     * Adds a hitbox to an entity.
     *
     * @param entity The entity to add the hitbox to
     * @param hitbox The hitbox to add
     */
    public void addHitbox(Entity entity, Hitbox hitbox) {
        if (!entityHitboxes.containsKey(entity)) {
            entityHitboxes.put(entity, new ArrayList<>());
        }
        entityHitboxes.get(entity).add(hitbox);
    }

    /**
     * Removes all hitboxes from an entity.
     * 
     * @param entity The entity to remove hitboxes from
     */
    public void removeHitboxes(Entity entity) {
        entityHitboxes.remove(entity);
    } // on peut la retirer
    
    /**
     * Updates the positions of all hitboxes for an entity.
     * 
     * @param entity The entity to update hitboxes for
     */ // Commentaire à retirer
    
    /**
     * Checks for collisions between attack hitboxes and vulnerable hitboxes.
     * If a collision is detected, damage is applied to the vulnerable entity.
     * 
     * @param attacker The entity that is attacking
     * @param damage The amount of damage to apply
     * @return A list of entities that were hit
     */
    public List<Entity> checkAttackCollisions(Entity attacker, int damage) {
            List<Entity> hitEntities = new ArrayList<>();
        
        if (!entityHitboxes.containsKey(attacker)) {
            return hitEntities;
        }
        
        // Get all attack hitboxes for the attacker
        List<Hitbox> attackHitboxes = new ArrayList<>();
        for (Hitbox hitbox : entityHitboxes.get(attacker)) {
            if (hitbox.getType() == HitboxType.ATTACK) {
                attackHitboxes.add(hitbox);
            }
        }
        
        // Check for collisions with vulnerable hitboxes of other entities
        for (Map.Entry<Entity, List<Hitbox>> entry : entityHitboxes.entrySet()) {
            Entity target = entry.getKey();
            
            // Skip the attacker
            if (target == attacker) {
                continue;
            }
            
            // Check if any attack hitbox intersects with any vulnerable hitbox
            boolean hit = false;
            for (Hitbox attackHitbox : attackHitboxes) {
                for (Hitbox targetHitbox : entry.getValue()) {
                    if (targetHitbox.getType() == HitboxType.VULNERABLE && attackHitbox.intersects(targetHitbox)) {
                        hit = true;
                        break;
                    }
                }
                if (hit) {
                    break;
                }
            }
            
            // If a hit was detected, apply damage and add to the list
            if (hit && target instanceof Actor) {
                ((Actor) target).receiveDamage(damage);
                hitEntities.add(target);
            }
        }
        
        return hitEntities;
    } // Surement le refactor

    /**
     * Creates a default vulnerable hitbox for an entity based on its dimensions.
     *
     * @param entity The entity to create a hitbox for
     * @return The created hitbox
     */
    public Hitbox createHitbox(Entity entity, HitboxType type) {
        Hitbox hitbox = new RectangleHitbox(
            entity.getPosX(),
            entity.getPosY(),
            entity.getWidth(),
            entity.getHeight(),
                type
        );
        addHitbox(entity, hitbox);
        return hitbox;
    } // On peut le refactor, je me dit qu'on pourrait le mettre directement dans la classe Hitbox ?

    /**
     * Creates an attack hitbox for a weapon.
     *
     * @param attacker The entity that is attacking
     * @param offsetX The x offset from the entity's position
     * @param offsetY The y offset from the entity's position
     * @param width The width of the attack hitbox
     * @param height The height of the attack hitbox
     * @return The created hitbox
     */
    public Hitbox createWeaponAttackHitbox(Entity attacker, double offsetX, double offsetY, double width, double height) {
        Hitbox hitbox = new RectangleHitbox(
            attacker.getPosX() + offsetX,
            attacker.getPosY() + offsetY,
            width,
            height,
            HitboxType.ATTACK
        );
        addHitbox(attacker, hitbox);
        return hitbox;
    } // On peut refactor

    /**
     * Creates a circular attack hitbox.
     *
     * @param attacker The entity that is attacking
     * @param offsetX The x offset from the entity's position
     * @param offsetY The y offset from the entity's position
     * @param radius The radius of the attack hitbox
     * @return The created hitbox
     */
    public Hitbox createCircularAttackHitbox(Entity attacker, double offsetX, double offsetY, double radius) {
        Hitbox hitbox = new CircleHitbox(
            attacker.getPosX() + offsetX,
            attacker.getPosY() + offsetY,
            radius,
            HitboxType.ATTACK
        );
        addHitbox(attacker, hitbox);
        return hitbox;
    } // refactor tout de suite
}