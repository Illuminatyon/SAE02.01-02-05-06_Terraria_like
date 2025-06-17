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

    public void initTreeManager() {
        trees.add(new Tree(2,15,15));
    }

    public void treeTakesDamage(int x,int y,int damage) {
        for (Tree tree : trees) {
            if (tree.getX() == x && tree.getY() == y) {
                if (tree.getHealth()tree.takesDamage(damage);
            }
        }
    }
}
