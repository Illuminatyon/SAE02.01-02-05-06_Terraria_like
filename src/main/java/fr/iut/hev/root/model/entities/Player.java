package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.Gravity;
import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.PlayerMouvementsEnum;
import fr.iut.hev.root.model.items.Item;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.HashSet;
import java.util.Set;

public class Player extends Actor {
    private Inventory inventory;
    private final Set<PlayerMouvementsEnum> playerMouvementEnums;
    private ObjectProperty<Item> itemInHandProperty;
    private IntegerProperty quantityOfItemInHandProperty;
    private IntegerProperty indexItemInHand;

    public Player(int posX, int posY, int width, int height, TileMap tileMap, int moveSpeed, int jumpForce, int reach, ActorEnum actor) {
        super(posX, posY, width, height, tileMap,10, moveSpeed, jumpForce,reach, actor);
        this.inventory = new Inventory();
        this.playerMouvementEnums = new HashSet<>();
        this.indexItemInHand = new SimpleIntegerProperty(0);
        this.itemInHandProperty = new SimpleObjectProperty<>(inventory.getInventorySlot(0).getItem());
        this.quantityOfItemInHandProperty = new SimpleIntegerProperty(inventory.getInventorySlot(0).getQuantity());
    }

    public void addPlayerMouvements(PlayerMouvementsEnum playerMouvementsEnum) {
        this.playerMouvementEnums.add(playerMouvementsEnum);
    }

    public void removePlayerMouvements(PlayerMouvementsEnum playerMouvementsEnum) {
        this.playerMouvementEnums.remove(playerMouvementsEnum);
    }

    public Set<PlayerMouvementsEnum> getPlayerMouvements() {return playerMouvementEnums;}

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
        if (playerMouvementEnums.contains(PlayerMouvementsEnum.MOVE_RIGHT)
                && playerMouvementEnums.contains(PlayerMouvementsEnum.MOVE_LEFT)) {
            super.setVelocityX(0);
        } else if (playerMouvementEnums.contains(PlayerMouvementsEnum.MOVE_RIGHT)) {
            super.setLookDirection(LookDirections.RIGHT);
            if (!super.getCollider().hasCollisionRight()) {
                super.setVelocityX(super.getMoveSpeed());
            } else {
                super.setVelocityX(0);
            }
        } else if (playerMouvementEnums.contains(PlayerMouvementsEnum.MOVE_LEFT)) {
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
        if (playerMouvementEnums.contains(PlayerMouvementsEnum.JUMP) && super.getCollider().hasCollisionBottom(super.getVelocityY() - Gravity.getGravityForce()) && !super.getIsJumping()) {
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
        this.inventory.add(loot.getItem(), loot.getQuantity());
        loot.removeSelf();
    }

    public void updateBreaksBlock(int x,int y, TileMap tileMap) {
        tileMap.tileGetsMined(x,y);
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public boolean usesItemInHand(MouseItemActionInputHandler eventHandler) {
        System.out.println(getItemInHand());
        System.out.println(getQuantityOfItemInHand());
        if (getQuantityOfItemInHand() > 0)
            return getItemInHand().isUsed(eventHandler);
        else
            return false;
    }

    public void consumeOneItem() {
        this.quantityOfItemInHandProperty.setValue(quantityOfItemInHandProperty.getValue() - 1);
    }

    public Item getItemInHand() {return this.itemInHandProperty.getValue();}
    public void setItemInHandProperty(Item itemInHandProperty) {this.itemInHandProperty.setValue(itemInHandProperty);}
    public ObjectProperty<Item> itemInHandProperty() {return this.itemInHandProperty;}
    public int getQuantityOfItemInHand() {return this.quantityOfItemInHandProperty.getValue();}
    public void setQuantityOfItemInHand(int quantity) {this.quantityOfItemInHandProperty.setValue(quantity);}
    public IntegerProperty quantityOfItemInHandProperty() {return this.quantityOfItemInHandProperty;}
    public int getIndexItemInHand() {return this.indexItemInHand.getValue();}
    public IntegerProperty indexItemInHandProperty() {return this.indexItemInHand;}
}
