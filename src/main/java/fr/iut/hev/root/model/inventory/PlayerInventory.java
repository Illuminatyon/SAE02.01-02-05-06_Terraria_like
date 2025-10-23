package fr.iut.hev.root.model.inventory;

import fr.iut.hev.root.model.items.Item;

/**
 * Inventaire du joueur principal.
 * Accepte tous les types d'items et a une capacité de 50 slots.
 */
public class PlayerInventory extends AbstractInventory {
    private static final int PLAYER_INVENTORY_SIZE = 50;

    public PlayerInventory() {
        super(PLAYER_INVENTORY_SIZE);
    }

    @Override
    protected boolean canAcceptItem(Item item) {
        // Le joueur peut ramasser n'importe quel item
        return true;
    }

    @Override
    protected void onInventoryFull() {
        // Tu peux ajouter ici une notification, un son, etc.
        System.out.println("Inventaire du joueur plein !");
    }
}