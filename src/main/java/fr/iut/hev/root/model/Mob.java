package fr.iut.hev.root.model;

import fr.iut.hev.root.model.enums.ActorEnum;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static fr.iut.hev.root.model.TileMap.format;

public class Mob extends Actor {



    public Mob(int posX, int posY, int width, int height, TileMap tileMap, int healthProperty, int moveSpeed, int jumpForce, int reach, ActorEnum actor) {
        super(posX, posY, width, height, tileMap, healthProperty, moveSpeed, jumpForce, reach, actor);

    }

    @Override
    public void diesQuestionMark() {
        if (getHealth()==0){
            setIsAliveProperty(false);
        }

    }

    @Override
    public void updateHorizontalMovement() {
        double changeDirection = Math.random();
        double direction = Math.random();
        if ((super.getLookDirection()==1)||(( changeDirection < 0.16) && direction < (0.33))) {
            super.setLookDirection(LookDirections.RIGHT);
            if (!super.getCollider().hasCollisionRight()) {
                super.setVelocityX(super.getMoveSpeed());
            } else {
                this.updateVerticalMovement();
            }
        } else if ((super.getLookDirection()==1)||( changeDirection <0.16 && ((direction < 0.66) && (direction > 0.33)))){
            super.setLookDirection(LookDirections.LEFT);
            if (!super.getCollider().hasCollisionLeft()) {
                super.setVelocityX(-super.getMoveSpeed());
            } else {
                updateVerticalMovement();
            }
        }
        else{
            super.setVelocityX(0);
        }



    }

    public void updatePosition() {
        if (!super.getCollider().hasCollisionBottom(super.getVelocityY() + 1) && !super.getIsJumping()) {
            //if (super.getVelocityY() < maxVelocityY)
            super.setVelocityY(super.getVelocityY() + Gravity.getGravityForce());
        } else {
            super.setVelocityY(0);
        }

        updateHorizontalMovement();
        super.posXProperty().set(super.posXProperty().getValue() + super.getVelocityX() * super.getMoveSpeed());
        super.posYProperty().set(super.posYProperty().getValue() + super.getVelocityY());
    }

    @Override
    public void updateVerticalMovement() {
        if (super.getCollider().hasCollisionBottom(super.getVelocityY() - Gravity.getGravityForce()) && !super.getIsJumping()) {
            super.setIsJumping(true);
            super.setJumpingTestDecay(0);
        } else if (super.getIsJumping()) {
            if (super.getJumpingTestDecay() == super.getJumpForce()) {
                super.setVelocityY(0);
                super.setIsJumping(false);
            } else if (!super.getCollider().hasCollisionTop(super.getVelocityY() + 1)) {
                super.setVelocityY(-super.getJumpForce() + super.getJumpingTestDecay());
                super.setJumpingTestDecay(super.getJumpingTestDecay() + 1);
            } else {
                super.setIsJumping(false);
            }
        }
    }

    public void jump() {

    }


}
