package fr.iut.hev.root.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Actor {
    private DoubleProperty posXProperty;
    private DoubleProperty posYProperty;
    private double moveSpeed;
    private double jumpForce;
    private double velocityX;
    private double velocityY;
    private double velocityMultiplier;

    public Actor(double posXProperty, double posYProperty, double moveSpeed, double jumpForce) {
        this.posXProperty = new SimpleDoubleProperty(posXProperty);
        this.posYProperty = new SimpleDoubleProperty(posYProperty);
        this.moveSpeed = moveSpeed;
        this.jumpForce = jumpForce;
    }

    public void updateMovements() {
        velocityY += Gravity.getGravityForce();
    }

    public final double getPosX() {
        return this.posXProperty.getValue();
    }

    public final void setPosX(double newPosX) {
        posXProperty.setValue(newPosX);
    }

    public final DoubleProperty posXProperty() {
        return this.posXProperty;
    }

    public final double getPosY() {
        return this.posYProperty.getValue();
    }

    public final void setPosY(double newPosY) {
        this.posYProperty.setValue(newPosY);
    }

    public final DoubleProperty posYProperty() {
        return this.posYProperty;
    }

    public double getMoveSpeed() {
        return this.moveSpeed;
    }

    public double getJumpForce() {
        return this.jumpForce;
    }

    public double getVelocityX() {
        return this.velocityX;
    }

    public void setVelocityX(double velocity) {
        this.velocityX = velocity;
    }

    public double getVelocityY() {
        return this.velocityY;
    }

    public void setVelocityY(double velocity) {
        this.velocityY = velocity;
    }
}
