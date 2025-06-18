package fr.iut.hev.root.controller.Listeners;

import fr.iut.hev.root.model.entities.Actor;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.view.ActorView;
import fr.iut.hev.root.view.HUDView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.nio.charset.MalformedInputException;
import java.util.ArrayList;

public class DeathListener implements ChangeListener<Number> {

    private Actor actor;
    private ArrayList<Actor> aliveActors;
    private ActorView actorView;

    public DeathListener(Actor actor,ActorView actorView, ArrayList<Actor> aliveActors) {
        this.actor = actor;
        this.actorView = actorView;
        this.aliveActors = aliveActors;
    }

    @Override
    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
        if (actor.getHealth() <= 0) {
            // Debug: Log actor death and removal from aliveActors list
            System.out.println("[DEBUG_LOG] Actor " + actor.getName() + " health is " + actor.getHealth() + ", removing from aliveActors list");

            // Check if actor is in aliveActors list before removing
            if (aliveActors.contains(actor)) {
                aliveActors.remove(actor);
                System.out.println("[DEBUG_LOG] Actor " + actor.getName() + " removed from aliveActors list");
            } else {
                System.out.println("[DEBUG_LOG] Actor " + actor.getName() + " not found in aliveActors list");
            }

            // Delete actor sprite
            if (actorView != null) {
                actorView.deleteActorSprite();
                System.out.println("[DEBUG_LOG] Actor " + actor.getName() + " sprite deleted");
            } else {
                System.out.println("[DEBUG_LOG] Actor " + actor.getName() + " view is null, cannot delete sprite");
            }
        }
    }
}
