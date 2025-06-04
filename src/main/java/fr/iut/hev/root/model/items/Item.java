package fr.iut.hev.root.model.items;

import fr.iut.hev.root.model.enums.Items;

public abstract class Item {
    private Items item;

    public Item(Items item) {
        this.item = item;
    }

    public Items getItem() {
        return this.item;
    }

    public void setItem(Items item) {
        this.item = item;
    }
}
