package fr.iut.hev.root.controller.Listeners;

import fr.iut.hev.root.model.entities.Entity;
import fr.iut.hev.root.model.entities.Interactive;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;

public class InteractionHitboxListener implements ChangeListener<Number> {
    private Entity listenedEntity;
    private HitboxManager hitboxManager;

    public InteractionHitboxListener(Entity listenedEntity,HitboxManager hitboxManager) {
        this.listenedEntity = listenedEntity;
        this.hitboxManager = hitboxManager;
    }

    @Override
    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
        ArrayList<Interactive> interactors = hitboxManager.checkInteractiveCollision(listenedEntity);

        if (!(interactors.isEmpty())) {
            for (Interactive interactor : interactors) {
                interactor.handlerInteraction();
            }
        }
    }
}
