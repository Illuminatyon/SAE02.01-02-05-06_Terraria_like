package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.controller.InputHandling.ActionKeyEvent.InventoryInput;
import fr.iut.hev.root.controller.InputHandling.ActionKeyEvent.KeyCodeInterface;
import fr.iut.hev.root.controller.InputHandling.ActionKeyEvent.PlayerInput;
import fr.iut.hev.root.model.entities.actor.Player;
import fr.iut.hev.root.view.CraftView;
import fr.iut.hev.root.view.InventoryView;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class KeyInputHandler implements EventHandler<KeyEvent> {

    private final Player player = Player.getInstance();
    private final InventoryView inventoryView;
    private final CraftView craftView;
    private final KeyCodeInterface playerStrategy;
    private final KeyCodeInterface inventoryStrategy;
    private KeyCodeInterface currentStrategy;

    public KeyInputHandler(InventoryView inventoryView, CraftView craftView) {
        this.inventoryView = inventoryView;
        this.craftView = craftView;
        this.playerStrategy = new PlayerInput();
        this.inventoryStrategy = new InventoryInput(inventoryView, craftView);
        this.currentStrategy = playerStrategy;
    }

    @Override
    public void handle(KeyEvent event) {
        // Choix de la stratégie selon l’état de l’inventaire
        currentStrategy = inventoryView.getInventoryOpened() ? inventoryStrategy : playerStrategy;
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            currentStrategy.handleKeyPressed(event.getCode());
        } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            currentStrategy.handleKeyReleased(event.getCode());
        }
    }
}
