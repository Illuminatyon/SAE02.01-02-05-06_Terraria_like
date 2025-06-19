package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.Collider;
import fr.iut.hev.root.model.Gravity;
import fr.iut.hev.root.model.TileMap;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class Entity {
    private IntegerProperty posXProperty;
    private IntegerProperty posYProperty;
    private int width;
    private int height;
    private int velocityX;
    private int velocityY;
    private int velocityMultiplier;
    private TileMap tileMap;
    private Collider collider;

    public Entity(int posX, int posY, int width, int height, TileMap tileMap) {
        this.posXProperty = new SimpleIntegerProperty(posX);
        this.posYProperty = new SimpleIntegerProperty(posY);
        this.width = width;
        this.height = height;
        this.velocityX = 0;
        this.velocityY = 0;
        this.tileMap = tileMap;
        this.collider = new Collider(tileMap, this);
    }

    public void updatePosition() {
        applyGravity();
        posXProperty.set(posXProperty.getValue() + velocityX);
        posYProperty.set(posYProperty.getValue() + velocityY);
    }

    public void applyGravity() {
        if (!this.collider.hasCollisionBottom(this.velocityY + 1)) {
            //if (super.getVelocityY() < maxVelocityY)
            velocityY += Gravity.getGravityForce();
        } else {
            velocityY = 0;
        }
    }

    public final int getPosX() {
        return this.posXProperty.getValue();
    }

    public final void setPosX(double newPosX) {
        posXProperty.setValue(newPosX);
    }

    public final IntegerProperty posXProperty() {
        return this.posXProperty;
    }

    public final int getPosY() {
        return this.posYProperty.getValue();
    }

    public final void setPosY(double newPosY) {
        this.posYProperty.setValue(newPosY);
    }

    public final IntegerProperty posYProperty() {
        return this.posYProperty;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getVelocityX() {
        return this.velocityX;
    }

    public void setVelocityX(int velocity) {
        this.velocityX = velocity;
    }

    public int getVelocityY() {
        return this.velocityY;
    }

    public void setVelocityY(int velocity) {
        this.velocityY = velocity;
    }

    public Collider getCollider() {
        return this.collider;
    }

    public TileMap getTileMap() {return this.tileMap;}

    public int getEffectivePosX() {
        return this.getPosX() + (tileMap.getWidth() * TileMap.format) / 2;
    }

    public int getEffectivePosY() {
        return this.getPosY() + (tileMap.getHeight() * TileMap.format) / 2;
    }
}
