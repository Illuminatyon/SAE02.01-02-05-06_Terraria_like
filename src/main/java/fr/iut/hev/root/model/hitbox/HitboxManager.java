package fr.iut.hev.root.model.hitbox;

import fr.iut.hev.root.model.entities.Actor;
import fr.iut.hev.root.model.entities.Entity;
import fr.iut.hev.root.model.entities.Interactive;
import fr.iut.hev.root.model.enums.HitboxType;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager class for hitboxes.
 * This class is responsible for creating, updating, and checking collisions between hitboxes.
 */
public class HitboxManager {
    private Map<Entity, List<Hitbox>> entityHitboxes;
    private HashMap<Interactive,Hitbox> interactiveHitboxes;

    /**
     * Creates a new hitbox manager.
     */
    public HitboxManager() {
        this.entityHitboxes = new HashMap<>();
        this.interactiveHitboxes = new HashMap<>();
    }

    /**
     * Adds a hitbox to an entity.
     * 
     * @param entity The entity to add the hitbox to
     * @param hitbox The hitbox to add
     */
    public void addHitbox(Entity entity, Hitbox hitbox) {
        if (!(hitbox.getType().equals(HitboxType.INTERACTION))) {
        if (!entityHitboxes.containsKey(entity)) {
            entityHitboxes.put(entity, new ArrayList<>());
        }
        entityHitboxes.get(entity).add(hitbox);
        }
        else {
            interactiveHitboxes.put((Interactive) entity,hitbox);
        }
    }

    /**
     * Removes all hitboxes from an entity.
     * 
     * @param entity The entity to remove hitboxes from
     */
    public void removeHitboxes(Entity entity) {
        entityHitboxes.remove(entity);
    }

    /**
     * Updates the positions of all hitboxes for an entity.
     * 
     * @param entity The entity to update hitboxes for
     */
    /*public void updateHitboxPositions(Entity entity) {

        if (entityHitboxes.containsKey(entity)) {
            for (Hitbox hitbox : entityHitboxes.get(entity)) {
                hitbox.setPosition(entity.getPosX(), entity.getPosY());
            }
        }
    }*/

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
    }

    public ArrayList<Interactive> checkInteractiveCollision(Entity entity) {
        Hitbox entityHitbox = getEntityHitbox(entity,HitboxType.INTERACTION);
        ArrayList<Interactive> entities = new ArrayList<>();

        for (Map.Entry<Interactive,Hitbox> hitbox : getInteractiveHitboxes().entrySet()) {
            if (hitbox.getValue() != entityHitbox && entityHitbox.intersects(hitbox.getValue())) {
                entities.add(hitbox.getKey());
            }
        }
        return entities;
    }

    public ArrayList<Interactive> checkInteractiveCollision(Hitbox playerHitbox) {
        if (playerHitbox.getType().equals(HitboxType.INTERACTION)) {
            ArrayList<Interactive> interactives = new ArrayList<>();

            for (Map.Entry<Interactive, Hitbox> interactiveHitbox : getInteractiveHitboxes().entrySet()) {
                if (interactiveHitbox.getValue() != playerHitbox && playerHitbox.intersects(interactiveHitbox.getValue())) {
                    interactives.add(interactiveHitbox.getKey());
                }
            }
            return interactives;
        }
        return null;
    }

    /**
     * Creates a default vulnerable hitbox for an entity based on its dimensions.
     * 
     * @param entity The entity to create a hitbox for
     * @return The created hitbox
     */
    public Hitbox createHitbox(Entity entity, HitboxType type) {
        // Calculate the center of the entity
        Hitbox hitbox = new Hitbox(
            entity.getPosX() + entity.getWidth() / 2,
            entity.getPosY() + entity.getHeight() / 2,
            entity.getWidth(),
            entity.getHeight(),
                type
        );
        // Bind to entity position plus half width/height to keep hitbox centered
        hitbox.xProperty().bind(entity.posXProperty().add(entity.getWidth() / 2));
        hitbox.yProperty().bind(entity.posYProperty().add(entity.getHeight() / 2));
        addHitbox(entity, hitbox);
        return hitbox;
    }

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
        // Create attack hitbox with offset from the center of the entity
        Hitbox hitbox = new Hitbox(
            attacker.getPosX() + attacker.getWidth() / 2 + offsetX,
            attacker.getPosY() + attacker.getHeight() / 2 + offsetY,
            width,
            height,
            HitboxType.ATTACK
        );
        addHitbox(attacker, hitbox);
        return hitbox;
    }

    public Hitbox getEntityHitbox(Entity entity,HitboxType hitboxType) {
        for (Hitbox hitbox : entityHitboxes.get(entity)) {
            if (hitbox.getType().equals(hitboxType))
                return hitbox;
        }
        return null;
    }

    public HashMap<Interactive,Hitbox> getInteractiveHitboxes() {
        return interactiveHitboxes;
    }
}
