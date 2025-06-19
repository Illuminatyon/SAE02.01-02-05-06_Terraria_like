package fr.iut.hev.root.controller.Listeners;

import fr.iut.hev.root.model.entities.Actor;
import fr.iut.hev.root.model.entities.Arrow;
import fr.iut.hev.root.model.entities.Loot;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.items.ItemFactory;
import fr.iut.hev.root.view.ActorView;
import fr.iut.hev.root.view.ArrowView;
import fr.iut.hev.root.view.HUDView;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Listener that handles the death of an actor
 * Removes the actor from the aliveActors list and deletes its sprite
 * Uses Platform.runLater() to ensure UI updates happen on the JavaFX thread
 */
public class DeathListener implements ChangeListener<Number> {

    private Actor actor;
    private ArrayList<Actor> aliveActors;
    private ActorView actorView;
    private ItemFactory itemFactory;
    private Random random;

    /**
     * Constructor for the DeathListener
     * @param actor the actor to listen to
     * @param actorView the view of the actor
     * @param aliveActors the list of alive actors
     * @param itemFactory the item factory for creating loot
     */
    public DeathListener(Actor actor, ActorView actorView, ArrayList<Actor> aliveActors, ItemFactory itemFactory) {
        this.actor = actor;
        this.actorView = actorView;
        this.aliveActors = aliveActors;
        this.itemFactory = itemFactory;
        this.random = new Random();
    }

    @Override
    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
        if (actor.getHealth() <= 0) {
            // Debug: Log actor death and removal from aliveActors list
            System.out.println("[DEBUG_LOG] Actor " + actor.getName() + " health is " + actor.getHealth() + ", removing from aliveActors list");

            // Check if the actor is a chicken (POULET) and drop raw chicken as loot
            if (actor.getName().equals("poulet")) {
                // Generate a random number of raw chicken pieces (1-3)
                int numChicken = random.nextInt(3) + 1;
                System.out.println("[DEBUG_LOG] Chicken died, dropping " + numChicken + " raw chicken pieces");

                // Create and drop the raw chicken as loot
                for (int i = 0; i < numChicken; i++) {
                    // Create a new Loot object with the raw chicken item
                    Loot rawChickenLoot = new Loot(
                        itemFactory.createItem(ItemsEnum.RAW_CHICKEN),
                        1,  // quantity
                        (int) actor.getPosX() + random.nextInt(20) - 10,  // random position near the chicken
                        (int) actor.getPosY(),
                        32,  // width
                        32,  // height
                        actor.getTileMap()
                    );
                    System.out.println("[DEBUG_LOG] Dropped raw chicken at position (" + rawChickenLoot.getPosX() + ", " + rawChickenLoot.getPosY() + ")");
                }
            }

            // Special handling for Arrow
            if (actor instanceof Arrow) {
                System.out.println("[DEBUG_LOG] Arrow died, removing from aliveActors list and deleting sprite");
            }

            // Check if actor is in aliveActors list before removing
            if (aliveActors.contains(actor)) {
                aliveActors.remove(actor);
                System.out.println("[DEBUG_LOG] Actor " + actor.getName() + " removed from aliveActors list");
            } else {
                System.out.println("[DEBUG_LOG] Actor " + actor.getName() + " not found in aliveActors list");
            }

            // Delete actor sprite using Platform.runLater() to ensure UI updates happen on the JavaFX thread
            if (actorView != null) {
                // For ArrowView, the deleteActorSprite method already uses Platform.runLater()
                if (actorView instanceof ArrowView) {
                    actorView.deleteActorSprite();
                    System.out.println("[DEBUG_LOG] Arrow sprite deleted via ArrowView.deleteActorSprite()");
                } else {
                    // For other actor views, use Platform.runLater() here
                    Platform.runLater(() -> {
                        actorView.deleteActorSprite();
                        System.out.println("[DEBUG_LOG] Actor " + actor.getName() + " sprite deleted via Platform.runLater()");
                    });
                }
            } else {
                System.out.println("[DEBUG_LOG] Actor " + actor.getName() + " view is null, cannot delete sprite");
            }
        }
    }
}
