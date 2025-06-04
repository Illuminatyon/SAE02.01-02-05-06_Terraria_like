package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.items.Item;
import fr.iut.hev.root.view.InventoryView;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;

public class MouseInventoryInputHandler implements EventHandler<MouseEvent> {

    private Inventory inventory;
    private InventoryView inventoryView;
    private ObjectProperty<HashMap<Item, Integer>> onHoldProperty;
    private MouseEvent mouseEvent;
    private DoubleProperty xProperty;
    private DoubleProperty yProperty;

    public MouseInventoryInputHandler(Inventory inventory,InventoryView inventoryView) {
        this.inventory = inventory;
        this.inventoryView = inventoryView;
        this.onHoldProperty = new SimpleObjectProperty<>(null);
        this.xProperty = new SimpleDoubleProperty(0);
        this.yProperty = new SimpleDoubleProperty(0);
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        this.mouseEvent = mouseEvent;
        if (inventoryView.getInventoryOpened()) {
            if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    onLeftClickPressed();
                }
                else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                    onRightClickPressed();
                }
            }
            if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_MOVED)) {
                setX(mouseEvent.getX());
                setY(mouseEvent.getY());
            }
        }
    }

    public void onLeftClickPressed() {
        int slotIndex = fromTargetStringToInd(mouseEvent.getTarget().toString());
        if (slotIndex != -1) {
            if (getOnHold() == null) {
                setOnHold(inventory.remove(slotIndex, inventory.getInventorySlot(slotIndex).getQuantity()));
            } else {
                setOnHold(inventory.add(slotIndex, getOnHold().keySet().iterator().next(), getOnHold().get(getOnHold().keySet().iterator().next())));
            }
        }
        else {
            System.out.println("item droped");
        }
    }

    public void onRightClickPressed() {
        int slotIndex = fromTargetStringToInd(mouseEvent.getTarget().toString());
        if (slotIndex != -1) {
            if (getOnHold() == null) {
                setOnHold(inventory.remove(slotIndex, inventory.getInventorySlot(slotIndex).getQuantity()/2));
            }
        }
    }

    public int fromTargetStringToInd(String target) {
        String slotString = "";
        int i,slotInd;
        char targetType = target.charAt(0),endingChar;
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

    public void setX(double xProperty) {this.xProperty.set(xProperty);}
    public void setY(double yProperty) {this.yProperty.set(yProperty);}
    public Double getX() {return this.xProperty.getValue();}
    public Double getY() {return this.yProperty.getValue();}
    public DoubleProperty yProperty() {return this.yProperty;}
    public DoubleProperty xProperty() {return this.xProperty;}
    public void setOnHold(HashMap<Item, Integer> onHoldProperty) {this.onHoldProperty.set(onHoldProperty);}
    public HashMap<Item, Integer> getOnHold() {return this.onHoldProperty.getValue();}
    public ObjectProperty<HashMap<Item, Integer>> onHoldProperty() {return this.onHoldProperty;}
}
