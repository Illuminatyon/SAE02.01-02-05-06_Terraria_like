package fr.iut.hev.root.controller.Listeners;

import fr.iut.hev.root.model.entities.Interactive;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;

public class InteractionHitboxListener implements ChangeListener<Number> {
    private Player player;
    private HitboxManager hitboxManager;

    public InteractionHitboxListener(Player player, HitboxManager hitboxManager) {
        this.player = player;
        this.hitboxManager = hitboxManager;
    }

    @Override
    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
        System.out.println("triggered");
        ArrayList<Interactive> interactors = hitboxManager.checkInteractiveCollision(player.getInteractiveHitbox(),player);
        System.out.println(interactors);
        if (!(interactors.isEmpty())) {
            for (Interactive interactor : interactors) {
                interactor.handlerInteraction(player);
            }
        }
    }
}
