package fr.iut.hev.root.model.entities.actor.mobs;

import fr.iut.hev.root.model.entities.actor.ActorEnum;
import fr.iut.hev.root.model.land.TileMap;
import fr.iut.hev.root.model.physics.hitbox.HitboxType;
import fr.iut.hev.root.model.physics.hitbox.HitboxManager;

public class Pnj extends Mob {

    public Pnj(int posX, int posY, int width, int height, TileMap tileMap, int health, int moveSpeed, int jumpForce, int reach, ActorEnum actor, HitboxManager hitboxManager) {
        super(posX, posY, width, height, tileMap, health, moveSpeed, jumpForce, reach, actor); // TODO : NORMALEMENT IL FAUT METTRE UN HITBOX MANAGER ! JE LAI ENLEVE PARCE QUE YAVAIT DES BUGS
        getHitboxManager().createHitbox(this,HitboxType.INTERACTION);
    }


}
