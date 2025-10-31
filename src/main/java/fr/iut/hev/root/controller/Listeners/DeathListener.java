package fr.iut.hev.root.controller.Listeners;

import fr.iut.hev.root.model.entities.actor.Actor;
import fr.iut.hev.root.model.entities.LootManager;
import fr.iut.hev.root.view.actor.ActorView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.util.ArrayList;

public class DeathListener implements ChangeListener<Number> {

    private final Actor actor;
    private final ArrayList<Actor> aliveActors;
    private final ActorView actorView;
    private final LootManager lootManager;

    public DeathListener(Actor actor, ActorView actorView, ArrayList<Actor> aliveActors, LootManager lootManager) {
        this.actor = actor;
        this.actorView = actorView;
        this.aliveActors = aliveActors;
        this.lootManager = lootManager;
    }

    @Override
    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
        if (actor.getHealth() <= 0) {
            logActorDeath();
            lootManager.dropLootForActor(actor);
            removeActorFromAliveList();
            deleteActorSprite();
        }
    }

    // Ce ne sont que des fonctions afin de juste faire des affichages, pour comprendre un peu le déroulé du code
    // et si on a besoin de faire des appels du coup

    private void logActorDeath() {
        System.out.println("[DEBUG_LOG] Actor " + actor.getName() + " health is " + actor.getHealth() + ", removing from aliveActors list");
    }

    private void removeActorFromAliveList() {
        if (aliveActors.contains(actor)) {
            aliveActors.remove(actor);
        }
    }

    private void deleteActorSprite() {
        if (actorView != null) {
            actorView.deleteActorSprite();
        }
    }
}
