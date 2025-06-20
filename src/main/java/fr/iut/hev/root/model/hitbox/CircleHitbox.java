/*package fr.iut.hev.root.model.hitbox;

import fr.iut.hev.root.model.enums.HitboxType;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Implementation of a circular hitbox.
 * This is useful for certain types of attacks or interactions.
 *//*
public class CircleHitbox implements Hitbox {
    private DoubleProperty xProperty;
    private DoubleProperty yProperty;
    private double radius;
    private HitboxType type;
    
    /**
     * Creates a new circular hitbox.
     * 
     * @param x The x-coordinate of the center
     * @param y The y-coordinate of the center
     * @param radius The radius of the circle
     * @param type The type of hitbox
     *//*
    public CircleHitbox(double x, double y, double radius, HitboxType type) {
        this.xProperty = new SimpleDoubleProperty(x);
        this.yProperty = new SimpleDoubleProperty(y);
        this.radius = radius;
        this.type = type;
    }
    
    @Override
    public boolean intersects(Hitbox other) {
        if (other instanceof CircleHitbox) {
            return intersectsCircle((CircleHitbox) other);
        } else if (other instanceof RectangleHitbox) {
            return intersectsRectangle((RectangleHitbox) other);
        }
        return false;
    }
    
    /**
     * Checks if this circle intersects with another circle.
     * 
     * @param other The other circle to check intersection with
     * @return true if the circles intersect, false otherwise
     *//*
    private boolean intersectsCircle(CircleHitbox other) {
        double dx = getCenterX() - other.getCenterX();
        double dy = getCenterY() - other.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (this.radius + other.radius);
    }

    /**
     * Checks if this circle intersects with a rectangle.
     * 
     * @param other The rectangle to check intersection with
     * @return true if the circle and rectangle intersect, false otherwise
     *//*
    private boolean intersectsRectangle(RectangleHitbox other) {
        // Find the closest point to the circle within the rectangle
        double closestX = Math.max(other.getCenterX() - other.getWidth() / 2, 
                         Math.min(getCenterX(), other.getCenterX() + other.getWidth() / 2));
        double closestY = Math.max(other.getCenterY() - other.getHeight() / 2, 
                         Math.min(getCenterY(), other.getCenterY() + other.getHeight() / 2));
        
        // Calculate the distance between the circle's center and this closest point
        double dx = getCenterX() - closestX;
        double dy = getCenterY() - closestY;
        double distanceSquared = dx * dx + dy * dy;
        
        // If the distance is less than the circle's radius, an intersection occurs
        return distanceSquared < (radius * radius);
    }
    
    @Override
    public HitboxType getType() {
        return type;
    }
    
    /**
     * Gets the radius of the circle.
     * 
     * @return The radius
     *//*
    public double getRadius() {
        return radius;
    }

    public double getCenterX() {return this.xProperty.getValue();}
    public double getCenterY() {return this.yProperty.getValue();}

    public DoubleProperty xProperty() {return this.xProperty;}
    public DoubleProperty yProperty() {return this.yProperty;}
}*/