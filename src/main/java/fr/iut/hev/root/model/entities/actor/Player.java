package fr.iut.hev.root.model.entities.actor;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.craft.CraftingManager;
import fr.iut.hev.root.model.entities.Loot;
import fr.iut.hev.root.model.physics.Gravity;
import fr.iut.hev.root.model.inventory.Inventory;
import fr.iut.hev.root.model.land.TileMap;
import fr.iut.hev.root.model.physics.hitbox.HitboxType;
import fr.iut.hev.root.model.physics.hitbox.HitboxManager;
import fr.iut.hev.root.model.items.Item;
import fr.iut.hev.root.model.items.ItemFactory;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.HashSet;
import java.util.Set;

public class Player extends Actor {

    private static Player player = null; // Singleton

    private Inventory inventory;
    private Set<PlayerMouvementsEnum> playerMouvementEnums;
    private ObjectProperty<Item> itemInHandProperty; //TODO: (Lino) repenser le système de hotbar et d'item sélectionné ce suppot du diable
    private IntegerProperty quantityOfItemInHandProperty;
    private CraftingManager craftingManager;

    // Constructeur privé
    private Player() {
        super(0, 0, 0, 0, null, 0, 0, 0, 0, null);
        this.inventory = null;
        this.craftingManager = null;
        this.playerMouvementEnums = null;
        this.itemInHandProperty = null;
        this.quantityOfItemInHandProperty = null;
    }

    // Méthode d’accès Singleton
    public static Player getInstance() {
        if (player == null) {
            player = new Player();
        }
        return player;
    }

    public void initPlayer(int posX, int posY, int width, int height, TileMap tileMap, int moveSpeed, int jumpForce, int reach) {
        setPosX(posX);
        setPosY(posY);
        setWidth(width);
        setHeight(height);
        this.setTileMap(tileMap);
        setMoveSpeed(moveSpeed);
        setJumpForce(jumpForce);
        setReach(reach);
        setHitboxManager(HitboxManager.getInstance());
        setType(ActorEnum.PLAYER);
        setHealth(10);

        this.inventory = new Inventory();
        this.craftingManager = new CraftingManager(this.inventory);
        this.playerMouvementEnums = new HashSet<>();
        this.itemInHandProperty = new SimpleObjectProperty<>(inventory.getInventorySlot(0).getItem());
        this.quantityOfItemInHandProperty = new SimpleIntegerProperty(inventory.getInventorySlot(0).getQuantity());
        getHitboxManager().createHitbox(this, HitboxType.INTERACTION);
    }

    public Set<PlayerMouvementsEnum> getPlayerMouvements() {return playerMouvementEnums;} // TODO: retirer le getter

    public void update() {
        updatePosition();
        // Create a copy of the loot collection to avoid ConcurrentModificationException
        Set<Loot> lootCopy = new HashSet<>(Loot.lootOnMapProperty.get()); // TODO Akram : analyser le fonctionnement en détail
        for (Loot loot : lootCopy) {
            if (getCollider().intersectsWith(loot.getCollider())) {
                pickUp(loot);
            }
        }
    }

    @Override
    public void updatePosition() { //TODO: (Lino) toujours la continuité de la réforme sur le mouvement dans actor et entity (à voir)
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

        // Create a copy of the loot collection to avoid ConcurrentModificationException
        Set<Loot> lootCopy = new HashSet<>(Loot.lootOnMapProperty.get());
        for (Loot loot : lootCopy) {
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

    public Inventory getInventory() {
        return this.inventory;
    }

    public boolean usesItemInHand(MouseItemActionInputHandler eventHandler) {
        if (getQuantityOfItemInHand() > 0) {
            return getItemInHand().isUsed(eventHandler);
        } else {
            return false;
        }
    }

    /**
     * Checks if a target position is within the player's reach
     * @param targetX the x coordinate of the target
     * @param targetY the y coordinate of the target
     * @param reachDistance the reach distance to check against (in tiles)
     * @return true if the target is within reach, false otherwise
     */
    public boolean isWithinReach(double targetX, double targetY, double reachDistance) {
        // Calculate the center position of the player
        double playerCenterX = getPosX() + getWidth() / 2;
        double playerCenterY = getPosY() + getHeight() / 2;

        // Calculate the distance between the player and the target
        double distance = Math.sqrt(
            Math.pow((targetX * TileMap.format) - playerCenterX, 2) + 
            Math.pow((targetY * TileMap.format) - playerCenterY, 2)
        );

        // Convert the distance to tiles and check if it's within reach
        return distance <= reachDistance * TileMap.format;
    }

    /**
     * Checks if a target position is within the player's default reach
     * @param targetX the x coordinate of the target
     * @param targetY the y coordinate of the target
     * @return true if the target is within reach, false otherwise
     */
    public boolean isWithinReach(double targetX, double targetY) {
        return isWithinReach(targetX, targetY, getReach());
    }

    public void consumeOneItem() {
        this.quantityOfItemInHandProperty.setValue(quantityOfItemInHandProperty.getValue() - 1);
    }

    public Item getItemInHand() {
        System.out.println(itemInHandProperty.getValue());
        return this.itemInHandProperty.getValue();
    }
    public ObjectProperty<Item> itemInHandProperty() {return this.itemInHandProperty;}
    public int getQuantityOfItemInHand() {return this.quantityOfItemInHandProperty.getValue();}
    public IntegerProperty quantityOfItemInHandProperty() {return this.quantityOfItemInHandProperty;}
    public CraftingManager getCraftingManager() {return this.craftingManager;}
    public void addPlayerMouvements(PlayerMouvementsEnum playerMouvementsEnum) {this.playerMouvementEnums.add(playerMouvementsEnum);}
    public void removePlayerMouvements(PlayerMouvementsEnum playerMouvementsEnum) {this.playerMouvementEnums.remove(playerMouvementsEnum);}
}
