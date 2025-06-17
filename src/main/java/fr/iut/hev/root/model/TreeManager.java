package fr.iut.hev.root.model;

import fr.iut.hev.root.model.items.ItemFactory;

import java.util.ArrayList;

public class TreeManager {

    private ArrayList<Tree> trees;
    private ItemFactory itemFactory;

    public TreeManager(ItemFactory itemFactory) {
        this.itemFactory = itemFactory;
        this.trees = new ArrayList<>();
    }
}
