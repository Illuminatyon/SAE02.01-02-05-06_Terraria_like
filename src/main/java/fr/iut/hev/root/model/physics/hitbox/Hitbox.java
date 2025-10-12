package fr.iut.hev.root.model.physics.hitbox;

import javafx.beans.property.DoubleProperty;

/**
 * Interface for hitboxes used in combat and interaction detection.
 * Unlike the Collider class which is used for environmental collision detection,
 * hitboxes are used for entity-entity interactions like combat.
 */
public interface Hitbox {


    /*
    * Pour le refactoring, ce qu'on peut faire :
    * Centraliser la création ainsi que l'attachement des hitbox pour éviter les duplications
    * On peut utiliser un coputeIfAbstent pour gérer la map entity -> hitbox
    * On pourrait aussi exposer de petits utilitaires getHitboxes(entity) et getHitboxsesOfType(entity, type)
    * On pourrait nettoyer checkAttackCollisions
    * Et donne des méthodes génériques createOffsetRectHitbox() et createOffsetCircleHitbox()
    * pour remplacer createWeaponAttackHitbox() et createCircleAttackHitbox()
    *
    * 1) Éviter instanceof : ajouter intersectsAvecCercle(...) et intersectsAvecRectangle(...)
    * 2) AABB rapide : ajouter aabb() et filtrer avec aabb.overlaps(...) avant un vrai test
    * 3) API plus propre : retirer xProperty()/yProperty() de l’interface; garder getCenterX/Y()
    * 4) Création unifiée : createOffsetRectHitbox(...) et createOffsetCircleHitbox(...) dans le manager
    * 5) Utilitaires manager : computeIfAbsent(...) + getHitboxesOfType(entity, type)
    * 6) Petites perfs : comparer distance² (pas de Math.sqrt) pour les cercles
    * */
    
    /**
     * Checks if this hitbox intersects with another hitbox.
     * 
     * @param other The other hitbox to check intersection with
     * @return true if the hitboxes intersect, false otherwise
     */
    boolean intersects(Hitbox other);
    
    /**
     * Gets the x-coordinate of the hitbox's center.
     * 
     * @return The x-coordinate
     */
    double getCenterX();
    
    /**
     * Gets the y-coordinate of the hitbox's center.
     * 
     * @return The y-coordinate
     */
    double getCenterY();


    DoubleProperty xProperty();
    DoubleProperty yProperty();

    /**
     * Gets the type of the hitbox.
     * 
     * @return The hitbox type
     */
    HitboxType getType();
}