package fr.iut.hev.root.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Actor {
    private TileMap tileMap;
    private IntegerProperty posXProperty;
    private IntegerProperty posYProperty;
    private int width;
    private int height;
    private int moveSpeed;
    private int jumpForce;
    private int velocityX;
    private int velocityY;
    private int velocityMultiplier;
    private Collider collider;
    public enum LookDirections {
        RIGHT(1),
        LEFT(-1);

        private int value;

        LookDirections(int value) {
            this.value = value;
        }
    };
    private IntegerProperty lookDirectionProperty;

    public Actor(TileMap tileMap, int posX, int posY, int width, int height, int moveSpeed, int jumpForce) {
        this.tileMap = tileMap;
        this.posXProperty = new SimpleIntegerProperty(posX);
        this.posYProperty = new SimpleIntegerProperty(posY);
        this.width = width;
        this.height = height;
        this.moveSpeed = moveSpeed;
        this.jumpForce = jumpForce;
        this.collider = new Collider(this.tileMap, this);
        this.lookDirectionProperty = new SimpleIntegerProperty(LookDirections.RIGHT.value);
    }

    public void updateMovements() {
        velocityY += Gravity.getGravityForce();
    }

    public final int getPosX() {
        return this.posXProperty.getValue();
    }

    public final void setPosX(double newPosX) {
        posXProperty.setValue(newPosX);
    }

    public final IntegerProperty posXProperty() {
        return this.posXProperty;
    }

    public final int getPosY() {
        return this.posYProperty.getValue();
    }

    public final void setPosY(double newPosY) {
        this.posYProperty.setValue(newPosY);
    }

    public final IntegerProperty posYProperty() {
        return this.posYProperty;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getMoveSpeed() {
        return this.moveSpeed;
    }

    public int getJumpForce() {
        return this.jumpForce;
    }

    public int getVelocityX() {
        return this.velocityX;
    }

    public void setVelocityX(int velocity) {
        this.velocityX = velocity;
    }

    public int getVelocityY() {
        return this.velocityY;
    }

    public void setVelocityY(int velocity) {
        this.velocityY = velocity;
    }

    public Collider getCollider() {
        return this.collider;
    }

    /*public LookDirections getLookDirection() { // TODO: fix
        return this.lookDirectionProperty;
    }*/

    public void setLookDirection(LookDirections newLookDirection) {
        this.lookDirectionProperty.setValue(newLookDirection.value);
    }

    public IntegerProperty lookDirectionProperty() {
        return this.lookDirectionProperty;
    }
}
