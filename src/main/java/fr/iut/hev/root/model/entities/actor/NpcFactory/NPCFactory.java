package fr.iut.hev.root.model.entities.actor.NpcFactory;

import fr.iut.hev.root.model.World;
import fr.iut.hev.root.model.entities.actor.ActorEnum;
import fr.iut.hev.root.model.entities.actor.mobs.Mob;
import fr.iut.hev.root.model.physics.hitbox.HitboxManager;

public abstract class NPCFactory {
    private World world;


    public NPCFactory() {
        this.world = World.getInstance();
    }

    public Mob mobFactory(ActorEnum actor, int x, int y) {
        Mob mob = setMob(actor);
        mob.setPosX(x);
        mob.setPosY(y);
        mob.setWidth(actor.getWidth());
        mob.setHeight(actor.getHeight());
        mob.setHealth(actor.getMaxHealth());
        mob.setMoveSpeed(actor.getMoveSpeed());
        mob.setJumpForce(actor.getJumpForce());
        mob.setReach(actor.getReach());
        mob.setHitboxManager(HitboxManager.getInstance());
        return mob;
    }

    public abstract Mob setMob(ActorEnum actor);
}

