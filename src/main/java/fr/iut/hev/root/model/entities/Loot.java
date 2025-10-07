package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.Gravity;
import fr.iut.hev.root.model.items.Item;
import fr.iut.hev.root.model.TileMap;

public class Loot extends Entity {
    private final Item item;
    private final int quantity;

    public Loot(Item item, int quantity, int posX, int posY, int width, int height, TileMap tileMap) {
        super(posX, posY, width, height, tileMap);
        this.item = item;
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public void applyGravity() {
        if (!super.getCollider().hasCollisionBottom(super.getVelocityY() + 1)) {
            super.setVelocityY(super.getVelocityY() + Gravity.getGravityForce());
        } else {
            super.setVelocityY(0);
        }
    }

    public void updatePosition() {
        applyGravity();
        super.posYProperty().set(super.posYProperty().getValue() + super.getVelocityY());
    }
}
