package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.Collider;
import fr.iut.hev.root.model.Gravity;
import fr.iut.hev.root.model.Item;
import fr.iut.hev.root.model.TileMap;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;

import java.util.HashSet;

public class Loot extends Entity {
    private Item item;
    private int quantity;
    public static final SetProperty<Loot> lootOnMapProperty =
            new SimpleSetProperty<>(FXCollections.observableSet(new HashSet<>()));

    public Loot(Item item, int quantity, int posX, int posY, int width, int height, TileMap tileMap) {
        super(posX, posY, width, height, tileMap);
        this.item = item;
        this.quantity = quantity;
        lootOnMapProperty.get().add(this);
    }

    public void remove() {
        lootOnMapProperty.get().remove(this);
    }

    public void updatePosition() {
        applyGravity();
        super.posYProperty().set(super.posYProperty().getValue() + super.getVelocityY());
    }

    @Override
    public void applyGravity() {
        if (!super.getCollider().hasCollisionBottom(super.getVelocityY() + 1)) {
            //if (super.getVelocityY() < maxVelocityY)
            super.setVelocityY(super.getVelocityY() + Gravity.getGravityForce());
        } else {
            super.setVelocityY(0);
        }
    }

    public static void remove(Loot loot) {
        lootOnMapProperty.get().remove(loot);
    }
}