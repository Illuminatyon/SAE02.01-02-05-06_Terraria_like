package fr.iut.hev.root.controller.InputHandling;

import Inventory;
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
    /**
     *Gestionnaire d'événement permettant la prise en charge des événements de type souris
     */

    private Inventory inventory;
    private InventoryView inventoryView;
    private ObjectProperty<HashMap<Item, Integer>> onHoldProperty;
    private MouseEvent mouseEvent;
    private DoubleProperty xProperty;
    private DoubleProperty yProperty;

    /**
     * Constructeur du gestionnaire d'entrées souris pour l'inventaire.
     * Initialise les références à l'inventaire et sa vue, et configure les propriétés
     * pour suivre la position du curseur et les objets tenus.
     *
     * @param inventory Référence à l'inventaire du joueur
     * @param inventoryView Vue de l'inventaire du joueur
     */
    public MouseInventoryInputHandler(Inventory inventory, InventoryView inventoryView) {
        this.inventory = inventory;
        this.inventoryView = inventoryView;
        this.onHoldProperty = new SimpleObjectProperty<>(null);
        this.xProperty = new SimpleDoubleProperty(0);
        this.yProperty = new SimpleDoubleProperty(0);
    }

    /**
     * Gère les événements souris pour l'inventaire.
     * Cette méthode traite les clics et les mouvements de la souris lorsque l'inventaire est ouvert,
     * permettant au joueur d'interagir avec les objets de son inventaire.
     *
     * @param mouseEvent L'événement souris à traiter
     */
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

    /**
     * Traite un clic gauche sur l'inventaire.
     * Si un emplacement d'inventaire est cliqué:
     * - Si aucun objet n'est tenu, prend tout le contenu de l'emplacement
     * - Si un objet est déjà tenu, tente de le placer dans l'emplacement cliqué
     * Si aucun emplacement n'est cliqué, l'objet tenu est abandonné.
     */
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

    /**
     * Traite un clic droit sur l'inventaire.
     * Si un emplacement d'inventaire est cliqué et qu'aucun objet n'est tenu,
     * prend la moitié du contenu de l'emplacement.
     */
    public void onRightClickPressed() {
        int slotIndex = fromTargetStringToInd(mouseEvent.getTarget().toString());
        if (slotIndex != -1) {
            if (getOnHold() == null) {
                setOnHold(inventory.remove(slotIndex, inventory.getInventorySlot(slotIndex).getQuantity()/2));
            }
        }
    }
    /**
     * Convertit une chaîne de caractères cible en indice d'emplacement d'inventaire.
     * Cette méthode analyse la chaîne fournie par l'événement souris pour déterminer
     * quel emplacement d'inventaire a été cliqué.
     *
     * @param target Chaîne de caractères représentant la cible du clic
     * @return Indice de l'emplacement d'inventaire, ou -1 si aucun emplacement valide n'a été cliqué
     */
    public int fromTargetStringToInd(String target) {
        String slotString = "";
        int i,slotInd;
        char targetType = target.charAt(0),endingChar = ',';
        if (targetType != 'P' && targetType != 'I')
            slotInd = -1;
        else {
            if (targetType == 'P') {
                i = 8;
            }
            else {
                i = 13;
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
