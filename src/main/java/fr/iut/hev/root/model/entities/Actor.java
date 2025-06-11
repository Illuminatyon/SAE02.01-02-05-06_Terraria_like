package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.Gravity;
import fr.iut.hev.root.model.TileMap;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class Actor extends Entity {
    private IntegerProperty healthProperty;
    private int moveSpeed;
    private int jumpForce;
    private boolean isJumping;
    private int jumpingTestDecay;
    private int reach;
    private ActorEnum type;
    protected int damage;

    private IntegerProperty lookDirectionProperty;
    public enum LookDirections {
        RIGHT(1),
        LEFT(-1);

        private int value;

        LookDirections(int value) {
            this.value = value;
        }
    };

    public Actor(int posX, int posY, int width, int height, TileMap tileMap, int healthProperty, int moveSpeed, int jumpForce,int reach, ActorEnum type) {
        super(posX, posY, width, height, tileMap);
        this.healthProperty = new SimpleIntegerProperty(healthProperty);
        this.moveSpeed = moveSpeed;
        this.jumpForce = jumpForce;
        this.lookDirectionProperty = new SimpleIntegerProperty(LookDirections.RIGHT.value);
        this.isJumping = false;
        this.jumpingTestDecay = 0;
        this.reach = reach;
        this.type = type;
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
    public String getName(){return this.type.getName();}

    public int getReach() {return this.reach;}

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

    public int getLookDirection() {return this.lookDirectionProperty.getValue();}

    public void receiveDamage(int damage) {
        if (!(damage > this.getHealth()))
            this.setHealth(getHealth() - damage);
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
