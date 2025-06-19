package fr.iut.hev.root.model.entities;

import com.sun.source.tree.WhileLoopTree;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.enums.HitboxType;
import fr.iut.hev.root.model.hitbox.Hitbox;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import fr.iut.hev.root.model.hitbox.RectangleHitbox;
import javafx.scene.layout.AnchorPane;

public class Pnj extends Mob{

    public Pnj(int posX, int posY, int width, int height, TileMap tileMap, int health, int moveSpeed, int jumpForce, int reach, ActorEnum actor, HitboxManager hitboxManager, AnchorPane pane) {
        super(posX, posY, width, height, tileMap, health, moveSpeed, jumpForce, reach, actor, hitboxManager, pane);
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


}
