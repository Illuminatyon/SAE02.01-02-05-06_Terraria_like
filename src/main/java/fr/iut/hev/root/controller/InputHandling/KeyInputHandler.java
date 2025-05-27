package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.Player;
import fr.iut.hev.root.model.enums.PlayerActions;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyInputHandler implements EventHandler<KeyEvent> {

    private Player player;

    public KeyInputHandler(Player player) {
        this.player = player;
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        if (keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED)) {
            switch (keyEvent.getCode()) {
                case KeyCode.Q -> player.addActiveActions(PlayerActions.MOVE_LEFT);
                case KeyCode.D -> player.addActiveActions(PlayerActions.MOVE_RIGHT);
                case KeyCode.E -> player.addActiveActions(PlayerActions.INVENTORY);
                case KeyCode.SPACE -> player.addActiveActions(PlayerActions.JUMP);
                case KeyCode.TAB -> player.addActiveActions(PlayerActions.MAP);
                case KeyCode.Z -> {
                    player.receiveDamage(1);
                    System.out.println("pv = " + player.getHealth());
                }
            }
        }
        else if (keyEvent.getEventType().equals(KeyEvent.KEY_RELEASED)) {
            switch (keyEvent.getCode()) {
                case KeyCode.Q -> player.removeActiveActions(PlayerActions.MOVE_LEFT);
                case KeyCode.D -> player.removeActiveActions(PlayerActions.MOVE_RIGHT);
                case KeyCode.SPACE -> player.removeActiveActions(PlayerActions.JUMP);
                case KeyCode.TAB -> player.removeActiveActions(PlayerActions.MAP);
            }
        }
    }
}
