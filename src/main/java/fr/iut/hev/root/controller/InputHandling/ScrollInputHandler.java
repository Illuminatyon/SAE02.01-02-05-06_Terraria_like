package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.view.InventoryView;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.input.ScrollEvent;

public class ScrollInputHandler implements EventHandler<ScrollEvent> {

    private InventoryView inventoryView;
    private IntegerProperty directionProperty;

    public ScrollInputHandler(InventoryView inventoryView) {
        this.inventoryView = inventoryView;
        this.directionProperty = new SimpleIntegerProperty(0);
    }

    @Override
    public void handle(ScrollEvent scrollEvent) {
        if (!inventoryView.getInventoryOpened()) {
            setDirection((int)scrollEvent.getDeltaY());
            System.out.println(getDirection());
            System.out.println("One iteration");
        }
    }

    public int getDirection() {return this.directionProperty.getValue();}
    public void setDirection(int directionProperty) {this.directionProperty.setValue(directionProperty);}
    public IntegerProperty directionProperty() {return this.directionProperty;}
}
