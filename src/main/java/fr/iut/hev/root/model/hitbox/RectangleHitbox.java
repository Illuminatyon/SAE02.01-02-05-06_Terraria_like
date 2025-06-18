package fr.iut.hev.root.model.hitbox;

import fr.iut.hev.root.model.enums.HitboxType;

/**
 * Implementation of a rectangular hitbox.
 * This is useful for most entities in the game.
 */
public class RectangleHitbox implements Hitbox {
    private double x;
    private double y;
    private double width;
    private double height;
    private HitboxType type;

    /**
     * Creates a new rectangular hitbox.
     * 
     * @param x The x-coordinate of the center
     * @param y The y-coordinate of the center
     * @param width The width of the rectangle
     * @param height The height of the rectangle
     * @param type The type of hitbox
     */
    public RectangleHitbox(double x, double y, double width, double height, HitboxType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    @Override
    public boolean intersects(Hitbox other) {
        if (other instanceof RectangleHitbox) {
            return intersectsRectangle((RectangleHitbox) other);
        } else if (other instanceof CircleHitbox) {
            return ((CircleHitbox) other).intersects(this);
        }
        return false;
    }

    /**
     * Checks if this rectangle intersects with another rectangle.
     * 
     * @param other The other rectangle to check intersection with
     * @return true if the rectangles intersect, false otherwise
     */
    private boolean intersectsRectangle(RectangleHitbox other) {
        double thisLeft = x - width / 2;
        double thisRight = x + width / 2;
        double thisTop = y - height / 2;
        double thisBottom = y + height / 2;

        double otherLeft = other.x - other.width / 2;
        double otherRight = other.x + other.width / 2;
        double otherTop = other.y - other.height / 2;
        double otherBottom = other.y + other.height / 2;

        return thisRight > otherLeft &&
               thisLeft < otherRight &&
               thisBottom > otherTop &&
               thisTop < otherBottom;
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
     * Gets the width of the rectangle.
     * 
     * @return The width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Gets the height of the rectangle.
     * 
     * @return The height
     */
    public double getHeight() {
        return height;
    }
}
