package fr.iut.hev.root.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Actor {
    private IntegerProperty posXProperty;
    private IntegerProperty posYProperty;
    private int moveSpeed;
    private int jumpForce;
    private int velocityX;
    private int velocityY;
    private int velocityMultiplier;

    public Actor(int posXProperty, int posYProperty, int moveSpeed, int jumpForce) {
        this.posXProperty = new SimpleIntegerProperty(posXProperty);
        this.posYProperty = new SimpleIntegerProperty(posYProperty);
        this.moveSpeed = moveSpeed;
        this.jumpForce = jumpForce;
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
}
