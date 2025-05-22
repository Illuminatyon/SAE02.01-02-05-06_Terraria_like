package fr.iut.hev.root.model;

import fr.iut.hev.root.model.enums.PlayerActions;

import java.util.HashSet;
import java.util.Set;

public class Player extends Actor {

    private final Set<PlayerActions> activeActions;

    public Player(int posX, int posY, int width, int height, TileMap tileMap, int moveSpeed, int jumpForce) {
        super(posX, posY, width, height, tileMap,10, moveSpeed, jumpForce);
        this.activeActions = new HashSet<>();
    }

    public void addActiveActions(PlayerActions playerActions) {
        this.activeActions.add(playerActions);
    }

    public void removeActiveActions(PlayerActions playerActions) {
        this.activeActions.remove(playerActions);
    }

    public Set<PlayerActions> getActiveActions() {return activeActions;}

    @Override
    public void updatePosition() {
        if (!super.getCollider().hasCollisionBottom(super.getVelocityY() + 1) && !super.getIsJumping()) {
            //if (super.getVelocityY() < maxVelocityY)
            super.setVelocityY(super.getVelocityY() + Gravity.getGravityForce());
        } else {
            super.setVelocityY(0);
        }

        updateHorizontalMovement();
        updateVerticalMovement();
        super.posXProperty().set(super.posXProperty().getValue() + super.getVelocityX() * super.getMoveSpeed());
        super.posYProperty().set(super.posYProperty().getValue() + super.getVelocityY());
        diesQuestionMark();
    }

    @Override
    public void updateHorizontalMovement() {
        // Code pas propre a nettoyer
        if (activeActions.contains(PlayerActions.MOVE_RIGHT)
                && activeActions.contains(PlayerActions.MOVE_LEFT)) {
            super.setVelocityX(0);
        } else if (activeActions.contains(PlayerActions.MOVE_RIGHT)) {
            super.setLookDirection(LookDirections.RIGHT);
            if (!super.getCollider().hasCollisionRight()) {
                super.setVelocityX(super.getMoveSpeed());
            } else {
                super.setVelocityX(0);
            }
        } else if (activeActions.contains(PlayerActions.MOVE_LEFT)) {
            super.setLookDirection(LookDirections.LEFT); // IL FAUT JUSTE FIX LE LEFT COLLIDER
            if (!super.getCollider().hasCollisionLeft()) {
                super.setVelocityX(-super.getMoveSpeed());
            } else {
                super.setVelocityX(0);
            }
        } else {
            super.setVelocityX(0);
        }
    }

    @Override
    public void updateVerticalMovement() {
        if (activeActions.contains(PlayerActions.JUMP) && super.getCollider().hasCollisionBottom(super.getVelocityY() - Gravity.getGravityForce()) && !super.getIsJumping()) {
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

    public void diesQuestionMark() {
        if (this.getHalf_heart() == 0)
            this.setIsAlive(false);
    }
}
