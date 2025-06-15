package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.controller.GlobalController;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.PlayerMouvements;
import fr.iut.hev.root.view.InventoryView;
import fr.iut.hev.root.view.MinimapView;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import static fr.iut.hev.root.controller.GlobalController.mob;

public class KeyInputHandler implements EventHandler<KeyEvent> {

    private Player player;
    private InventoryView inventoryView;
    private MinimapView minimapView;
    private GlobalController globalController;

    public KeyInputHandler(Player player, InventoryView inventoryView) {
        this.player = player;
        this.inventoryView = inventoryView;
        this.minimapView = null;
        this.globalController = null;
    }

    public KeyInputHandler(Player player, InventoryView inventoryView, MinimapView minimapView) {
        this.player = player;
        this.inventoryView = inventoryView;
        this.minimapView = minimapView;
        this.globalController = null;
    }

    public KeyInputHandler(Player player, InventoryView inventoryView, MinimapView minimapView, GlobalController globalController) {
        this.player = player;
        this.inventoryView = inventoryView;
        this.minimapView = minimapView;
        this.globalController = globalController;
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        if (keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED)) {
            switch (keyEvent.getCode()) {
                case KeyCode.Q -> player.addPlayerMouvements(PlayerMouvements.MOVE_LEFT);
                case KeyCode.D -> player.addPlayerMouvements(PlayerMouvements.MOVE_RIGHT);
                case KeyCode.E -> inventoryView.setInventoryVisible();
                case KeyCode.SPACE -> player.addPlayerMouvements(PlayerMouvements.JUMP);
                case KeyCode.TAB -> {
                    System.out.println("map opened");
                    if (minimapView != null) {
                        boolean wasEnlarged = minimapView.isEnlarged();
                        minimapView.toggleEnlargedView();

                        // No longer pausing the game when the minimap is enlarged
                        System.out.println("Minimap toggled without pausing the game");
                    }
                }
                case KeyCode.Z -> {
                    player.receiveDamage(1);
                    System.out.println("pv = " + player.getHealth());
                }
                case KeyCode.Y -> {
                    mob.receiveDamage(1);
                    System.out.println("pv = " + mob.getHealth());
                }
            }
        } else if (keyEvent.getEventType().equals(KeyEvent.KEY_RELEASED)) {
            switch (keyEvent.getCode()) {
                case KeyCode.Q -> player.removePlayerMouvements(PlayerMouvements.MOVE_LEFT);
                case KeyCode.D -> player.removePlayerMouvements(PlayerMouvements.MOVE_RIGHT);
                case KeyCode.SPACE -> player.removePlayerMouvements(PlayerMouvements.JUMP);
            }
        }
    }
}
