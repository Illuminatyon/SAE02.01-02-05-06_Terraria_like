package fr.iut.hev.root.model;

import fr.iut.hev.root.model.entities.Loot;
import fr.iut.hev.root.model.enums.ItemsEnum;
import fr.iut.hev.root.model.items.ItemFactory;

import java.util.ArrayList;

import static fr.iut.hev.root.model.TileMap.format;

public class TreeManager {

    private ArrayList<Tree> trees;
    private ItemFactory itemFactory;
    private TileMap tileMap;

    public TreeManager(ItemFactory itemFactory,TileMap tileMap){
        this.itemFactory = itemFactory;
        this.tileMap = tileMap;
        this.trees = new ArrayList<>();
        initTreeManager();
    }

    public void initTreeManager() {
        trees.add(new Tree(2,12 * format,12 * format));
    }

    public void treeTakesDamage(int x,int y,int damage) {
        Tree minedTree = getTree(x,y);
        if (minedTree != null) {
            if (minedTree.getHealth() > 0)
                minedTree.takesDamage(damage);
            else {
                Loot droppedLoot = new Loot(itemFactory.createItem(ItemsEnum.WOOD), minedTree.getTaille() * 3, minedTree.getX(), minedTree.getY(), 32, 32, tileMap);
            }
        }
    }

    public Tree getTree(int x,int y) {
        for (Tree tree : trees) {
            if (tree.getX() == x && tree.getY() == y)
                return tree;
        }
        return null;
    }

    public ArrayList<Tree> getTrees() {return this.trees;}
}
