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
            aliveActors.remove(actor);
            actorView.deleteActorSprite();
        }
    }
}
