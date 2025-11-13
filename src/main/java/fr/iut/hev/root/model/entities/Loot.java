package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.World;
import fr.iut.hev.root.model.items.Item;

public class Loot extends Entity {
    private final Item item;
    private final int quantity;

    public Loot(Item item, int quantity, int posX, int posY, int width, int height) {
        super(posX, posY, width, height);
        this.item = item;
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }



    public void updatePosition() {
        applyGravity();
        super.posYProperty().set(super.posYProperty().getValue() + super.getVelocityY());
    }

    public void removeSelf() {
        World.getInstance().getLootManager().getLootOnMap().remove(this);
    }
}