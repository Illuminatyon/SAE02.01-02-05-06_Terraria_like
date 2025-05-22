package fr.iut.hev.root.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class Actor extends Entity {
    private boolean isAlive;
    private IntegerProperty healthProperty;
    private int moveSpeed;
    private int jumpForce;
    private boolean isJumping;
    private int jumpingTestDecay;

    private IntegerProperty lookDirectionProperty;
    public enum LookDirections {
        RIGHT(1),
        LEFT(-1);

        private int value;

        LookDirections(int value) {
            this.value = value;
        }
    };

    public Actor(int posX, int posY, int width, int height, TileMap tileMap, int healthProperty, int moveSpeed, int jumpForce) {
        super(posX, posY, width, height, tileMap);
        this.isAlive = true;
        this.healthProperty = new SimpleIntegerProperty(healthProperty);
        this.moveSpeed = moveSpeed;
        this.jumpForce = jumpForce;
        this.lookDirectionProperty = new SimpleIntegerProperty(LookDirections.RIGHT.value);
        this.isJumping = false;
        this.jumpingTestDecay = 0;
    }

    @Override
    public void updatePosition() {
        applyGravity();
        updateHorizontalMovement();
        updateVerticalMovement();
        super.posXProperty().set(super.posXProperty().getValue() + super.getVelocityX() * moveSpeed);
        super.posYProperty().set(super.posYProperty().getValue() + super.getVelocityY());
    }

    @Override
    public void applyGravity() {
        if (!super.getCollider().hasCollisionBottom(super.getVelocityY() + 1) && !isJumping) {
            //if (super.getVelocityY() < maxVelocityY)
            super.setVelocityY(super.getVelocityY() + Gravity.getGravityForce());
        } else {
            super.setVelocityY(0);
        }
    }

    public void updateHorizontalMovement() {

    }

    public void updateVerticalMovement() {
        
    }

    public int getMoveSpeed() {
        return this.moveSpeed;
    }

    public int getJumpForce() {
        return this.jumpForce;
    }

    public boolean getIsJumping() {
        return this.isJumping;
    }

    public void setIsJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    public int getJumpingTestDecay() {
        return this.jumpingTestDecay;
    }

    public void setJumpingTestDecay(int newValue) {
        this.jumpingTestDecay = newValue;
    }

    public final int getHealth() {return this.healthProperty.getValue();}

    public final void setHealth(int halfHeart) {this.healthProperty.setValue(halfHeart);}

    public final IntegerProperty healthProperty() {return this.healthProperty;}

    public void receiveDamage(int damage) {
        if (!(damage > this.getHealth()))
            this.setHealth(getHealth() - damage);
    }

    public boolean getIsAlive() {return this.isAlive;}

    public void setIsAlive(boolean isAlive) {this.isAlive = isAlive;}

    /*public LookDirections getLookDirection() { // TODO: fix
        return this.lookDirectionProperty;
    }*/

    public void setLookDirection(LookDirections newLookDirection) {
        this.lookDirectionProperty.setValue(newLookDirection.value);
    }

    public IntegerProperty lookDirectionProperty() {
        return this.lookDirectionProperty;
    }

    public abstract void diesQuestionMark();
}
