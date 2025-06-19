package fr.iut.hev.root.model.entities;

import fr.iut.hev.root.controller.GlobalController;
import fr.iut.hev.root.controller.Listeners.DeathListener;
import fr.iut.hev.root.model.Gravity;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.enums.ActorEnum;
import fr.iut.hev.root.model.enums.HitboxType;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import fr.iut.hev.root.model.items.ItemFactory;
import fr.iut.hev.root.view.ArrowView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class Arrow extends Actor {
    private static final double GRAVITY_FACTOR = 0.0; // No gravity for arrows
    private double velocityXInitial;
    private double velocityYInitial;
    private int damage;
    private ArrowView arrowView;
    private boolean hasHit = false;
    private ItemFactory itemFactory;

    private GlobalController globalController;

    public Arrow(int posX, int posY, int width, int height, TileMap tileMap,
                 double velocityX, double velocityY, int damage,
                 AnchorPane actorsPane, GlobalController globalController,ItemFactory itemFactory) {
        // Use the ARROW ActorEnum
        super(posX, posY, width, height, tileMap, 1, 0, 0, 0, ActorEnum.ARROW, new HitboxManager());

        this.velocityXInitial = velocityX;
        this.velocityYInitial = velocityY;
        this.damage = damage;
        this.globalController = globalController;
        this.itemFactory = itemFactory;

        // Set initial velocity
        setVelocityX((int)velocityX);
        setVelocityY((int)velocityY);

        // Create arrow view
        this.arrowView = new ArrowView(this, tileMap, actorsPane);

        // Create attack hitbox for the arrow
        // Using a rectangular hitbox that matches the arrow's dimensions
        getHitboxManager().createHitbox(this, HitboxType.ATTACK);

        // Add death listener to remove arrow when it hits something
        healthProperty().addListener(new DeathListener(this, arrowView, globalController.getAliveActors(),itemFactory));

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

            // Ensure the arrow is removed from the view if it's still present
            if (arrowView != null && arrowView.getActorSprite() != null) {
                System.out.println("[DEBUG_LOG] Arrow still has a sprite, removing it");
                arrowView.deleteActorSprite();
                // Set arrowView to null to prevent further deletion attempts
                arrowView = null;
            }

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

            // Set health to 0 to trigger DeathListener, which will handle removing the arrow from aliveActors
            setHealth(0);
            System.out.println("[DEBUG_LOG] Arrow health set to 0 after hitting terrain");

            // Let the DeathListener handle the sprite removal
            return;
        }

        // Update hitbox positions as the arrow moves
        getHitboxManager().updateHitboxPositions(this);

        // Check for collisions with actors using hitboxes
        List<Entity> hitEntities = getHitboxManager().checkAttackCollisions(this, damage);

        if (!hitEntities.isEmpty()) {
            // Arrow hit at least one entity
            hasHit = true;

            // Log the hits
            for (Entity entity : hitEntities) {
                if (entity instanceof Actor) {
                    System.out.println("[DEBUG_LOG] Arrow hit a mob: " + ((Actor)entity).getName() + " at position: " + getPosX() + ", " + getPosY());
                } else {
                    System.out.println("[DEBUG_LOG] Arrow hit an entity at position: " + getPosX() + ", " + getPosY());
                }
            }

            // Set health to 0 to trigger DeathListener, which will handle removing the arrow from aliveActors
            setHealth(0);
            System.out.println("[DEBUG_LOG] Arrow health set to 0 after hitting entities");

            // Let the DeathListener handle the sprite removal
            return;
        }

        // Fallback to traditional collision detection for actors without hitboxes
        ArrayList<Actor> aliveActors = getAliveActors();
        if (aliveActors != null) {
            for (Actor actor : aliveActors) {
                // Skip self and player
                if (actor == this || actor instanceof Player) {
                    continue;
                }

                // Check if arrow collides with actor using colliders
                if (getCollider().intersectsWith(actor.getCollider())) {
                    // Apply damage to actor
                    actor.receiveDamage(damage);

                    // Mark arrow as hit
                    hasHit = true;
                    System.out.println("[DEBUG_LOG] Arrow hit a mob (using collider): " + actor.getName() + " at position: " + getPosX() + ", " + getPosY());

                    // Set health to 0 to trigger DeathListener, which will handle removing the arrow from aliveActors
                    setHealth(0);
                    System.out.println("[DEBUG_LOG] Arrow health set to 0 after hitting mob: " + actor.getName());

                    // Let the DeathListener handle the sprite removal
                    return;
                }
            }
        }

        // Update position
        posXProperty().set(posXProperty().getValue() + getVelocityX());
        posYProperty().set(posYProperty().getValue() + getVelocityY());

        // Update the arrow view only if the arrow hasn't hit anything
        if (!hasHit) {
            if (arrowView != null) {
                System.out.println("[DEBUG_LOG] Updating arrow view");
                arrowView.update();
            } else {
                System.out.println("[DEBUG_LOG] Cannot update arrow view: arrowView is null");
            }
        } else {
            System.out.println("[DEBUG_LOG] Arrow has hit something, not updating view");
        }
    }

    private ArrayList<Actor> getAliveActors() {
        if (globalController != null) {
            return globalController.getAliveActors();
        }
        return null;
    }

    // Calculate the angle of the arrow based on its velocity
    public double getAngle() {
        return Math.toDegrees(Math.atan2(getVelocityY(), getVelocityX()));
    }

    // Get the GlobalController
    public GlobalController getGlobalController() {
        return globalController;
    }

    /**
     * Checks if the arrow has hit something
     * @return true if the arrow has hit something, false otherwise
     */
    public boolean hasHit() {
        return hasHit;
    }

    /**
     * Gets the hitbox manager for this arrow
     * @return the hitbox manager
     */
    public HitboxManager getHitboxManager() {
        return super.getHitboxManager();
    }
}
