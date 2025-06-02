package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.view.InventoryView;
import javafx.event.EventHandler;
import javafx.scene.input.ScrollEvent;

public class ScrollInputHandler implements EventHandler<ScrollEvent> {

    private InventoryView inventoryView;
    private int direction;

    public ScrollInputHandler(InventoryView inventoryView) {
        this.inventoryView = inventoryView;
        this.direction = 0;
    }

    @Override
    public void handle(ScrollEvent scrollEvent) {
        if (!inventoryView.getInventoryOpened()) {
            direction = (int)scrollEvent.getDeltaY();
        }
    }

    public int getDirection() {return this.direction;}
}
