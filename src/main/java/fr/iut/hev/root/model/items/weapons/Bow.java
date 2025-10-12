/*package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.entities.Arrow;
import fr.iut.hev.root.model.entities.actor.Player;
import fr.iut.hev.root.model.items.enums.ItemsEnum;
import fr.iut.hev.root.model.physics.hitbox.HitboxManager;
import fr.iut.hev.root.view.actor.ActorView;
import fr.iut.hev.root.view.weapon.BowView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class Bow extends Weapon {
    private static final int ARROW_SPEED = 10;
    private boolean isAnimating = false;
    private BowView bowView; // N'est pas censé connaitre la vue
    private ItemFactory itemFactory;

    public Bow(ItemsEnum itemsEnum, HitboxManager hitboxManager,ItemFactory itemFactory) {
        super(itemsEnum,hitboxManager);
        this.itemFactory = itemFactory;
    }

    @Override
    public boolean isUsed(MouseItemActionInputHandler eventHandler) {
        Player player = eventHandler.getPlayer();

        // Check if the player has arrows in inventory
        if (!playerHasArrows(player)) {
            System.out.println("No arrows in inventory!");
            return false;
        }

        // If already animating, don't start a new animation
        if (isAnimating) {
            return false;
        }

        // Calculate the position for the bow based on player position
        double playerCenterX = player.getPosX() + player.getWidth() / 2;
        double playerCenterY = player.getPosY() + player.getHeight() / 2;

        // Calculate direction vector from player to mouse
        final double dirX = eventHandler.getX() - playerCenterX;
        final double dirY = eventHandler.getY() - playerCenterY;

        // Normalize the direction vector
        double length = Math.sqrt(dirX * dirX + dirY * dirY);
        final double normalizedDirX = dirX / length;
        final double normalizedDirY = dirY / length;

        // Position the bow in front of the player in the direction of the mouse
        double bowX = playerCenterX + normalizedDirX * 20; // 20 pixels in front of player
        double bowY = playerCenterY + normalizedDirY * 20;

        // Set animating flag
        isAnimating = true;

        // Create or update the bow view for animation
        if (bowView == null) {
            // Get the actorsPane from the global controller
            AnchorPane actorsPane = eventHandler.getGlobalController().getEntitiesPane();
            bowView = new BowView(actorsPane);
        }

        // Show the bow animation
        bowView.showBowAnimation(
            bowX + eventHandler.getGlobalController().getCamera().getCurrentCamX(),
            bowY + eventHandler.getGlobalController().getCamera().getCurrentCamY(),
            normalizedDirX,
            normalizedDirY
        );

        // Create animation timeline
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0.5), e -> {
                // Create and shoot an arrow
                Arrow arrow = new Arrow(
                    (int)playerCenterX,
                    (int)playerCenterY,
                    16,
                    4,
                    eventHandler.getTileMap(),
                    normalizedDirX * ARROW_SPEED,
                    normalizedDirY * ARROW_SPEED,
                    getDamage(),
                    eventHandler.getGlobalController().getEntitiesPane(),
                    eventHandler.getGlobalController(),
                    itemFactory,
                    super.getHitboxManager()
                );

                // Add the arrow to the game
                //eventHandler.getGlobalController().getAliveActors().add(arrow); // Pk ?
                // Arrow est censé dans ce cas être une Entity, pas un Actor

                // Consume one arrow from inventory
                consumeArrow(player);

                // End animation
                isAnimating = false;
            })
        );

        timeline.play();

        return true;
    }

    private boolean playerHasArrows(Player player) {
        // Check if the player has arrows in inventory
        return player.getInventory().getItemIteration(ItemsEnum.ARROW) > 0;
    }

    private void consumeArrow(Player player) {
        // Consume one arrow from inventory
        player.getInventory().remove(ItemsEnum.ARROW, 1);
    }
}
*/ // TODO : delete on le finira pas