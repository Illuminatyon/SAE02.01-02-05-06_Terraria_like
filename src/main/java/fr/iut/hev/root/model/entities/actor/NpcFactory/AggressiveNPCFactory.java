package fr.iut.hev.root.model.entities.actor.NpcFactory;

import fr.iut.hev.root.model.entities.actor.Actor;
import fr.iut.hev.root.model.entities.actor.ActorEnum;
import fr.iut.hev.root.model.entities.actor.mobs.AggressiveMob;
import fr.iut.hev.root.model.entities.actor.mobs.Mob;

public class AggressiveNPCFactory extends NPCFactory {
    public AggressiveNPCFactory() {
        super();
    }



    @Override
    public Mob setMob(ActorEnum actor ) {
        AggressiveMob mob = new AggressiveMob();
        mob.setDamage(actor.getDamage());
        mob.setAggroDistance(actor.getAggrodistance());
        mob.setAttackCooldown(actor.getAttackcooldown());
        return mob;

    }
}
