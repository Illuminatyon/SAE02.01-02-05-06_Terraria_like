package fr.iut.hev.root.model.entities.actor.NpcFactory;

import fr.iut.hev.root.model.entities.actor.ActorEnum;
import fr.iut.hev.root.model.entities.actor.mobs.Mob;

public class InteractiveNpcFactory extends NPCFactory {

    public InteractiveNpcFactory() { }

    @Override
    public Mob setMob(ActorEnum actor) {
        return new Mob();
    }
}
