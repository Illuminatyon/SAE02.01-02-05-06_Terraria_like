package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.enums.HitboxType;
import fr.iut.hev.root.model.hitbox.HitboxManager;

public class Pnj extends Mob implements Interactive {

    public Pnj(int posX, int posY, int width, int height, TileMap tileMap, int health, int moveSpeed, int jumpForce, int reach, ActorEnum actor, HitboxManager hitboxManager) {
        super(posX, posY, width, height, tileMap, health, moveSpeed, jumpForce, reach, actor, hitboxManager);
        getHitboxManager().createHitbox(this,HitboxType.INTERACTION);
    }

    @Override
    public void updateHorizontalMovement (){
        int currentDirection=super.Changement();

        // Appliquer la direction actuelle
        if ((currentDirection == -1 )&& (getPosX()>32) ) {
            super.setLookDirection(LookDirections.LEFT );
            if (!super.getCollider().hasCollisionLeft()) {
                super.setVelocityX(-super.getMoveSpeed());
            } else {
                updateVerticalMovement(); // Saut si bloqué
            }
        } else if ((currentDirection == 1) &&(getPosX()<342 ) ){
            super.setLookDirection(LookDirections.RIGHT);
            if (!super.getCollider().hasCollisionRight()) {
                super.setVelocityX(super.getMoveSpeed());
            } else {
                updateVerticalMovement(); // Saut si bloqué
            }
        } else {
            super.setVelocityX(0);
        }
    }

    public void handlerInteraction(Player player) {
        System.out.println("interaction");
        System.out.println("[DEBUG] NPC interaction handled!");
        System.out.println("[DEBUG] NPC Type: " + this.getName());
        System.out.println("[DEBUG] Distance between Player and NPC: " + 
            Math.sqrt(Math.pow(player.getPosX() - this.getPosX(), 2) + 
                     Math.pow(player.getPosY() - this.getPosY(), 2)));

        // Check if hitboxes are intersecting
        boolean hitboxesIntersect = player.getInteractiveHitbox().intersects(
            player.getHitboxManager().getInteractiveHitboxes().get(this), player);
        System.out.println("[DEBUG] Hitboxes intersecting: " + hitboxesIntersect);
    }
}
