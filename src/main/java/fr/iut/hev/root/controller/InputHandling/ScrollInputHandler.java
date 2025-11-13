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
 * Permet de changer dynamiquement l'item sélectionné dans la hotbar via la molette de la souris.
 * Met à jour l'affichage et les propriétés liées à l'item tenu en main.
 */
public class ScrollInputHandler implements EventHandler<ScrollEvent> {
    private static final int HOTBAR_SIZE = 10; // Nombre de slots dans la hotbar
    private final Inventory inventory; // Référence à l'inventaire du joueur
    private final HotbarView hotbarView; // Vue de la hotbar pour la mise à jour visuelle
    private final InventoryView inventoryView; // Vue de l'inventaire pour vérifier son état

    // Propriétés observables pour la synchronisation avec le modèle
    private final ObjectProperty<Item> onHandItemProperty; // Item actuellement sélectionné dans la hotbar
    private final IntegerProperty quantityProperty; // Quantité de l'item sélectionné
    private final IntegerProperty directionProperty; // Direction du scroll (1: haut, -1: bas, 0: aucun)
    private int currentSlotIndex; // Index du slot actuellement sélectionné dans la hotbar

    /**
     * Constructeur du gestionnaire de molette.
     * Initialise les propriétés et les références aux vues et à l'inventaire.
     */
    public ScrollInputHandler(Inventory inventory, HotbarView hotbarView, InventoryView inventoryView) {
        this.inventory = inventory;
        this.hotbarView = hotbarView;
        this.inventoryView = inventoryView;
        this.onHandItemProperty = new SimpleObjectProperty<>();
        this.quantityProperty = new SimpleIntegerProperty(0);
        this.directionProperty = new SimpleIntegerProperty(0);
        this.currentSlotIndex = 0; // Slot 0 sélectionné par défaut
    }

    /**
     * Gère les événements de molette.
     * Met à jour la direction du scroll si l'inventaire est fermé.
     * - Molette vers le haut : direction = 1 (défilement vers la droite)
     * - Molette vers le bas : direction = -1 (défilement vers la gauche)
     */
    @Override
    public void handle(ScrollEvent event) {
        if (!inventoryView.getInventoryOpened()) {
            if (event.getDeltaY() > 0) {
                directionProperty.set(1); // Défilement vers la droite
            } else if (event.getDeltaY() < 0) {
                directionProperty.set(-1); // Défilement vers la gauche
            }
        }
    }

    /**
     * Met à jour la hotbar en fonction de la direction du scroll.
     * - Réinitialise la mise en surbrillance du slot actuel.
     * - Calcule le nouvel index du slot (en tenant compte de la taille de la hotbar).
     * - Met à jour l'item et la quantité sélectionnés.
     * - Met en surbrillance le nouveau slot sélectionné.
     */
    public void updateHotbar() {
        // Réinitialise la mise en surbrillance du slot actuel
        hotbarView.resetHighlight(currentSlotIndex);

        // Calcule le nouvel index du slot (en boucle)
        currentSlotIndex = (currentSlotIndex + getDirection()) % HOTBAR_SIZE;
        if (currentSlotIndex < 0) {
            currentSlotIndex += HOTBAR_SIZE; // Gestion du modulo négatif
        }

        // Récupère l'item et la quantité du nouveau slot
        Item newItem = inventory.getInventorySlot(currentSlotIndex).getItem();
        int newQuantity = inventory.getInventorySlot(currentSlotIndex).getQuantity();

        // Met à jour les propriétés observables
        onHandItemProperty.set(newItem);
        quantityProperty.set(newQuantity);

        // Met en surbrillance le nouveau slot sélectionné
        hotbarView.setHighlight(currentSlotIndex);

        // Réinitialise la direction pour éviter les mises à jour répétées
        directionProperty.set(0);
    }

    // --- Getters pour les propriétés observables ---
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
