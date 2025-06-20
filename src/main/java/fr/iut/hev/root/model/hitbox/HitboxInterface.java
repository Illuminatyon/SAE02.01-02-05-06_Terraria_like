/*package fr.iut.hev.root.model.hitbox;

import fr.iut.hev.root.model.enums.HitboxType;
import javafx.beans.property.DoubleProperty;

/**
 * Interface for hitboxes used in combat and interaction detection.
 * Unlike the Collider class which is used for environmental collision detection,
 * hitboxes are used for entity-entity interactions like combat.
 *//*
public interface Hitbox {
    
    /**
     * Checks if this hitbox intersects with another hitbox.
     * 
     * @param other The other hitbox to check intersection with
     * @return true if the hitboxes intersect, false otherwise
     *//*
    boolean intersects(Hitbox other);
    
    /**
     * Gets the x-coordinate of the hitbox's center.
     * 
     * @return The x-coordinate
     *//*
    double getCenterX();
    
    /**
     * Gets the y-coordinate of the hitbox's center.
     * 
     * @return The y-coordinate
     *//*
    double getCenterY();


    DoubleProperty xProperty();
    DoubleProperty yProperty();
    

    
    /**
     * Gets the type of the hitbox.
     * 
     * @return The hitbox type
     *//*
    HitboxType getType();
}*/