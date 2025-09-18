package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.Gravity;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.HitboxType;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class Actor extends Entity {

    private IntegerProperty healthProperty;
    private int moveSpeed;
    private int jumpForce;
    private boolean isJumping;
    private int jumpingTestDecay; // TODO : faire un renommage pour que ce soit plus explicite
    private int reach;
    private ActorEnum type; //TODO: réfléchir à l'utilité de cette enum ptet demander à un prof
    private HitboxManager hitboxManager;
    private IntegerProperty lookDirectionProperty;

    // TODO : refactor avec healProperty comme un Enum, pour faciliter et mettre les guetteurs / setteurs autre part | (Lino) imaginer une classe barre de vie
    // TODO : Même chose pour les autres attributs qui sont des Enums, parce que finalement la classe Actor elle est chargé de malade | (Lino) une classe potentielle pour le mouvement

    public enum LookDirections {
        RIGHT(1),
        LEFT(-1);

        private int value;

        LookDirections(int value) {
            this.value = value;
        }
    }; // TODO : faire un refactoring pour que les directions soient des enum

    public Actor(int posX, int posY, int width, int height, TileMap tileMap, int healthProperty, int moveSpeed, int jumpForce,int reach, ActorEnum type, HitboxManager hitboxManager) {
        super(posX, posY, width, height, tileMap);
        this.healthProperty = new SimpleIntegerProperty(healthProperty);
        this.moveSpeed = moveSpeed;
        this.jumpForce = jumpForce;
        this.lookDirectionProperty = new SimpleIntegerProperty(LookDirections.RIGHT.value); // TODO : changer ça aussi
        this.isJumping = false;
        this.jumpingTestDecay = 0; // TODO : renommer aussi
        this.reach = reach;
        this.type = type;
        this.hitboxManager = new HitboxManager();
        hitboxManager.createHitbox(this, HitboxType.VULNERABLE);
    }

    @Override
    public void updatePosition() {
        applyGravity();
        updateHorizontalMovement();
        updateVerticalMovement();
        super.posXProperty().set(super.posXProperty().getValue() + super.getVelocityX() * moveSpeed);
        super.posYProperty().set(super.posYProperty().getValue() + super.getVelocityY());
    } // TODO : Potentiellement faire un refactoring ? Parce que dans la classe Entity, y'a déjà une fonction qui a le même nom | ptet essayer d'économiser le plus de code entre les deux fonctionnements (ateurs et entité comme looot par exemple

    @Override
    public void applyGravity() {
        if (!super.getCollider().hasCollisionBottom(super.getVelocityY() + 1) && !isJumping) {
            //if (super.getVelocityY() < maxVelocityY)
            super.setVelocityY(super.getVelocityY() + Gravity.getGravityForce());
        } else {
            super.setVelocityY(0);
        }
    } // TODO : Même chose ici je pense, on retrouve le même problème

    public HitboxManager getHitboxManager() {
        return hitboxManager;
    }

    public void updateHorizontalMovement() {}

    public void updateVerticalMovement(){}

    public String getName(){return this.type.getName();}

    public int getReach() {return this.reach;}

    public int getMoveSpeed() {
        return this.moveSpeed;
    }

    public int getJumpForce() {
        return this.jumpForce;
    }

    public boolean getIsJumping() {
        return this.isJumping;
    }

    public void setIsJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    public int getJumpingTestDecay() {
        return this.jumpingTestDecay;
    }

    public void setJumpingTestDecay(int newValue) {
        this.jumpingTestDecay = newValue;
    }

    public final int getHealth() {return this.healthProperty.getValue();}

    public final void setHealth(int halfHeart) {this.healthProperty.setValue(halfHeart);}

    public final IntegerProperty healthProperty() {return this.healthProperty;}

    public int getLookDirection() {return this.lookDirectionProperty.getValue();}

    public void receiveDamage(int damage) {
        this.setHealth(getHealth() - damage);
    }

    /*public LookDirections getLookDirection() { // TODO: fix ou directement retirer
        return this.lookDirectionProperty;
    }*/

    // TODO : faire un refactoring des guetteurs et des setters en fonction des différentes modifications qui vont
    // TODO : êtres apportés au sein du code.

    public void setLookDirection(LookDirections newLookDirection) {
        this.lookDirectionProperty.setValue(newLookDirection.value);
    }

    public IntegerProperty lookDirectionProperty() {
        return this.lookDirectionProperty;
    }
}
