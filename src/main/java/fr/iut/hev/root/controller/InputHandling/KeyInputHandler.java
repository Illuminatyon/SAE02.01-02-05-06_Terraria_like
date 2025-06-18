package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.Player;
import fr.iut.hev.root.model.World;
import fr.iut.hev.root.model.enums.PlayerActions;
import fr.iut.hev.root.utils.JsonManager;
import fr.iut.hev.root.utils.SaveManager;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class KeyInputHandler implements EventHandler<KeyEvent> {
    private World world;
    private Player player;

    public KeyInputHandler(World world) {
        this.world = world;
        this.player = world.getPlayer();
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
                case KeyCode.ESCAPE -> {
                    try {
                        SaveManager.saveWorld(world);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        else if (keyEvent.getEventType().equals(KeyEvent.KEY_RELEASED)) {
            switch (keyEvent.getCode()) {
                case KeyCode.Q -> player.removeActiveActions(PlayerActions.MOVE_LEFT);
                case KeyCode.D -> player.removeActiveActions(PlayerActions.MOVE_RIGHT);
                case KeyCode.SPACE -> player.removeActiveActions(PlayerActions.JUMP);
            }
        }
    }
}
