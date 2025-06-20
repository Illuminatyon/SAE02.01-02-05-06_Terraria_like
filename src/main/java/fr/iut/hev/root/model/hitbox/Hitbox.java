package fr.iut.hev.root.model.hitbox;

import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.HitboxType;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Implementation of a rectangular hitbox.
 * This is useful for most entities in the game.
 */
public class Hitbox {
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
    public Hitbox(double x, double y, double width, double height, HitboxType type) {
        this.xProperty = new SimpleDoubleProperty(x);
        this.yProperty = new SimpleDoubleProperty(y);
        this.width = width;
        this.height = height;
        this.type = type;
    }

    /*public boolean intersects(Hitbox other) {
        if (other instanceof RectangleHitbox) {
            return intersectsRectangle((RectangleHitbox) other);
        } else if (other instanceof CircleHitbox) {
            return ((CircleHitbox) other).intersects(this);
        }
        return false;
    }*/

    /**
     * Checks if this rectangle intersects with another rectangle.
     *
     * @param other The other rectangle to check intersection with
     * @return true if the rectangles intersect, false otherwise
     */
    public boolean intersects(Hitbox other, Player player) {
        double thisLeft = getCenterX() - width / 2;
        double thisRight = getCenterX() + width / 2;
        double thisTop = getCenterY() - height / 2;
        double thisBottom = getCenterY() + height / 2;

        double otherLeft = other.getCenterX() - other.width / 2;
        double otherRight = other.getCenterX() + other.width / 2;
        double otherTop = other.getCenterY() - other.height / 2;
        double otherBottom = other.getCenterY() + other.height / 2;

        // Debug information about hitbox boundaries
        System.out.println("[DEBUG] Hitbox intersection calculation:");
        System.out.println("[DEBUG] This hitbox boundaries: left=" + thisLeft + ", right=" + thisRight + 
                          ", top=" + thisTop + ", bottom=" + thisBottom);
        System.out.println("[DEBUG] Other hitbox boundaries: left=" + otherLeft + ", right=" + otherRight + 
                          ", top=" + otherTop + ", bottom=" + otherBottom);

        // Check each condition separately for debugging
        boolean rightOverlapsLeft = thisRight > otherLeft;
        boolean leftOverlapsRight = thisLeft < otherRight;
        boolean bottomOverlapsTop = thisBottom > otherTop;
        boolean topOverlapsBottom = thisTop < otherBottom;

        System.out.println("[DEBUG] Right overlaps left: " + rightOverlapsLeft);
        System.out.println("[DEBUG] Left overlaps right: " + leftOverlapsRight);
        System.out.println("[DEBUG] Bottom overlaps top: " + bottomOverlapsTop);
        System.out.println("[DEBUG] Top overlaps bottom: " + topOverlapsBottom);

        boolean intersects = rightOverlapsLeft && leftOverlapsRight && bottomOverlapsTop && topOverlapsBottom;
        System.out.println("[DEBUG] Hitboxes intersect: " + intersects);

        return intersects;
    }

    public double getCenterX() {
        return xProperty.getValue();
    }

    public double getCenterY() {
        return yProperty.getValue();
    }

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
    public DoubleProperty xProperty() {return this.xProperty;}
    public DoubleProperty yProperty() {return this.yProperty;}
}
