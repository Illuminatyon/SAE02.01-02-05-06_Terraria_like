package fr.iut.hev.root.model;

public class InventorySlot {
    private Item item;
    private int quantity;
    private final int position; // peut etre inutile

    public InventorySlot(int position) {
        item = null;
        quantity = 0;
        this.position = position;
    }

    public void remove() {
        item = null;
        quantity = 0;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {

    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }
}
