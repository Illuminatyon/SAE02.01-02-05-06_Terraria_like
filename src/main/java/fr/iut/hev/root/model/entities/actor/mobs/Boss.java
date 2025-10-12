package fr.iut.hev.root.model.entities.actor.mobs;

import fr.iut.hev.root.model.entities.actor.Actor;
import fr.iut.hev.root.model.entities.actor.ActorEnum;
import fr.iut.hev.root.model.entities.actor.Player;
import fr.iut.hev.root.model.land.TileMap;
import fr.iut.hev.root.model.physics.hitbox.HitboxManager;
import javafx.scene.layout.Pane;

import java.util.*;

public class Boss extends AggressiveMob {

    public Boss(
            int posX, int posY, int width, int height,
            TileMap tileMap, int health, int moveSpeed, int jumpForce, int reach,
            ActorEnum type, Player player,
            int aggroDistance, int attackCooldown,
            List<Actor> aliveActors, Pane globalPane,
            int damage,
            HitboxManager hitboxManager
    ) {
        super(posX, posY, width, height, tileMap, health, moveSpeed, jumpForce, reach, type, player, aggroDistance, attackCooldown, aliveActors, globalPane, damage, hitboxManager);
    }

//     @Override
//    public void attackPlayer(){
//        if ((super.getPlayer().getEffectivePosX()- super.getEffectivePosX()) < 75){
//            super.
 //       }
   // }

// TODO : Elle va partir aussi cette classe parce que la ça ne sert à rien
}
