package fr.iut.hev.root.model;

import fr.iut.hev.root.model.enums.Items;

public class Item {

    private Items item;

    public Item(Items item) {
        this.item = item;
    }

    public Items getItem() {return this.item;}
    public void setItem(Items item) {this.item = item;}
}
