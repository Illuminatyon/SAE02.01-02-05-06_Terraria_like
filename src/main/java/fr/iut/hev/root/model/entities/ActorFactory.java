package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.hitbox.HitboxManager;

import java.util.ArrayList;

public class ActorFactory {
    private TileMap tileMap;
    private Player player;
    private HitboxManager hitboxManager;

    public ActorFactory(TileMap tileMap, Player player, HitboxManager hitboxManager) {
        this.tileMap = tileMap;
        this.player = player;
        this.hitboxManager = hitboxManager;

    }

    public Mob createMob(  int x, int y) {
                        ActorEnum type = ActorEnum.POULET;
                        Mob mob = new Mob(x,y, type.getWidth(), type.getHeight(), tileMap, type.getMaxHealth(), type.getSpeed(), type.getJumpForce(), type.getReach(), type,hitboxManager);
                        return mob;
    }

   public  AggressiveMob createAgressiveMob(int x, int y, ArrayList<Actor> actors){
        ActorEnum type = ActorEnum.ZOMBIE;
        AggressiveMob mob =new  AggressiveMob(x,y, type.getWidth(), type.getHeight(), tileMap, type.getMaxHealth(), type.getSpeed(), type.getJumpForce(), type.getReach(), type,this.player,20,1500, actors,2, this.hitboxManager);

    return mob;
    }
}
