package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.physics.Collider;
import fr.iut.hev.root.model.physics.Gravity;
import fr.iut.hev.root.model.land.TileMap;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/*

 */
public class Entity {
    private IntegerProperty posXProperty;
    private IntegerProperty posYProperty;
    private int width;
    private int height; // TODO : passer height et width dans collider (vérifier les conséquences dans player)
    private int velocityX;
    private int velocityY;
    private int velocityMultiplier; // TODO : a retirer


    private TileMap tileMap;
    private Collider collider;
    // TODO : Potentiellement faire d'autres classes pour les attributs, afin d'alléger le constructeur et même la classe
    // TODO : en général

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

    public void initAfterDeserialization(TileMap tileMap, int posX, int posY) {
        this.tileMap = tileMap;
        this.collider = new Collider(tileMap, this);
    } // TODO : A retirer

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
        } // TODO : peut être le refactor ? (enlever les if)
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

    public void setWidth(int width) {this.width = width;}

    public void setHeight(int height) {this.height = height;}

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
    }// TODO : a retirer

    public int getEffectivePosY() {
        return this.getPosY() + (tileMap.getHeight() * TileMap.format) / 2;
    } // TODO : a retirer

    public void setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
    }
}
