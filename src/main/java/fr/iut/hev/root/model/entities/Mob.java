package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.Gravity;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.enums.HitboxType; // TODO : Enlever les imports qui ne servent à rien
import fr.iut.hev.root.model.hitbox.Hitbox;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import fr.iut.hev.root.model.hitbox.RectangleHitbox;

public class Mob extends Actor {
    private long lastDirectionChangeTime = 0;
    private int currentDirection = 0; // -1 pour gauche, 1 pour droite, 0 pour stationnaire
    private int jumpCooldown= 1000;
    private long lastJumpTime = 0;

    public Mob(int posX, int posY, int width, int height, TileMap tileMap, int health, int moveSpeed, int jumpForce, int reach, ActorEnum actor, HitboxManager hitboxManager) {
        super(posX, posY, width, height, tileMap, health, moveSpeed, jumpForce, reach, actor, hitboxManager );
    }

    @Override
    public void updatePosition() {
        // Gravité
        if (!super.getCollider().hasCollisionBottom(super.getVelocityY() + 1) && !super.getIsJumping()) {
            super.setVelocityY(super.getVelocityY() + Gravity.getGravityForce());
        } else {
            super.setVelocityY(0);
        }

        // Mouvement horizontal
        updateHorizontalMovement();

        // Déplacement sur l'axe X
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

        // Déplacement sur l'axe Y
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
        // Jump automatique si bloque
    }

    public int Changement(){
        long currentTime = System.currentTimeMillis();
        int DIRECTION_CHANGE_INTERVAL = 2000;
        // Changement aléatoire de direction
        if (currentTime - lastDirectionChangeTime > DIRECTION_CHANGE_INTERVAL && !getIsJumping()) {
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
        return currentDirection;
    }
    @Override
    public void updateHorizontalMovement() {

        currentDirection=Changement();
        // Appliquer la direction actuelle
        if (currentDirection == -1) {
            super.setLookDirection(LookDirections.LEFT);
            if (!super.getCollider().hasCollisionLeft()) {
                super.setVelocityX(-super.getMoveSpeed());
            } else {
                updateVerticalMovement(); // Saut si bloqué
            }
        } else if (currentDirection == 1) {
            super.setLookDirection(LookDirections.RIGHT);
            if (!super.getCollider().hasCollisionRight()) {
                super.setVelocityX(super.getMoveSpeed());
            } else {
                updateVerticalMovement(); // Saut si bloqué
            }
        } else {
            super.setVelocityX(0);
        }
    }



    @Override
    public void updateVerticalMovement() {
        long currentTime = System.currentTimeMillis();

        if (super.getCollider().hasCollisionBottom(super.getVelocityY() - Gravity.getGravityForce()) && !super.getIsJumping() && (!super.getCollider().hasCollisionLeft() || !super.getCollider().hasCollisionRight())) {

            // Vérifiez si le temps de recharge est écoulé
            if (currentTime - lastJumpTime >= jumpCooldown) {
                super.setIsJumping(true);
                super.setJumpingTestDecay(0);
                lastJumpTime = currentTime; // Mettre à jour le dernier temps de saut
            }
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

}
