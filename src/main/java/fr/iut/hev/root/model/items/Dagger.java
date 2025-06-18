package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.Collider;
import fr.iut.hev.root.model.TileMap;
import fr.iut.hev.root.model.entities.Actor;
import fr.iut.hev.root.model.entities.Entity;
import fr.iut.hev.root.model.entities.Mob;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.view.DaggerView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.ArrayList;

public class Dagger extends Weapon {
    private static final int KNOCKBACK_STRENGTH = 5;
    private boolean isAnimating = false;
    private DaggerView daggerView;

    public Dagger(ItemsEnum itemsEnum) {
        super(itemsEnum);
    }

    @Override
    public boolean isUsed(MouseItemActionInputHandler eventHandler) {
        int x = (int)eventHandler.getX() / TileMap.format;
        int y = (int)eventHandler.getY() / TileMap.format;
        Player player = eventHandler.getPlayer();

        // Check if the target position is within the player's reach
        if (!player.isWithinReach(x, y)) {
            return false;
        }

        // If already animating, don't start a new animation
        if (isAnimating) {
            return false;
        }

        // Calculate the position for the dagger based on player position and mouse coordinates
        double playerCenterX = player.getPosX() + player.getWidth() / 2;
        double playerCenterY = player.getPosY() + player.getHeight() / 2;

        // Calculate direction vector from player to mouse
        final double dirX = eventHandler.getX() - playerCenterX;
        final double dirY = eventHandler.getY() - playerCenterY;

        // Normalize the direction vector
        double length = Math.sqrt(dirX * dirX + dirY * dirY);
        final double normalizedDirX = dirX / length;
        final double normalizedDirY = dirY / length;

        // Position the dagger in front of the player in the direction of the mouse
        double daggerX = playerCenterX + normalizedDirX * 40; // 40 pixels in front of player
        double daggerY = playerCenterY + normalizedDirY * 40;

        // Create a temporary entity for the dagger hitbox
        Entity daggerEntity = new Entity((int)daggerX, (int)daggerY, 32, 32, eventHandler.getTileMap());
        Collider daggerCollider = daggerEntity.getCollider();

        // Set animating flag
        isAnimating = true;

        // Create or update the dagger view for animation
        if (daggerView == null) {
            // Get the actorsPane from the global controller
            AnchorPane actorsPane = eventHandler.getGlobalController().getActorsPane();
            daggerView = new DaggerView(actorsPane);
        }

        // Show the dagger animation
        daggerView.showDaggerAnimation(
            daggerX + eventHandler.getGlobalController().getCameraOffsetX(), 
            daggerY + eventHandler.getGlobalController().getCameraOffsetY(), 
            normalizedDirX, 
            normalizedDirY
        );

        // Create animation timeline
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0.2), e -> {
                // Get the list of alive actors from the global controller
                ArrayList<Actor> aliveActors = eventHandler.getGlobalController().getAliveActors();

                // Check for collisions with mobs
                for (Actor actor : aliveActors) {
                    // Skip the player
                    if (actor == player) {
                        continue;
                    }

                    // Check if the dagger hitbox intersects with the actor's collider
                    if (daggerCollider.intersectsWith(actor.getCollider())) {
                        // Apply damage to the actor
                        actor.receiveDamage(getDamage());

                        // Apply knockback if the actor is a Mob
                        if (actor instanceof Mob) {
                            applyKnockback((Mob) actor, normalizedDirX, normalizedDirY);
                        }

                        System.out.println("Dagger hit " + actor.getName() + "! Remaining health: " + actor.getHealth());
                    }
                }
            }),
            new KeyFrame(Duration.seconds(0.4), e -> {
                // End animation
                isAnimating = false;
                if (daggerView != null) {
                    daggerView.hideDagger();
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
