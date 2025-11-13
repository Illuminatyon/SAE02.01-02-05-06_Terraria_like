package fr.iut.hev.root.controller.InputHandling;

import fr.iut.hev.root.model.inventory.Inventory;
import fr.iut.hev.root.model.inventory.InventorySlot;
import fr.iut.hev.root.model.items.Item;
import fr.iut.hev.root.view.InventoryView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;

/**
 * Gestionnaire des interactions souris dans l'inventaire.
 * Permet de déplacer les items entre les slots via clics gauche/droit,
 * avec un aperçu visuel de l'item tenu par le curseur.
 */
public class MouseInventoryInputHandler implements EventHandler<MouseEvent> {
    private final Inventory inventory;
    private final InventoryView inventoryView;
    private Item heldItem; // Item actuellement tenu par le curseur
    private int heldQuantity; // Quantité de l'item tenu
    private int sourceSlotIndex; // Index du slot source de l'item tenu

    public MouseInventoryInputHandler(Inventory inventory, InventoryView inventoryView) {
        this.inventory = inventory;
        this.inventoryView = inventoryView;
    }

    /**
     * Gère les événements souris (mouvement, clic gauche/droit).
     * - Met à jour la position de l'aperçu de l'item tenu.
     * - Traite les clics pour prendre/déposer des items.
     */
    @Override
    public void handle(MouseEvent event) {
        if (!inventoryView.getInventoryOpened()) return;

        // Met à jour la position de l'aperçu de l'item tenu
        if (event.getEventType() == MouseEvent.MOUSE_MOVED || event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            updateHeldItemDisplay(event.getX(), event.getY());
        }

        // Traite les clics pour prendre/déposer des items
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            int clickedSlot = getSlotIndexFromEvent(event);
            if (clickedSlot == -1) return;

            if (event.getButton() == MouseButton.PRIMARY) {
                handleLeftClick(clickedSlot); // Clic gauche : prendre/déposer tout le stack
            } else if (event.getButton() == MouseButton.SECONDARY) {
                handleRightClick(clickedSlot); // Clic droit : prendre/déposer la moitié du stack
            }
        }
    }

    /**
     * Met à jour l'affichage de l'item tenu par le curseur.
     * Affiche un aperçu visuel de l'item à côté du curseur.
     */
    private void updateHeldItemDisplay(double mouseX, double mouseY) {
        if (heldItem != null) {
            HashMap<Item, Integer> heldItemMap = new HashMap<>();
            heldItemMap.put(heldItem, heldQuantity);
            inventoryView.updateOnHoldPane(heldItemMap);
            inventoryView.updateOnHoldPosition(mouseX + 10, mouseY - 10);
        } else {
            inventoryView.updateOnHoldPane(null);
        }
    }

    /**
     * Gère le clic gauche : prendre ou déposer tout le stack d'un item.
     * - Si aucun item n'est tenu, prend tout le stack du slot cliqué.
     * - Si un item est tenu, le dépose dans le slot cliqué (fusionne si possible).
     */
    private void handleLeftClick(int targetSlot) {
        InventorySlot slot = inventory.getInventorySlot(targetSlot);

        // Si aucun item n'est tenu, prendre tout le stack du slot cliqué
        if (heldItem == null) {
            if (slot.getItem() != null) {
                heldItem = slot.getItem();
                heldQuantity = slot.getQuantity();
                sourceSlotIndex = targetSlot;
                slot.setItem(null);
                slot.setQuantity(0);
            }
        }
        // Si un item est tenu, le déposer dans le slot cliqué
        else {
            // Si le slot est vide, déposer tout l'item tenu
            if (slot.getItem() == null) {
                slot.setItem(heldItem);
                slot.setQuantity(heldQuantity);
                clearHeldItem();
            }
            // Si le slot contient le même type d'item, fusionner les stacks
            else if (slot.getItem().getItemEnum() == heldItem.getItemEnum()) {
                int spaceAvailable = slot.getItem().getItemEnum().getLimitStacking() - slot.getQuantity();
                if (spaceAvailable >= heldQuantity) {
                    slot.setQuantity(slot.getQuantity() + heldQuantity);
                    clearHeldItem();
                } else {
                    slot.setQuantity(slot.getItem().getItemEnum().getLimitStacking());
                    heldQuantity -= spaceAvailable;
                }
            }
            // Si le slot contient un item différent, échanger les items
            else {
                Item tempItem = slot.getItem();
                int tempQuantity = slot.getQuantity();
                slot.setItem(heldItem);
                slot.setQuantity(heldQuantity);
                heldItem = tempItem;
                heldQuantity = tempQuantity;
                sourceSlotIndex = targetSlot;
            }
        }
    }

    /**
     * Gère le clic droit : prendre ou déposer la moitié du stack d'un item.
     * - Si aucun item n'est tenu, prend la moitié du stack du slot cliqué.
     * - Si un item est tenu, dépose 1 unité dans le slot cliqué (fusionne si possible).
     */
    private void handleRightClick(int targetSlot) {
        InventorySlot slot = inventory.getInventorySlot(targetSlot);

        // Si aucun item n'est tenu, prendre la moitié du stack du slot cliqué
        if (heldItem == null) {
            if (slot.getItem() != null && slot.getQuantity() > 0) {
                int half = (slot.getQuantity() + 1) / 2;
                heldItem = slot.getItem();
                heldQuantity = half;
                sourceSlotIndex = targetSlot;
                slot.setQuantity(slot.getQuantity() - half);
                if (slot.getQuantity() == 0) {
                    slot.setItem(null);
                }
            }
        }
        // Si un item est tenu, déposer 1 unité dans le slot cliqué
        else {
            // Si le slot est vide, déposer 1 unité de l'item tenu
            if (slot.getItem() == null) {
                slot.setItem(heldItem);
                slot.setQuantity(1);
                heldQuantity--;
                if (heldQuantity == 0) {
                    clearHeldItem();
                }
            }
            // Si le slot contient le même type d'item, ajouter 1 unité
            else if (slot.getItem().getItemEnum() == heldItem.getItemEnum()) {
                if (slot.getQuantity() < slot.getItem().getItemEnum().getLimitStacking()) {
                    slot.setQuantity(slot.getQuantity() + 1);
                    heldQuantity--;
                    if (heldQuantity == 0) {
                        clearHeldItem();
                    }
                }
            }
        }
    }

    /**
     * Extrait l'index du slot depuis l'événement souris.
     * Parse la chaîne de caractères de l'événement pour récupérer l'ID du slot cliqué.
     *
     * @return L'index du slot, ou -1 si l'ID n'a pas pu être extrait.
     */
    private int getSlotIndexFromEvent(MouseEvent event) {
        try {
            String target = event.getTarget().toString();
            int idStart = target.indexOf("id=") + 3;
            int idEnd = target.indexOf(",", idStart);
            if (idEnd == -1) idEnd = target.indexOf("]", idStart);
            return Integer.parseInt(target.substring(idStart, idEnd));
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Réinitialise l'item tenu par le curseur.
     * Efface l'aperçu visuel et réinitialise les variables.
     */
    private void clearHeldItem() {
        heldItem = null;
        heldQuantity = 0;
        sourceSlotIndex = -1;
        inventoryView.updateOnHoldPane(null);
    }
}
