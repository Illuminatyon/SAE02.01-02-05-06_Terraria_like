package fr.iut.hev.root.model.inventory;

import fr.iut.hev.root.model.items.Item;

/**
 * Inventaire d'un coffre qui pourrait être dans le jeu.
 * Plus grand que l'inventaire du joueur (100 slots).
 */
public class ChestInventory extends AbstractInventory {
    private static final int CHEST_SIZE = 100;
    private final String chestId;

    public ChestInventory(String chestId) {
        super(CHEST_SIZE);
        this.chestId = chestId;
    }

    @Override
    protected boolean canAcceptItem(Item item) {
        return true;
    }

    @Override
    protected void onItemAdded(Item item, int quantity) {
        System.out.println("Item ajouté au coffre " + chestId);
    }

    public String getChestId() {
        return chestId;
    }
}