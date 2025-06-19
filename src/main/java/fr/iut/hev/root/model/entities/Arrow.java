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

    // Store the original double values of velocity for more precise movement
    private double velocityXDouble;
    private double velocityYDouble;

    private GlobalController globalController;

    public Arrow(int posX, int posY, int width, int height, TileMap tileMap,
                 double velocityX, double velocityY, int damage,
                 AnchorPane actorsPane, GlobalController globalController, ItemFactory itemFactory) {
        // Use the ARROW ActorEnum
        super(posX, posY, width, height, tileMap, 1, 0, 0, 0, ActorEnum.ARROW, new HitboxManager());

        this.velocityXInitial = velocityX;
        this.velocityYInitial = velocityY;
        this.damage = damage;
        this.globalController = globalController;
        this.itemFactory = itemFactory;

        // Store the original double values for more precise movement
        this.velocityXDouble = velocityX;
        this.velocityYDouble = velocityY;

        // Set initial velocity (still needed for collision detection)
        setVelocityX((int)velocityX);
        setVelocityY((int)velocityY);

        // Create arrow view
        this.arrowView = new ArrowView(this, tileMap, actorsPane);

        // Calculate the angle of the arrow
        double angle = Math.atan2(velocityY, velocityX);

        // Create a longer, thinner hitbox in the direction of travel for better collision detection
        double hitboxLength = width * 1.5; // Make the hitbox longer than the arrow
        double hitboxWidth = height * 0.8; // Make the hitbox thinner than the arrow

        // Create a custom attack hitbox that is longer in the direction of travel
        // This will make the arrow more likely to hit targets
        double offsetX = Math.cos(angle) * width / 4; // Offset the hitbox to be centered on the arrow
        double offsetY = Math.sin(angle) * height / 4;

        // Remove the default VULNERABLE hitbox created by Actor constructor
        getHitboxManager().removeHitboxes(this);

        // Create a custom attack hitbox
        getHitboxManager().createWeaponAttackHitbox(this, -width/2, -height/2, width, height);

        // Create a custom vulnerable hitbox
        getHitboxManager().createHitbox(this, HitboxType.VULNERABLE);

        // Add death listener to remove arrow when it hits something
        healthProperty().addListener(new DeathListener(this, arrowView, globalController.getAliveActors(), itemFactory));

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
                // Don't set arrowView to null here, let DeathListener handle it
            }

            // Set health to 0 to trigger DeathListener if it hasn't been triggered yet
            if (getHealth() > 0) {
                setHealth(0);
                System.out.println("[DEBUG_LOG] Arrow health set to 0 after hasHit check in updatePosition");
            }

            return;
        }

        // Apply gravity to Y velocity (arrows should arc)
        // Update both the integer and double velocity values
        velocityYDouble += Gravity.getGravityForce() * GRAVITY_FACTOR;
        setVelocityY((int)velocityYDouble);

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
                if (actor.equals(this) || actor instanceof Player) {
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

        // Update position using the double values for more precise movement
        posXProperty().set(posXProperty().getValue() + (int)velocityXDouble);
        posYProperty().set(posYProperty().getValue() + (int)velocityYDouble);

        // The arrow view update is now handled by the GlobalController in updateCameraPosition()
        if (!hasHit) {
            if (arrowView == null) {
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
        // Use the double velocity values for more accurate angle calculation
        return Math.toDegrees(Math.atan2(velocityYDouble, velocityXDouble));
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

    /**
     * Gets the arrow view
     * @return the arrow view
     */
    public ArrowView getArrowView() {
        return arrowView;
    }
}
