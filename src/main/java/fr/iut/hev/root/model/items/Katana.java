package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.Collider;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.entities.Actor;
import fr.iut.hev.root.model.entities.Entity;
import fr.iut.hev.root.model.entities.Mob;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import fr.iut.hev.root.view.KatanaView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Katana weapon class
 */
public class Katana extends Weapon {
    private static final int KNOCKBACK_STRENGTH = 8;
    private boolean isAnimating = false;
    private KatanaView katanaView;

    // Custom reach for katana (significantly better than dagger)
    private static final double KATANA_REACH_MULTIPLIER = 2.0; // 100% more reach than default

    public Katana(ItemsEnum itemsEnum, HitboxManager hitboxManager) {
        super(itemsEnum,hitboxManager);
    }

    @Override
    public boolean isUsed(MouseItemActionInputHandler eventHandler) {
        int x = (int)eventHandler.getX() / TileMap.format;
        int y = (int)eventHandler.getY() / TileMap.format;
        Player player = eventHandler.getPlayer();

        // Calculate the position for the katana based on player position and mouse coordinates
        double playerCenterX = player.getPosX() + player.getWidth() / 2;
        double playerCenterY = player.getPosY() + player.getHeight() / 2;

        // Calculate direction vector from player to mouse
        final double dirX = eventHandler.getX() - playerCenterX;
        final double dirY = eventHandler.getY() - playerCenterY;

        // Calculate the distance from player to mouse click
        double distance = Math.sqrt(dirX * dirX + dirY * dirY);

        // Define minimum and maximum distance for the katana attack
        double minDistance = 50; // Minimum distance in pixels
        double maxDistance = player.getReach() * KATANA_REACH_MULTIPLIER * TileMap.format; // Maximum distance in pixels

        // Check if the target position is within the valid range for the katana attack
        if (distance < minDistance || distance > maxDistance) {
            return false;
        }

        // If already animating, don't start a new animation
        if (isAnimating) {
            return false;
        }

        // Normalize the direction vector
        final double normalizedDirX = dirX / distance;
        final double normalizedDirY = dirY / distance;

        // Position the katana directly on the player so the handle is at the player's position
        double katanaX = playerCenterX;
        double katanaY = playerCenterY;

        // Calculate the target position for the katana (where the player clicked)
        double targetX = eventHandler.getX();
        double targetY = eventHandler.getY();

        // Create a temporary entity for the katana hitbox that covers the entire path
        // The hitbox will be a rectangle that covers the path from player to target
        int hitboxX = (int)Math.min(katanaX, targetX) - 32;
        int hitboxY = (int)Math.min(katanaY, targetY) - 32;
        int hitboxWidth = (int)Math.abs(targetX - katanaX) + 64;
        int hitboxHeight = (int)Math.abs(targetY - katanaY) + 64;

        Entity katanaEntity = new Entity(hitboxX, hitboxY, hitboxWidth, hitboxHeight, eventHandler.getTileMap());
        Collider katanaCollider = katanaEntity.getCollider();

        // Set animating flag
        isAnimating = true;

        // Create or update the katana view for animation
        if (katanaView == null) {
            // Get the actorsPane from the global controller
            AnchorPane actorsPane = eventHandler.getGlobalController().getEntitiesPane();
            katanaView = new KatanaView(actorsPane);
        }

        // Show the katana plunge animation
        katanaView.showKatanaPlungeAnimation(
            katanaX + eventHandler.getGlobalController().getCameraOffsetX(), 
            katanaY + eventHandler.getGlobalController().getCameraOffsetY(), 
            normalizedDirX, 
            normalizedDirY,
            targetX + eventHandler.getGlobalController().getCameraOffsetX(),
            targetY + eventHandler.getGlobalController().getCameraOffsetY()
        );

        // Create animation timeline with increased cooldown (2 seconds total)
        Timeline timeline = new Timeline(
            // Apply damage when the katana reaches the target (0.25 seconds)
            new KeyFrame(Duration.seconds(0.25), e -> {
                // Get the list of alive actors from the global controller
                ArrayList<Actor> aliveActors = eventHandler.getGlobalController().getAliveActors();

                // Create a copy of the aliveActors list to avoid ConcurrentModificationException
                ArrayList<Actor> actorsCopy = new ArrayList<>(aliveActors);

                // Check for collisions with mobs
                for (Actor actor : actorsCopy) {
                    // Skip the player
                    if (actor == player) {
                        continue;
                    }

                    // Check if the katana hitbox intersects with the actor's collider
                    if (katanaCollider.intersectsWith(actor.getCollider())) {
                        // Apply damage to the actor
                        actor.receiveDamage(getDamage());

                        // Apply knockback if the actor is a Mob
                        if (actor instanceof Mob) {
                            applyKnockback((Mob) actor, normalizedDirX, normalizedDirY);
                        }

                        System.out.println("Katana hit " + actor.getName() + "! Remaining health: " + actor.getHealth());
                    }
                }
            }),
            new KeyFrame(Duration.seconds(2.0), e -> {
                // End animation and reset after 2 seconds (increased cooldown)
                isAnimating = false;
                if (katanaView != null) {
                    katanaView.hideKatana();
                }
            })
        );

        timeline.play();

        return true;
    }

    private void applyKnockback(Mob mob, double dirX, double dirY) {
        // Apply knockback in the direction of the attack
        mob.setVelocityX((int)(dirX * KNOCKBACK_STRENGTH));
        mob.setVelocityY((int)(dirY * KNOCKBACK_STRENGTH));
    }
}
