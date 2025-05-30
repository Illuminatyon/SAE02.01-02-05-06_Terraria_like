package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.Item;
import fr.iut.hev.root.view.InventoryView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;

public class MouseInventoryInputHandler implements EventHandler<MouseEvent> {

    private Inventory inventory;
    private InventoryView inventoryView;
    private HashMap<Item, Integer> onHold;
    private MouseEvent mouseEvent;

    public MouseInventoryInputHandler(Inventory inventory,InventoryView inventoryView) {
        this.inventory = inventory;
        this.inventoryView = inventoryView;
        this.onHold = null;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        this.mouseEvent = mouseEvent;
        if (inventoryView.getInventoryOpened()) {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                onLeftClickPressed();
            }
        }
    }

    public void onLeftClickPressed() {
        int slotIndex = fromTargetStringToInd(mouseEvent.getTarget().toString());
        System.out.println(slotIndex);
        if (slotIndex != -1) {
            if (onHold == null) {
                onHold = inventory.remove(slotIndex, inventory.getInventorySlot(slotIndex).getQuantity());
            } else {
                onHold = inventory.add(slotIndex, onHold.keySet().iterator().next(), onHold.get(onHold.keySet().iterator().next()));
            }
        }
    }

    public int fromTargetStringToInd(String target) {
        String slotString = "";
        int i,slotInd;
        char targetType = target.charAt(0),endingChar;
        System.out.println(targetType);
        if (targetType != 'P' && targetType != 'I')
            slotInd = -1;
        else {
            if (targetType == 'P') {
                i = 8;
                endingChar = ']';
            }
            else {
                i = 13;
                endingChar = ',';
            }
            while (target.charAt(i) != endingChar) {
                slotString += String.valueOf(target.charAt(i));
                i++;
            }
            slotInd = Integer.parseInt(slotString);

        }
        return slotInd;
    }
}
