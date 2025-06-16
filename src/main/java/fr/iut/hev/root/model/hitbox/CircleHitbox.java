package fr.iut.hev.root.model.hitbox;

import fr.iut.hev.root.model.enums.HitboxType;

/**
 * Implementation of a circular hitbox.
 * This is useful for certain types of attacks or interactions.
 */
public class CircleHitbox implements Hitbox {
    private double x;
    private double y;
    private double radius;
    private HitboxType type;
    
    /**
     * Creates a new circular hitbox.
     * 
     * @param x The x-coordinate of the center
     * @param y The y-coordinate of the center
     * @param radius The radius of the circle
     * @param type The type of hitbox
     */
    public CircleHitbox(double x, double y, double radius, HitboxType type) {
        this.x = x;
        this.y = y;
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
     */
    private boolean intersectsCircle(CircleHitbox other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (this.radius + other.radius);
    }
    
    /**
     * Checks if this circle intersects with a rectangle.
     * 
     * @param other The rectangle to check intersection with
     * @return true if the circle and rectangle intersect, false otherwise
     */
    private boolean intersectsRectangle(RectangleHitbox other) {
        // Find the closest point to the circle within the rectangle
        double closestX = Math.max(other.getCenterX() - other.getWidth() / 2, 
                         Math.min(x, other.getCenterX() + other.getWidth() / 2));
        double closestY = Math.max(other.getCenterY() - other.getHeight() / 2, 
                         Math.min(y, other.getCenterY() + other.getHeight() / 2));
        
        // Calculate the distance between the circle's center and this closest point
        double dx = x - closestX;
        double dy = y - closestY;
        double distanceSquared = dx * dx + dy * dy;
        
        // If the distance is less than the circle's radius, an intersection occurs
        return distanceSquared < (radius * radius);
    }
    
    @Override
    public double getCenterX() {
        return x;
    }
    
    @Override
    public double getCenterY() {
        return y;
    }
    
    @Override
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public HitboxType getType() {
        return type;
    }
    
    /**
     * Gets the radius of the circle.
     * 
     * @return The radius
     */
    public double getRadius() {
        return radius;
    }
}