package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.inventory.Inventory;
import fr.iut.hev.root.view.HotbarView;
import fr.iut.hev.root.view.InventoryView;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.input.ScrollEvent;
import fr.iut.hev.root.model.items.Item;

/**
 * Gestionnaire des événements de molette pour la navigation dans la hotbar.
 */
public class ScrollInputHandler implements EventHandler<ScrollEvent> {
    private static final int HOTBAR_SIZE = 10;
    private final Inventory inventory;
    private final HotbarView hotbarView;
    private final InventoryView inventoryView;

    private final ObjectProperty<Item> onHandItemProperty;
    private final IntegerProperty quantityProperty;
    private final IntegerProperty directionProperty;
    private int currentSlotIndex;

    public ScrollInputHandler(Inventory inventory, HotbarView hotbarView, InventoryView inventoryView) {
        this.inventory = inventory;
        this.hotbarView = hotbarView;
        this.inventoryView = inventoryView;
        this.onHandItemProperty = new SimpleObjectProperty<>();
        this.quantityProperty = new SimpleIntegerProperty(0);
        this.directionProperty = new SimpleIntegerProperty(0);
        this.currentSlotIndex = 0;

        // Assurer que le slot initial est bien en surbrillance et que les propriétés sont initialisées
        hotbarView.setHighlight(currentSlotIndex);
        Item initialItem = inventory.getInventorySlot(currentSlotIndex).getItem();
        int initialQuantity = inventory.getInventorySlot(currentSlotIndex).getQuantity();
        onHandItemProperty.set(initialItem);
        quantityProperty.set(initialQuantity);
    }

    @Override
    public void handle(ScrollEvent event) {
        if (!inventoryView.getInventoryOpened()) {
            if (event.getDeltaY() > 0) {
                directionProperty.set(1);
            } else if (event.getDeltaY() < 0) {
                directionProperty.set(-1);
            }
            // Note : on ne fait pas l'update directement ici afin de séparer l'événement d'entrée
            // et la logique qui met à jour l'affichage (ex: appelée depuis la boucle de jeu ou un tick).
        }
    }

    public void updateHotbar() {
        int dir = getDirection();
        if (dir == 0) {
            return; // rien à faire si pas de direction
        }

        // Réinitialise l'ancien slot
        hotbarView.resetHighlight(currentSlotIndex);

        // Utilise floorMod pour un wrapping correct (aucune confusion avec les négatifs)
        currentSlotIndex = Math.floorMod(currentSlotIndex + dir, HOTBAR_SIZE);

        // Met à jour l'item et la quantité du nouveau slot
        Item newItem = inventory.getInventorySlot(currentSlotIndex).getItem();
        int newQuantity = inventory.getInventorySlot(currentSlotIndex).getQuantity();
        onHandItemProperty.set(newItem);
        quantityProperty.set(newQuantity);

        // Met en surbrillance le nouveau slot sélectionné
        hotbarView.setHighlight(currentSlotIndex);

        // Réinitialise la direction pour éviter les updates répétées
        directionProperty.set(0);
    }

    // Getters
    public ObjectProperty<Item> onHandItemProperty() {
        return this.onHandItemProperty;
    }

    public IntegerProperty quantityProperty() {
        return this.quantityProperty;
    }

    public IntegerProperty directionProperty() {
        return this.directionProperty;
    }

    public int getDirection() {
        return this.directionProperty.get();
    }
}
