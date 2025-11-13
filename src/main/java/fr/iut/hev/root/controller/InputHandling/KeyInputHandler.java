package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.controller.InputHandling.ActionKeyEvent.InventoryInput;
import fr.iut.hev.root.controller.InputHandling.ActionKeyEvent.KeyCodeInterface;
import fr.iut.hev.root.controller.InputHandling.ActionKeyEvent.PlayerInput;
import fr.iut.hev.root.view.CraftView;
import fr.iut.hev.root.view.InventoryView;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/**
 * Routeur dynamique des entrées clavier.
 * Bascule entre PlayerInput (mouvements) et InventoryInput (navigation) selon l'état de l'inventaire.
 * Implémente le pattern Strategy.
 *
 * @see KeyCodeInterface
 * @see PlayerInput
 * @see InventoryInput
 */
public class KeyInputHandler implements EventHandler<KeyEvent> {
    private final KeyCodeInterface playerStrategy;
    private final KeyCodeInterface inventoryStrategy;
    private KeyCodeInterface currentStrategy;
    private final InventoryView inventoryView;

    public KeyInputHandler(InventoryView inventoryView, CraftView craftView) {
        this.inventoryView = inventoryView;
        this.playerStrategy = new PlayerInput(inventoryView);
        this.inventoryStrategy = new InventoryInput(inventoryView, craftView);
        this.currentStrategy = playerStrategy;
    }

    @Override
    public void handle(KeyEvent event) {
        this.currentStrategy = inventoryView.getInventoryOpened() ? inventoryStrategy : playerStrategy;
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            currentStrategy.handleKeyPressed(event.getCode());
        } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            currentStrategy.handleKeyReleased(event.getCode());
        }
    }
}
