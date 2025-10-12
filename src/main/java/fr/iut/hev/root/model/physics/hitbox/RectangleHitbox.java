package fr.iut.hev.root.model.physics.hitbox;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Implementation of a rectangular hitbox.
 * This is useful for most entities in the game.
 */
public class RectangleHitbox implements Hitbox {
    private DoubleProperty xProperty;
    private DoubleProperty yProperty;
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
        this.xProperty = new SimpleDoubleProperty(x);
        this.yProperty = new SimpleDoubleProperty(y);
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
        double thisLeft = getCenterX() - width / 2;
        double thisRight = getCenterX() + width / 2;
        double thisTop = getCenterY() - height / 2;
        double thisBottom = getCenterY() + height / 2;

        double otherLeft = other.getCenterX() - other.width / 2;
        double otherRight = other.getCenterX() + other.width / 2;
        double otherTop = other.getCenterY() - other.height / 2;
        double otherBottom = other.getCenterY() + other.height / 2;

        return thisRight > otherLeft &&
               thisLeft < otherRight &&
               thisBottom > otherTop &&
               thisTop < otherBottom;
    }

    @Override
    public double getCenterX() {
        return xProperty.getValue();
    }

    @Override
    public double getCenterY() {
        return yProperty.getValue();
    }

    public void setPosition(double x, double y) {
        xProperty.unbind(); // Jsp pk mais ca marche donc je laisse
        yProperty.unbind();
        setX(x);
        setY(y);
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

    public void setX(double x) {
        /*System.out.println(x);
        System.out.println(xProperty);*/
        this.xProperty.setValue(x);
    }

    public void setY(double y) {
        this.yProperty.setValue(y);
    }

    public DoubleProperty xProperty() {
        return this.xProperty;
    }

    public DoubleProperty yProperty() {
        return this.yProperty;
    }
}
