package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.PlayerMouvementsEnum;
import fr.iut.hev.root.view.CraftView;
import fr.iut.hev.root.view.InventoryView;
import fr.iut.hev.root.model.World;
import fr.iut.hev.root.utils.SaveManager;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class KeyInputHandler implements EventHandler<KeyEvent> {
    private World world;
    private Player player;
    private InventoryView inventoryView;
    private CraftView craftView;

    public KeyInputHandler(World world, InventoryView inventoryView, CraftView craftView) {
        this.world = world;
        this.player = world.getPlayer();
        this.inventoryView = inventoryView;
        this.craftView = craftView;
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        if (keyEvent.getEventType().equals(KeyEvent.KEY_PRESSED)) {
            switch (keyEvent.getCode()) {
                case KeyCode.Q -> player.addPlayerMouvements(PlayerMouvementsEnum.MOVE_LEFT);
                case KeyCode.D -> player.addPlayerMouvements(PlayerMouvementsEnum.MOVE_RIGHT);
                case KeyCode.E -> inventoryView.setInventoryVisible();
                case KeyCode.R -> {
                    if (inventoryView.getInventoryOpened())
                        craftView.setCraftGUIVisible();
                }
                case KeyCode.SPACE -> player.addPlayerMouvements(PlayerMouvementsEnum.JUMP);
                case KeyCode.TAB -> System.out.println("map opened");
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
        } else if (keyEvent.getEventType().equals(KeyEvent.KEY_RELEASED)) {
            switch (keyEvent.getCode()) {
                case KeyCode.Q -> player.removePlayerMouvements(PlayerMouvementsEnum.MOVE_LEFT);
                case KeyCode.D -> player.removePlayerMouvements(PlayerMouvementsEnum.MOVE_RIGHT);
                case KeyCode.SPACE -> player.removePlayerMouvements(PlayerMouvementsEnum.JUMP);
            }
        }
    }
}
