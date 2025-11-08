package fr.iut.hev.root.controller.InputHandling.ActionKeyEvent;

import fr.iut.hev.root.view.CraftView;
import fr.iut.hev.root.view.InventoryView;
import javafx.scene.input.KeyCode;

public class InventoryInput implements KeyCodeInterface {

    private final InventoryView inventoryView;
    private final CraftView craftView;

    public InventoryInput(InventoryView inventoryView, CraftView craftView) {
        this.inventoryView = inventoryView;
        this.craftView = craftView;
    }

    @Override
    public void handleKeyPressed(KeyCode code) {
        switch (code) {
            case E -> inventoryView.setInventoryVisible();
            case R -> {
                if (inventoryView.getInventoryOpened()) {
                    craftView.setCraftGUIVisible();
                }
            }
            case TAB -> System.out.println("map opened");
        }
    }

    @Override
    public void handleKeyReleased(KeyCode code) {
        // Pas de relâchement géré ici
    }
}
