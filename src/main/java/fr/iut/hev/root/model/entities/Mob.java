package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.Gravity;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.enums.ConsumableStats;
import fr.iut.hev.root.model.enums.Items;
import fr.iut.hev.root.model.items.Consumable;
import fr.iut.hev.root.model.items.Item;

import java.sql.SQLOutput;

import static fr.iut.hev.root.model.TileMap.format;

public class Mob extends Actor {
    private long lastDirectionChangeTime = 0;
    private static final long DIRECTION_CHANGE_INTERVAL = 2000;
    private int currentDirection = 0; // -1 for left, 1 for right, 0 for stationary


    public Mob(int posX, int posY, int width, int height, TileMap tileMap, int healthProperty, int moveSpeed, int jumpForce, int reach, ActorEnum actor) {
        super(posX, posY, width, height, tileMap, healthProperty, moveSpeed, jumpForce, reach, actor);

    }

    @Override
    public void diesQuestionMark() {
        if (getHealth()==0){
            setIsAliveProperty(false);
            Item item = new Consumable(Items.RAW_CHICKEN, ConsumableStats.RAW_CHICKEN);
            Loot droppedLoot = new Loot(item, 1, getPosX() + format*getTileMap().getWidth()/2, getPosY() + format*getTileMap().getHeight()/2, 32, 32, getTileMap());
            System.out.println("x = " + getPosX() + " y = " + getPosY());
            System.out.println("x = " + droppedLoot.getPosX() + " y = " + droppedLoot.getPosY());
        }

    }

    @Override
   /* public void updateHorizontalMovement() {
        double changeDirection = Math.random();
        double direction = Math.random();
        if ((super.getLookDirection()==-1)||(( changeDirection < 0.16) && direction < (0.5))) {
            super.setLookDirection(LookDirections.RIGHT);
            if (!super.getCollider().hasCollisionRight()) {
                super.setVelocityX(super.getMoveSpeed());
            } else {
                this.updateVerticalMovement();
            }
        } else if ((super.getLookDirection()==-1)||( changeDirection <0.16 && /*((direction < 0.66) && (direction > 0.5))){
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



    }*/

    public void updateHorizontalMovement() {
        long currentTime = System.currentTimeMillis();


        if (currentTime - lastDirectionChangeTime > DIRECTION_CHANGE_INTERVAL && !(getIsJumping())) {
            double direction = Math.random();
            if (direction < 0.33) {
                currentDirection = -1;
            } else if (direction < 0.66) {
                currentDirection = 1;
            } else {
                currentDirection = 0;
            }


            lastDirectionChangeTime = currentTime;
        }


        if (currentDirection == -1) {
            super.setLookDirection(LookDirections.LEFT);
            if (!super.getCollider().hasCollisionLeft()) {
                super.setVelocityX(-super.getMoveSpeed());
            } else {
                updateVerticalMovement();
            }
        } else if (currentDirection == 1) {
            super.setLookDirection(LookDirections.RIGHT);
            if (!super.getCollider().hasCollisionRight()) {
                super.setVelocityX(super.getMoveSpeed());
            } else {
                updateVerticalMovement();
            }
        } else {
            super.setVelocityX(0);
        }
    }


  /*  public void updatePosition() {
        if (!super.getCollider().hasCollisionBottom(super.getVelocityY() + 1) && !super.getIsJumping()) {
            //if (super.getVelocityY() < maxVelocityY)
            super.setVelocityY(super.getVelocityY() + Gravity.getGravityForce());
        } else {
            super.setVelocityY(0);
        }

        updateHorizontalMovement();
       // updateVerticalMovement();
        super.posXProperty().set(super.posXProperty().getValue() + super.getVelocityX() * super.getMoveSpeed());
        super.posYProperty().set(super.posYProperty().getValue() + super.getVelocityY());
    }*/
  public void updatePosition() {

      if (!super.getCollider().hasCollisionBottom(super.getVelocityY() + 1) && !super.getIsJumping()) {
          super.setVelocityY(super.getVelocityY() + Gravity.getGravityForce());
      } else {
          super.setVelocityY(0);
      }


      updateHorizontalMovement();


      double velocityX = super.getVelocityX() * super.getMoveSpeed();
      double moveStepX = Math.signum(velocityX);
      double remainingX = Math.abs(velocityX);

      while (remainingX > 0) {
          if (moveStepX > 0 && !super.getCollider().hasCollisionRight()) {
              super.posXProperty().set(super.posXProperty().get() + 1);
          } else if (moveStepX < 0 && !super.getCollider().hasCollisionLeft()) {
              super.posXProperty().set(super.posXProperty().get() - 1);
          } else {

              super.setVelocityX(0);
              break;
          }
          remainingX -= 1;
      }


      double velocityY = super.getVelocityY();
      double moveStepY = Math.signum(velocityY);
      double remainingY = Math.abs(velocityY);

      while (remainingY > 0) {
          if (moveStepY > 0 && !super.getCollider().hasCollisionBottom(1)) {
              super.posYProperty().set(super.posYProperty().get() + 1);
          } else if (moveStepY < 0 && !super.getCollider().hasCollisionTop(-1)) {
              super.posYProperty().set(super.posYProperty().get() - 1);
          } else {
              super.setVelocityY(0);
              break;
          }
          remainingY -= 1;
      }
      updateVerticalMovement();
  }

    @Override
    public void updateVerticalMovement() {
        if (super.getCollider().hasCollisionBottom(super.getVelocityY() - Gravity.getGravityForce()) && !super.getIsJumping() && (!super.getCollider().hasCollisionLeft()||!super.getCollider().hasCollisionRight())) {
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
