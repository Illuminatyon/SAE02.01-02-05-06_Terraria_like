package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.Gravity;
import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.Item;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.PlayerMouvements;

import java.util.HashSet;
import java.util.Set;

public class Player extends Actor {
    private Inventory inventory;
    private final Set<PlayerMouvements> activeActions;

    public Player(int posX, int posY, int width, int height, TileMap tileMap, int moveSpeed, int jumpForce, int reach) {
        super(posX, posY, width, height, tileMap,10, moveSpeed, jumpForce,reach);
        this.inventory = new Inventory();
        this.activeActions = new HashSet<>();
    }

    public void addActiveActions(PlayerMouvements playerMouvements) {
        this.activeActions.add(playerMouvements);
    }

    public void removeActiveActions(PlayerMouvements playerMouvements) {
        this.activeActions.remove(playerMouvements);
    }

    public Set<PlayerMouvements> getActiveActions() {return activeActions;}

    public void update() {
        updatePosition();
        for (Loot loot : Loot.lootOnMapProperty.get()) {
            if (getCollider().intersectsWith(loot.getCollider())) {
                pickUp(loot);
            }
        }
    }

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

        // TMP
        for (Loot loot : Loot.lootOnMapProperty.get()) {
            if (getCollider().intersectsWith(loot.getCollider())) {
                pickUp(loot);
            }
        }
    }

    @Override
    public void updateHorizontalMovement() {
        // Code pas propre a nettoyer
        if (activeActions.contains(PlayerMouvements.MOVE_RIGHT)
                && activeActions.contains(PlayerMouvements.MOVE_LEFT)) {
            super.setVelocityX(0);
        } else if (activeActions.contains(PlayerMouvements.MOVE_RIGHT)) {
            super.setLookDirection(LookDirections.RIGHT);
            if (!super.getCollider().hasCollisionRight()) {
                super.setVelocityX(super.getMoveSpeed());
            } else {
                super.setVelocityX(0);
            }
        } else if (activeActions.contains(PlayerMouvements.MOVE_LEFT)) {
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
        if (activeActions.contains(PlayerMouvements.JUMP) && super.getCollider().hasCollisionBottom(super.getVelocityY() - Gravity.getGravityForce()) && !super.getIsJumping()) {
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

    public void pickUp(Loot loot) {
        this.inventory.add(0, loot.getItem(), loot.getQuantity());
        loot.removeSelf();
    }

    public void diesQuestionMark() {
        if (this.getHealth() == 0)
            this.setIsAliveProperty(false);
    }

    public void updateBreaksBlock(int x,int y, TileMap tileMap) {
        tileMap.tileGetsMined(x,y);
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
