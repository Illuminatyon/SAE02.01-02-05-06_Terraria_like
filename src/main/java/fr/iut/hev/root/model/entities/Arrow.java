/*package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.controller.GlobalController;
import fr.iut.hev.root.controller.Listeners.DeathListener;
import fr.iut.hev.root.model.Gravity;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import fr.iut.hev.root.model.items.ItemFactory;
import fr.iut.hev.root.view.actor.ArrowView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class Arrow extends Actor {
    private static final double GRAVITY_FACTOR = 0.1; // No gravity for arrows
    private double velocityXInitial;
    private double velocityYInitial;
    private int damage;
    private ArrowView arrowView;
    private boolean hasHit = false;
    private ItemFactory itemFactory;

    private GlobalController globalController; // Global controller should not be here, commented in world

    public Arrow(int posX, int posY, int width, int height, TileMap tileMap,
                 double velocityX, double velocityY, int damage,
                 AnchorPane actorsPane, GlobalController globalController,ItemFactory itemFactory, HitboxManager hitboxManager ) {
        // Use the ARROW ActorEnum
        super(posX, posY, width, height, tileMap, 1, 0, 0, 0, ActorEnum.ARROW, hitboxManager);

        this.velocityXInitial = velocityX;
        this.velocityYInitial = velocityY;
        this.damage = damage;
        this.globalController = globalController; // En commentaire sur world

        // Set initial velocity
        setVelocityX((int)velocityX);
        setVelocityY((int)velocityY);

        // Create arrow view
        this.arrowView = new ArrowView(this, tileMap, actorsPane,camera.getCurrentCamX(),camera.getCurrentCamY());

        // Add death listener to remove arrow when it hits something
        //healthProperty().addListener(new DeathListener(this, arrowView, globalController.getAliveActors(),itemFactory)); // En commentaire dans world a cause de gloalcontroller

        // Debug: Log arrow creation
        System.out.println("[DEBUG_LOG] Arrow created at position: " + posX + ", " + posY);
    }

    @Override
    public void updatePosition() {
        // Debug: Log updatePosition call
        System.out.println("[DEBUG_LOG] Arrow updatePosition called at position: " + getPosX() + ", " + getPosY() + ", hasHit: " + hasHit);

        // If arrow has hit something, don't update position
        if (hasHit) {
            System.out.println("[DEBUG_LOG] Arrow has already hit something, skipping update");
            return;
        }

        // Apply gravity to Y velocity (arrows should arc)
        setVelocityY(getVelocityY() + (int)(Gravity.getGravityForce() * GRAVITY_FACTOR));

        // Check for collisions with terrain
        if (getCollider().hasCollisionRight() || getCollider().hasCollisionLeft() || 
            getCollider().hasCollisionTop(-1) || getCollider().hasCollisionBottom(1)) {
            // Arrow hit terrain, mark it as hit
            hasHit = true;

            // Debug: Log arrow hit terrain
            System.out.println("[DEBUG_LOG] Arrow hit terrain at position: " + getPosX() + ", " + getPosY());

            // Remove the arrow from the view
            setHealth(0);

            // Debug: Log arrow health set to 0
            System.out.println("[DEBUG_LOG] Arrow health set to 0 after hitting terrain");

            // Ensure the arrow view is removed immediately
            if (arrowView != null) {
                arrowView.deleteActorSprite();
                System.out.println("[DEBUG_LOG] Arrow view deleted after hitting terrain");
            } else {
                System.out.println("[DEBUG_LOG] Arrow view was already null when trying to delete after hitting terrain");
            }

            return;
        }

        // Check for collisions with actors
        // DE ICI en com
        ArrayList<Actor> aliveActors = getAliveActors();
        if (aliveActors != null) {
            for (Actor actor : aliveActors) {
                // Skip self and player
                if (actor == this || actor instanceof Player) {
                    continue;
                }

                // Check if arrow collides with actor
                if (getCollider().intersectsWith(actor.getCollider())) {
                    // Apply damage to actor
                    actor.receiveDamage(damage);

                    // Mark arrow as hit and remove it from the view
                    System.out.println("[DEBUG_LOG] Arrow hit a mob: " + actor.getName() + " at position: " + getPosX() + ", " + getPosY());
                    setHealth(0);
                    hasHit = true;

                    // Debug: Log arrow health set to 0
                    System.out.println("[DEBUG_LOG] Arrow health set to 0 after hitting mob: " + actor.getName());

                    // Ensure the arrow view is removed immediately
                    if (arrowView != null) {
                        arrowView.deleteActorSprite();
                        System.out.println("[DEBUG_LOG] Arrow view deleted after hitting mob: " + actor.getName());
                    } else {
                        System.out.println("[DEBUG_LOG] Arrow view was already null when trying to delete after hitting mob: " + actor.getName());
                    }

                    return;
                }
            }
        } // JUSQU ICI en com

        // Update position
        posXProperty().set(posXProperty().getValue() + getVelocityX());
        posYProperty().set(posYProperty().getValue() + getVelocityY());

        // Update the arrow view only if the arrow hasn't hit anything
        if (!hasHit) {
            if (arrowView != null) {
                System.out.println("[DEBUG_LOG] Updating arrow view");
                arrowView.update(); // En com
            } else {
                System.out.println("[DEBUG_LOG] Cannot update arrow view: arrowView is null");
            }
        } else {
            System.out.println("[DEBUG_LOG] Arrow has hit something, not updating view");
        }
    }

    // DE ICI en com
    private ArrayList<Actor> getAliveActors() {
        /*if (globalController != null) {
            return globalController.getAliveActors();
        }*//*
        return null;
    } // JUSQU ICI en com a cause du globalcontroller

    // Calculate the angle of the arrow based on its velocity
    public double getAngle() {
        return Math.toDegrees(Math.atan2(getVelocityY(), getVelocityX()));
    }

    // Get the GlobalController
    public GlobalController getGlobalController() {
        return globalController;
    } // TODO : Retirer toute cette classe finalement, parce que bon
}
*/