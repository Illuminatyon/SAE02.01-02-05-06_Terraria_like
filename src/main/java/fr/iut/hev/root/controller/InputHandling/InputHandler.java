
package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.entities.actor.Player;
import fr.iut.hev.root.view.*;
import javafx.application.Platform;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;

/**
 * Façade pour la gestion centralisée des entrées utilisateur (clavier, souris, molette).
 * Coordonne les gestionnaires spécialisés et configure les bindings entre modèle et vue.
 *
 * @see KeyInputHandler
 * @see MouseInventoryInputHandler
 * @see MouseItemActionInputHandler
 * @see ScrollInputHandler
 */
public class InputHandler {
    private KeyInputHandler keyInputHandler;
    private MouseInventoryInputHandler mouseInventoryInputHandler;
    private MouseItemActionInputHandler mouseItemActionInputHandler;
    private ScrollInputHandler scrollInputHandler;

    /**
     * Initialise les gestionnaires d'entrées avec leurs dépendances.
     *
     * @param inventoryView Vue de l'inventaire
     * @param craftView Vue du craft
     * @param camera Caméra du jeu
     * @param tileMapView Vue de la carte de tuiles
     * @param hotbarView Vue de la barre d'accès rapide
     */
    public InputHandler(InventoryView inventoryView, CraftView craftView, Camera camera, TileMapView tileMapView, HotbarView hotbarView) {
        this.keyInputHandler = new KeyInputHandler(inventoryView, craftView);
        this.mouseInventoryInputHandler = new MouseInventoryInputHandler(Player.getInstance().getInventory(), inventoryView);
        this.mouseItemActionInputHandler = new MouseItemActionInputHandler(camera, inventoryView, tileMapView);
        this.scrollInputHandler = new ScrollInputHandler(Player.getInstance().getInventory(), hotbarView, inventoryView);
    }

    /**
     * Configure les bindings entre le modèle du joueur et le gestionnaire de molette.
     */
    private void initScrollInputHandler() {
        Player player = Player.getInstance();
        player.itemInHandProperty().bindBidirectional(scrollInputHandler.onHandItemProperty());
        player.quantityOfItemInHandProperty().bindBidirectional(scrollInputHandler.quantityProperty());
        player.itemInHandProperty().addListener((observableValue, item, t1) -> mouseItemActionInputHandler.updateCooldown());
        scrollInputHandler.directionProperty().addListener((observableValue, number, t1) -> {
            if (scrollInputHandler.getDirection() != 0)
                scrollInputHandler.updateHotbar();
        });
    }

    /**
     * Initialise les gestionnaires d'entrées et enregistre les event handlers sur la scène JavaFX.
     *
     * @param landTileMap Grille de tuiles du terrain
     * @param hudAnchorPane Panneau HUD
     */
    public void initInputHandler(TilePane landTileMap, AnchorPane hudAnchorPane) {
        initScrollInputHandler();
        Platform.runLater(() -> {
            landTileMap.getScene().addEventHandler(KeyEvent.ANY, keyInputHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, mouseItemActionInputHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_RELEASED, mouseItemActionInputHandler);
            landTileMap.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseItemActionInputHandler);
            hudAnchorPane.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseInventoryInputHandler);
            hudAnchorPane.addEventHandler(MouseEvent.MOUSE_MOVED, mouseInventoryInputHandler);
            landTileMap.getScene().addEventHandler(ScrollEvent.SCROLL, scrollInputHandler);
        });
    }

    /**
     * @return Le gestionnaire d'actions souris dans le monde.
     */
    public MouseItemActionInputHandler getMouseItemActionInputHandler() {
        return this.mouseItemActionInputHandler;
    }
}
