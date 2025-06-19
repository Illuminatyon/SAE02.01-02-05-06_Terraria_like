package fr.iut.hev.root.model.items;

import fr.iut.hev.root.controller.InputHandling.MouseItemActionInputHandler;
import fr.iut.hev.root.model.entities.Arrow;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.hitbox.HitboxManager;
import fr.iut.hev.root.view.BowView;

public class Bow extends Weapon {
    public static final int ARROW_SPEED = 10;
    private boolean isAnimating = false;
    private ItemFactory itemFactory;

    public Bow(ItemsEnum itemsEnum, HitboxManager hitboxManager, ItemFactory itemFactory) {
        super(itemsEnum, hitboxManager);
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

        // Set animating flag
        isAnimating = true;

        // Calculate the position for the bow based on player position
        double playerCenterX = player.getPosX() + player.getWidth() / 2;
        double playerCenterY = player.getPosY() + player.getHeight() / 2;

        // Calculate direction vector from player to mouse
        // eventHandler.getX() and eventHandler.getY() are already in world coordinates
        // (they have the camera offset subtracted in MouseItemActionInputHandler)
        final double dirX = eventHandler.getX() - playerCenterX;
        final double dirY = eventHandler.getY() - playerCenterY;

        // Normalize the direction vector
        double length = Math.sqrt(dirX * dirX + dirY * dirY);
        final double normalizedDirX = dirX / length;
        final double normalizedDirY = dirY / length;

        // Create or get the bow view and start the animation
        BowView.getInstance(eventHandler.getGlobalController().getEntitiesPane())
               .startBowAnimation(
                   playerCenterX, 
                   playerCenterY, 
                   normalizedDirX, 
                   normalizedDirY, 
                   eventHandler.getGlobalController().getCameraOffsetX(),
                   eventHandler.getGlobalController().getCameraOffsetY(),
                   () -> {
                       // This will be called when the animation completes
                       // Create and shoot an arrow
                       Arrow arrow = createArrow(
                           (int)playerCenterX, 
                           (int)playerCenterY, 
                           normalizedDirX, 
                           normalizedDirY, 
                           eventHandler
                       );

                       // Add the arrow to the game
                       eventHandler.getGlobalController().getAliveActors().add(arrow);

                       // Consume one arrow from inventory
                       consumeArrow(player);

                       // End animation
                       isAnimating = false;
                   }
               );

        return true;
    }

    /**
     * Creates an arrow entity
     */
    public Arrow createArrow(int posX, int posY, double dirX, double dirY, MouseItemActionInputHandler eventHandler) {
        return new Arrow(
            posX, 
            posY, 
            16, 
            4, 
            eventHandler.getTileMap(),
            dirX * ARROW_SPEED, 
            dirY * ARROW_SPEED,
            getDamage(),
            eventHandler.getGlobalController().getEntitiesPane(),
            eventHandler.getGlobalController(),
            itemFactory
        );
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
