package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.Gravity;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.enums.HitboxType;
import fr.iut.hev.root.model.enums.TileTypesEnum;
import fr.iut.hev.root.model.hitbox.Hitbox;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import fr.iut.hev.root.model.pathfinding.AStar;
import fr.iut.hev.root.model.pathfinding.Point;
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
