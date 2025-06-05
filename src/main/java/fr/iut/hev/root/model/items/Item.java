package fr.iut.hev.root.model.items;

import fr.iut.hev.root.model.Inventory;
import fr.iut.hev.root.model.entities.Player;
import fr.iut.hev.root.model.enums.Items;
import javafx.beans.property.IntegerProperty;
import javafx.scene.input.MouseEvent;

import java.util.Optional;

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

    public abstract boolean isUsed(Player player, Inventory inventory);
}
